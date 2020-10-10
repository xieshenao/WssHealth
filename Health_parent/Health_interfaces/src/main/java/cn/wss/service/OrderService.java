package cn.wss.service;

import cn.wss.entity.Result;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface OrderService {
    public Result order(Map map) throws Exception;
    //根据id查询预约体检信息
    public Map findById(Integer id)throws Exception;
}
