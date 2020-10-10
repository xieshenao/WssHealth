package com.aoshen.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


//密码加密

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-security.xml")
public class PasswordEncodingTest {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Test
    public void test(){
        String admin = passwordEncoder.encode("1234");
        System.out.println(admin);
    }
}
