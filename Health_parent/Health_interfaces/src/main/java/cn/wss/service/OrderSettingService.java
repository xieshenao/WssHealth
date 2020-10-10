package cn.wss.service;

import cn.wss.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    //添加用户提交的文件
    public void add(List<OrderSetting>orderSettings);
    //查询一个月的日历信息
    public List<Map>getOrderSettingByMonth(String date);
    //更新可预约人数
    public void update(OrderSetting orderSetting);
}
