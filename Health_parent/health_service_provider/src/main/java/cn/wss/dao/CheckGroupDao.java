package cn.wss.dao;

import cn.wss.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CheckGroupDao {
    //模糊查询
    public List<CheckGroup>findAllBy(String queryString);

    //添加检查组信息
    public void addCheckGroup(CheckGroup checkGroup);
    //添加中间表的数据
    public void addCheckGroupCheckItem(@Param("checkGroupId")Integer checkGroupId,@Param("checkitemIds") Integer[] checkitemIds);
    //根据检查组id查询检查项id
    public Integer[] findCheckItemIdsByCheckGroupId(@Param("id")Integer id);

    //更新检查组
    public void updateCheckGroup(CheckGroup checkGroup);

    //删除取消的复选框选项
    public void batchDeleteCheckGroupAndCheckItem(@Param("checkGroupId")Integer checkGroupId,@Param("checkItemIds")Integer[] checkItemIds);

    //删除检查组
    public void deleteCheckGroup(@Param("id") Integer id);
    //删除中间表
    public void deleteCheckGroupAndCheckItem(@Param("id") Integer id);

    //查询
    public List<CheckGroup>findAll();
}
