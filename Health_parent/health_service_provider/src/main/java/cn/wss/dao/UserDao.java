package cn.wss.dao;

import cn.wss.pojo.User;

public interface UserDao {
    //根据名称查询
    public User findUserByUserName(String username);
}
