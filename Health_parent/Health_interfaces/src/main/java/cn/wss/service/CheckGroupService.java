package cn.wss.service;

import cn.wss.entity.PageResult;
import cn.wss.entity.QueryPageBean;
import cn.wss.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {
    //分页查询
    public PageResult findPage(QueryPageBean queryPageBean);
    //添加检查组
    public void add(Integer checkitemIds[], CheckGroup checkGroup);
    //根据检查组id查检查项id
    public Integer[] findCheckItemIdsByCheckGroupId(Integer id);
    //编辑
    public void update(Integer checkItemIds[],CheckGroup checkGroup);
    //删除
    public void delete(Integer id);
    //查询
    public List<CheckGroup>findAll();
}
