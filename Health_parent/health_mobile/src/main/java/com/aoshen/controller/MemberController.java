package com.aoshen.controller;

import cn.wss.constant.MessageConstant;
import cn.wss.constant.RedisMessageConstant;
import cn.wss.entity.Result;
import cn.wss.pojo.Member;
import cn.wss.service.MemberService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

//快速登录
@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private MemberService memberService;

    @RequestMapping("/login")
    public Result login(HttpServletResponse response, @RequestBody Map map){
        //先取出手机号和验证码
        String telephone = (String)map.get("telephone");
        String validateCode = (String)map.get("validateCode");
        //根据手机号取出redis中存储生成的验证码
        String code = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        if(validateCode != null && validateCode.equals(code)){
            //判断如果验证码一致，判断是否是会员
            Member member = memberService.findMemberByTelephone(telephone);
            if(member == null){
                //不是会员，自动注册
                member = new Member();
                member.setPhoneNumber(telephone);
                member.setRegTime(new Date());
                memberService.addMember(member);
            }
            //是会员的，就存档cookie信息，快速登录
            Cookie cookie = new Cookie("loginTelephone",telephone);
            //设置信息
            cookie.setMaxAge(60*60*24);//生效时间：一天
            cookie.setPath("/");
            //存到session中
            response.addCookie(cookie);
            //将会员信息保存到redis
            String json = JSON.toJSON(member).toString();//将member对象转成json再转成字符串
            jedisPool.getResource().setex(telephone,60*30,json);
            //登录成功
            return new Result(true, MessageConstant.LOGIN_SUCCESS);
        }else{
            //验证码输入错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

    }
}
