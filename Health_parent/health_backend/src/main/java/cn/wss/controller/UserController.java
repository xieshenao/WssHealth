package cn.wss.controller;

import cn.wss.constant.MessageConstant;
import cn.wss.entity.Result;
import cn.wss.pojo.UserNickname;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 1:返回用户访问错误信息
 * 2:实现回显昵称功能
 */
@RestController
@RequestMapping("/user")
public class UserController {

    //获取当前登录的用户名
    @RequestMapping("/getUserName")
    public Result getUsername(HttpServletRequest request){
        //因为用户登录，spring security完成认证后，会将用户信息存到框架提供的上下文中
        request.getHeader("AuthToken");
        //获取自定义的昵称实体类，里面也包含有用户信息
        UserNickname user = (UserNickname)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user != null){
            //如果框架中存储的信息不为空,就返回给前端页面用户名
            String username = user.getNickname();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,username);
        }
        return new Result(false, MessageConstant.GET_USERNAME_FAIL);
    }

    //返回用户错误信息
    @RequestMapping("/loginException")
    public String login(HttpServletRequest request, HttpServletResponse response){
        //获取登录异常信息(从框架的session中获取)
        HttpSession session = request.getSession();
        AuthenticationException spring_security_last_exception = (AuthenticationException)session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");

        //判断异常信息包含
        /**
         * 用户名不存在:UsernameNotFoundException;
         * 密码错误:BadCredentialException;
         * 帐户被锁:LockedException;
         * 帐户未启动:DisabledException;
         * 密码过期:CredentialExpiredException;
         */
        if(spring_security_last_exception instanceof BadCredentialsException){
            //密码错误
            return "密码输入错误";
        }else if(spring_security_last_exception instanceof UsernameNotFoundException){
            return "用户名不存在";
        }
        return spring_security_last_exception.getMessage();
    }
}
