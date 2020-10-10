package cn.wss.dao;

import cn.wss.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {
    //根据日期查询
    public long findCountByOrderDate(Date date);
    //新增
    public void add(OrderSetting setting);
    //更新
    public void update(OrderSetting orderSetting);
    //查询一个月的日历信息
    public List<OrderSetting> getBegin(Map map);
    //更新已预约人数
    public void updateReservations(OrderSetting orderSetting);
}
