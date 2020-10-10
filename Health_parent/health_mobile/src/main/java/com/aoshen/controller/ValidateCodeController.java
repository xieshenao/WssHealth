package com.aoshen.controller;

import cn.wss.constant.MessageConstant;
import cn.wss.constant.RedisMessageConstant;
import cn.wss.entity.Result;
import cn.wss.utils.SMSUtils;
import cn.wss.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * 验证码校验
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/send4Code")
    public Result sendCode(String telephone){
        //设置生成的验证码是用来预约的
        String key = telephone + RedisMessageConstant.SENDTYPE_ORDER;
        //随机生成4为数字
        String validateCode = ValidateCodeUtils.generateValidateCode(4).toString();
        return sendCode(key,telephone,validateCode);
    }

    @RequestMapping("/send6Code")
    public Result send6Code(String telephone){
        //设置手机号标识，用来存储redis中
        String key = telephone + RedisMessageConstant.SENDTYPE_LOGIN;
        //生成6为数字验证码
        String validateCode = ValidateCodeUtils.generateValidateCode(6).toString();
        return sendCode(key,telephone,validateCode);
    }


    //发送验证码方法
    public Result sendCode(String key,String telephone,String validateCode){
        //计算出剩余存储的时间
        long ttl = jedisPool.getResource().ttl(key);
        if(ttl >= 240){
            //如果存储时间大于240秒，一分钟内不能点击（存储时间为300秒）
            return new Result(false,"一分钟内不能点击");
        }
        try{
            //发送短信
            //SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode);
            //存到redis中
            jedisPool.getResource().setex(key,300,validateCode);
            return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS,validateCode);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }
}
