package cn.wss.dao;

import cn.wss.pojo.Role;

import java.util.Set;

public interface RoleDao {
    //根据用户id查询角色信息
    public Set<Role>findRoleByUserId(Integer userId);
}
