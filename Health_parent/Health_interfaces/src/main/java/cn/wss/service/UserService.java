package cn.wss.service;

import cn.wss.pojo.User;

public interface UserService {
    public User findAllByUserName(String username);
}
