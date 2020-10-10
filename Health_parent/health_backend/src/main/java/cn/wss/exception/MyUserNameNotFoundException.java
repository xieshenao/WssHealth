package cn.wss.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 自动逸用户名找不到异常
 */
public class MyUserNameNotFoundException extends AuthenticationException {
    //设置序列化版本号
    private static final long serialVersionUID = 1L;
    public MyUserNameNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public MyUserNameNotFoundException(String msg) {
        super(msg);
    }
}
