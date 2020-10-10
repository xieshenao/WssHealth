package cn.wss.service.impl;

import cn.wss.constant.MessageConstant;
import cn.wss.dao.MemberDao;
import cn.wss.dao.OrderDao;
import cn.wss.dao.OrderSettingDao;
import cn.wss.entity.Result;
import cn.wss.pojo.Member;
import cn.wss.pojo.Order;
import cn.wss.pojo.OrderSetting;
import cn.wss.service.OrderService;
import cn.wss.utils.DateUtils;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private JedisPool jedisPool;

    @Override
    public Result order(Map map) throws Exception{
        /**
         * 1:检测用户选择的日期是否提前进行了预约设置
         */
        String orderDate = (String) map.get("orderDate");//获取预约日期
        //转换成date对象
        Date date = DateUtils.parseString2Date(orderDate);
        //查询预约设置是否有这个日期
        OrderSetting orderSetting = orderDao.findByOrderDate(date);
        if(orderSetting == null){
            //如果没有指定预约设置，返回错误信息
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        /**
         * 2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
         */
        int number = orderSetting.getNumber();//可预约人数
        int reservations = orderSetting.getReservations();//已预约人数
        if(reservations >= number){
            //说明已经约满了
            return new Result(false,MessageConstant.ORDER_FULL);
        }

        /**
         * 判断会员表中是否有数据，
         * 如果有数据就判断是否重复预约
         * 没有数据，就自动注册成为会员
         */
        /**
         * 3、检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约
         */
        String telephone = (String)map.get("telephone");
        //根据电话号码判断会员表中是否有重复的数据
        Member member = memberDao.findByTelephone(telephone);
        if(member != null){
            //不等于空，就说明还用户是会员，判断是否存在重复预约
            Integer id = member.getId();//会员ID
            Date order_date = date; //预约日期
            Integer setmealId = Integer.parseInt((String)map.get("setmealId")) ;//套餐ID
            //封装进order（体检预约信息）中
            Order order = new Order(id,order_date,setmealId);
            //查询预约信息中是否有数据
            List<Order> list = orderDao.findByOrder(order);

            if(list != null && list.size() > 0){
                //如果list不为空，就说明预约信息中已经有这个预约信息了，重复预约了
                return new Result(false,MessageConstant.HAS_ORDERED);
            }

        }else{
            //4、如果是新用户，自动注册预约(往会员表中插入该用户信息)
            member = new Member();
            member.setName((String)map.get("name"));//姓名
            member.setSex((String)map.get("sex"));//性别
            member.setIdCard((String)map.get("idCard"));//身份证号
            member.setPhoneNumber(telephone);//电话号
            member.setRegTime(new Date());//注册时间(当前时间)
            //注册
            memberDao.addMember(member);
        }

        //如果预约信息是空的就说明没有重复预约，进行预约
        /**
         * 5、预约成功，更新当日的已预约人数
         */
        //添加预约信息
        Order order = new Order();
        order.setMemberId(member.getId());//会员id
        order.setOrderDate(date);//预约日期
        order.setOrderType((String)map.get("orderType"));//预约类型
        order.setOrderStatus(Order.ORDERSTATUS_NO);//到诊状态
        order.setSetmealId(Integer.parseInt((String)map.get("setmealId")));//套餐id
        orderDao.addOrder(order);

        //设置预约人数+1
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        //更新预约设置表
        orderSettingDao.update(orderSetting);
        return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
    }

    @Override
    public Map findById(Integer id) throws Exception{
        Map order = orderDao.findById(id);
        if(order != null){
            //处理日期格式
            Date orderDate = (Date) order.get("orderDate");
            order.put("orderDate",DateUtils.parseDate2String(orderDate));
        }

        return order;
    }
}
