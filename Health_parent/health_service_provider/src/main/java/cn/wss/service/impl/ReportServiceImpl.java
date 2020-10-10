package cn.wss.service.impl;

import cn.wss.dao.MemberDao;
import cn.wss.dao.OrderDao;
import cn.wss.dao.SetMealDao;
import cn.wss.pojo.HotSetmeal;
import cn.wss.service.ReportService;
import cn.wss.utils.DateUtils;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.NumberFormat;
import java.util.*;

@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private SetMealDao setMealDao;
    /**
     * 查询前端用到的所有数据
     * @return
     */
    @Override
    public Map<String, Object> getBusinessReport() throws Exception{
        //今日日期
        String reportDate = DateUtils.parseDate2String(new Date());
        //本日新增会员数
        Integer todayNewMember =  memberDao.findByThisToday(reportDate);
        //本周新增会员数
        String thisWeekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());//获取本周第一天
        Integer thisWeekNewMember = memberDao.findByThisWeek(thisWeekMonday);
        //本月新增会员数
        String firstDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
        Integer thisMonthNewMember = memberDao.findByThisMonth(firstDay4ThisMonth);
        //总会员数
        Integer totalMember = memberDao.findCount();

        //今日预约数
        Integer todayOrderNumber = orderDao.findByThisOrder(reportDate);
        //今日到诊数
        Integer todayVisitsNumber = orderDao.findByThisVisits(reportDate);
        //本周预约数
        Integer thisWeekOrderNumber = orderDao.findByThisOrder(thisWeekMonday);
        //本周到诊数
        Integer thisWeekVisitsNumber = orderDao.findByThisVisits(thisWeekMonday);
        //本月预约数
        Integer thisMonthOrderNumber = orderDao.findByThisOrder(firstDay4ThisMonth);
        //本月到诊数
        Integer thisMonthVisitsNumber = orderDao.findByThisVisits(firstDay4ThisMonth);

        //热门套餐
        //查询预约人数占前四个的套餐名称及其预约数量
        List<HotSetmeal> hotSetmeal = setMealDao.findSetMealLimitFour();
        //查询出总预约数量
        Integer byAll = orderDao.findByAll();
        double v = Double.parseDouble(byAll+"");
        //计算预约占比
        for (HotSetmeal setmeal : hotSetmeal) {
            Double count = Double.parseDouble(setmeal.getSetmeal_count());
            Double i = count / v;
           /* Double j = Math.round(i,3);*/
            setmeal.setProportion(i);
        }
        //把所有数据加到map集合中
        Map<String,Object> result = new HashMap<>();
        result.put("reportDate",reportDate);
        result.put("todayNewMember",todayNewMember);
        result.put("totalMember",totalMember);
        result.put("thisWeekNewMember",thisWeekNewMember);
        result.put("thisMonthNewMember",thisMonthNewMember);
        result.put("todayOrderNumber",todayOrderNumber);
        result.put("thisWeekOrderNumber",thisWeekOrderNumber);
        result.put("thisMonthOrderNumber",thisMonthOrderNumber);
        result.put("todayVisitsNumber",todayVisitsNumber);
        result.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        result.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        result.put("hotSetmeal",hotSetmeal);
        return result;
    }
}
