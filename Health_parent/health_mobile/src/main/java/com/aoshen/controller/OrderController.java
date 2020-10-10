package com.aoshen.controller;

import cn.wss.constant.MessageConstant;
import cn.wss.constant.RedisMessageConstant;
import cn.wss.entity.Result;
import cn.wss.pojo.Order;
import cn.wss.service.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JedisPool jedisPool;
    @Reference
    private OrderService orderService;
    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map){
        String telephone = (String)map.get("telephone");
        //从redis中获取根据手机号存储的验证码
        String RedisValidateCode = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        //获取用户输入的验证码
        String validateCode = (String)map.get("validateCode");
        //1:首先判断用户输入的验证码是否正确
        if(validateCode != null && validateCode.equals(RedisValidateCode)){
            //如果用户输入的验证码和redis中的验证码一致,调用业务层进行业务处理
            //设置预约类型
            map.put("orderType",Order.ORDERTYPE_WEIXIN);
            //不管有没有异常，都返回给前端一个信息，保证传给前端的数据不为空
            Result result = new Result(false, MessageConstant.VALIDATECODE_ERROR);
            try{
                //防止出现异常，进行异常抓取
                //调用业务层方法
                result = orderService.order(map);

            }catch(Exception e){
                e.printStackTrace();
                //出现异常就返回异常处理结果
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);

            }

            //如果预约成功
            if(result.isFlag()){
                //返回给前端一个信号
                return new Result(true,MessageConstant.ORDER_SUCCESS,result);
            }
            //返回前端数据
            return result;
        }else{
            //如果验证码不匹配,返回错误信息
            return new Result(false,MessageConstant.VALIDATECODE_ERROR);
        }
    }

    @RequestMapping("/findById")
    public Result findById(Integer id){
        try{
            Map map = orderService.findById(id);
            return new Result(true,"预约成功",map);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"预约失败");
        }
    }
}
