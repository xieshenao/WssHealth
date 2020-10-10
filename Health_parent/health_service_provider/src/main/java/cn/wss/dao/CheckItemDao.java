package cn.wss.dao;

import cn.wss.entity.QueryPageBean;
import cn.wss.entity.Result;
import cn.wss.pojo.CheckItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CheckItemDao {
    public void add(CheckItem checkItem);
    //根据项目编码和项目名称查询
    public List<CheckItem> findCheckItemCodeAndName(@Param("code") String code, @Param("name") String name);
    //分页查询方法
    public List<CheckItem> selectByCondition(String queryString);
    //更新
    public Long update(CheckItem checkItem);
    //删除
    public void delete(Integer id);
    //根据检查项查询中间表是否关联的有其他数据
    public long findCountByCheckItemId(Integer checkItemId);
    //查询
    public List<CheckItem>findAll();}

