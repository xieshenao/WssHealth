package cn.wss.dao;

import cn.wss.pojo.Order;
import cn.wss.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;
import org.omg.CORBA.INTERNAL;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderDao {
    //根据日期查询预约设置表
    public OrderSetting findByOrderDate(@Param("date") Date date);
    //查询预约信息表是否有数据
    public List<Order>findByOrder(Order order);
    //添加预约信息
    public void addOrder(Order order);
    //根据预约信息id查询出其他两表的内容
    public Map findById(@Param("id") Integer id);

    //查询在这日期之后的预约人数
    public Integer findByThisOrder(String date);
    //查询在这日期的到诊人数
    public Integer findByThisVisits(String date);
    //查询总预约人数
    public Integer findByAll();
}
