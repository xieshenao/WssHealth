package cn.wss.service.impl;

import cn.wss.dao.OrderSettingDao;
import cn.wss.pojo.OrderSetting;
import cn.wss.service.OrderSettingService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Override
    public void add(List<OrderSetting> orderSettings) {
        //先判断是否有数据
        if(orderSettings != null && orderSettings.size() > 0){
            //在service业务层进行判断新增还是更新
            //遍历数据
            for (OrderSetting orderSetting : orderSettings) {
                //判断是否有这个数据
                long date = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                if(date > 0){
                    //如果大于0就说明数据库有数据，进行更新操作
                    orderSettingDao.update(orderSetting);
                }else{
                    //数据库没有数据，就执行增加
                    orderSettingDao.add(orderSetting);
                }
            }
        }else{
            throw new RuntimeException("没有数据欸");
        }


    }

    @Override
    public List<Map> getOrderSettingByMonth(String date) {//date:2020-7
        //对数据进行拼接，
        String begin = date+"-1";//2020-7-1

        String end = date+"-31";//2020-7-31
        //创建map集合存储日期
        Map<String,String>map = new HashMap<>();
        map.put("begin",begin);
        map.put("end",end);
        //创建集合封装map
        List<Map>result = new ArrayList<>();
        //在数据库进行查询所有内容
        List<OrderSetting> list = orderSettingDao.getBegin(map);
        //判断是否有内容
        if(list != null && list.size() > 0){
            //日期类型转换
            Calendar calendar = Calendar.getInstance();
            //对集合进行遍历
            for (OrderSetting orderSetting : list) {
                //创建map集合对数据进行封装,每次循环创建一个新的map集合，防止只存储一条数据
                Map<String,Object>map1 = new HashMap<>();
                calendar.setTime(orderSetting.getOrderDate());
                //获取日期数字（几号）
                map1.put("date",calendar.get(Calendar.DAY_OF_MONTH));
                map1.put("number",orderSetting.getNumber());//总预约人数
                map1.put("reservations",orderSetting.getReservations());//已预约人数

                //集合封装map
                result.add(map1);
            }
        }
        return result;
    }

    @Override
    public void update(OrderSetting orderSetting) {
        long date = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if(date > 0){
            //如果大于0就说明数据库有数据，进行更新操作
            orderSettingDao.update(orderSetting);
        }else{
            //数据库没有数据，就执行增加
            orderSettingDao.add(orderSetting);
        }
    }


}
