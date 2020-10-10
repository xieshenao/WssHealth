package cn.wss.dao;

import cn.wss.pojo.Permission;

import java.util.Set;

public interface PermissionDao {
    //根据角色id查询权限信息
    public Set<Permission> findPermissionByRoleId(Integer roleId);
}
