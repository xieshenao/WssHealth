package cn.wss.service.impl;

import cn.wss.dao.PermissionDao;
import cn.wss.dao.RoleDao;
import cn.wss.dao.UserDao;
import cn.wss.pojo.Permission;
import cn.wss.pojo.Role;
import cn.wss.pojo.User;
import cn.wss.service.UserService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;
    @Override
    public User findAllByUserName(String username) {
        //首先根据用户名查询用户信息
        User user = userDao.findUserByUserName(username);
        if(user == null){
            return null;
        }
        //根据用户id查询出角色信息
        Set<Role> roles = roleDao.findRoleByUserId(user.getId());
        //封装进用户信息中
        user.setRoles(roles);
        if(roles != null){
            for (Role role : roles) {
                //根据角色信息查询出权限信息
                Set<Permission> permission = permissionDao.findPermissionByRoleId(role.getId());
                //封装进角色信息中
                role.setPermissions(permission);
            }
        }
        //返回数据
        return user;
    }
}
