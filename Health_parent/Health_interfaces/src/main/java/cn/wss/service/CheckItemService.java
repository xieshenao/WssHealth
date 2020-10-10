package cn.wss.service;

import cn.wss.entity.PageResult;
import cn.wss.entity.QueryPageBean;
import cn.wss.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {
    public static final long ERROR_FINDBYID = -1;
    public static final long SUCCESS_FINDBYID = 1;

    //增加检查项信息
    public void add(CheckItem checkItem);

    //根据项目编码和项目名称查询
    public List<CheckItem> findCheckItemCodeAndName(String code,String name);

    //分页查询方法
    public PageResult pageQuery(QueryPageBean queryPageBean);

    //更新
    public Long update(CheckItem checkItem);

    //删除
    public void delete(Integer id);

    //查询
    public List<CheckItem>findAll();
}
