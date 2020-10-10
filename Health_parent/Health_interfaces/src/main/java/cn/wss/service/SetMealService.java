package cn.wss.service;

import cn.wss.entity.PageResult;
import cn.wss.entity.QueryPageBean;
import cn.wss.pojo.SetMealNumber;
import cn.wss.pojo.Setmeal;

import java.util.List;

public interface SetMealService {
    //分页查询
    public PageResult findPage(QueryPageBean queryPageBean);
    //添加
    public void add(Setmeal setmeal,Integer[] checkgroupIds);
    //根据id擦查询中间表
    public Integer[] findByCheckGroupId(Integer id);
    //编辑
    public void update(Integer[] checkGroupIds,Setmeal setmeal);
    //删除
    public void delete(Integer id);
    //查询所有套餐数据
    public List<Setmeal> findAll();
    //根据id查询所有的数据(检查项、检查组、套餐)
    public Setmeal findById(Integer id);
    //查询套餐数量
    public List<SetMealNumber>findSetMealCount();
}
