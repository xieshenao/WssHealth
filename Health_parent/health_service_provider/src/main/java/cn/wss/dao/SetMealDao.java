package cn.wss.dao;

import cn.wss.pojo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SetMealDao {
    //分页查询
    public List<Setmeal>findSetMealAll(String queryString);
    //添加
    public void addSetMeal(Setmeal setmeal);
    //添加中间表数据
    public void addSetMealAndCheckGroup(@Param("setmealId")Integer setmealId,@Param("checkGroupIds") Integer[] checkGroupIds);
    //根据id擦查询中间表
    public Integer[] findByCheckGroupId(@Param("id") Integer id);
    //更新套餐
    public void updateSetMeal(Setmeal setmeal);
    //删除用户取消的复选框
    public void deleteSetMealAndCheckGroup(@Param("id") Integer id,@Param("checkGroupIds") Integer[] checkGroupIds);
    //删除
    public void delete(Integer id);
    //查询所有套餐数据
    public List<Setmeal> findAll();
    //根据id查询套餐
    public Setmeal findSetMealById(@Param("id") Integer id);
    //根据套餐id查询检查组数据
    public List<CheckGroup> findCheckGroupBySetMealId(@Param("id") Integer id);
    //根据检查组id查询检查项id
    public List<CheckItem> findCheckItemByCheckGroup(@Param("checkGroupId")Integer checkGroupId);
    //查询套餐数量
    public List<SetMealNumber>findNameValue();
    //查询预约人数占前四名的套餐名称
    public List<HotSetmeal> findSetMealLimitFour();
}
