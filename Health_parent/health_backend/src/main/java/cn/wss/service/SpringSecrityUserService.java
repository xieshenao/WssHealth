package cn.wss.service;

import cn.wss.exception.MyUserNameNotFoundException;
import cn.wss.pojo.Permission;
import cn.wss.pojo.Role;
import cn.wss.pojo.User;
import cn.wss.pojo.UserNickname;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 获取用户信息：用户名、密码、角色和访问权限
 */
@Component
public class SpringSecrityUserService implements UserDetailsService {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名获取用户信息（role、user、permission）
        User user = userService.findAllByUserName(username);
        if(user == null){
            //为空，说明用户不存在
            throw new MyUserNameNotFoundException("不好意思，用户名找不到欸~~");
        }

        //创建集合，存储用户信息和权限信息
        List<GrantedAuthority>list = new ArrayList<>();
        //获取角色信息
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            //赋予角色信息
            list.add(new SimpleGrantedAuthority(role.getKeyword()));
            //获取权限信息
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                //赋予权限信息
                list.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }
        //把用户信息存到我们自定义的昵称实体类中
        UserNickname user1 = new UserNickname(username,user.getPassword(),list);
        //设置昵称
        user1.setId(user.getId());
        user1.setNickname("欢迎"+user1.getUsername());
        //把存储的数据交给security
        return user1;
    }
}
