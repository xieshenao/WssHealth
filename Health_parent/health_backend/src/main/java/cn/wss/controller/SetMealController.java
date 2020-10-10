package cn.wss.controller;

import cn.wss.constant.MessageConstant;
import cn.wss.constant.RedisConstant;
import cn.wss.entity.PageResult;
import cn.wss.entity.QueryPageBean;
import cn.wss.entity.Result;
import cn.wss.pojo.Setmeal;
import cn.wss.service.SetMealService;
import cn.wss.utils.QiniuUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetMealController {

    //注入JedisPoll操作Redis服务
    @Autowired
    private JedisPool jedisPool;

    //文件上传
    @RequestMapping("/upload")
    public Result upload(MultipartFile imgFile){
        //得到文件的源文件名
        String originalFilename = imgFile.getOriginalFilename();
        //截取字符串，得到.img
        int i = originalFilename.lastIndexOf(".");
        String substring = originalFilename.substring(i);//.jpg
        //使用UUID生成随机字符串进行拼接
        String fileName = UUID.randomUUID().toString() + substring;//usafafbadfvasjkfbasjkfjs.jpg(随机字符串拼接成的文件名)
        try{
            //将文件名上传到七牛云
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);

            //将传到七牛云中的图片保存到redis
            //jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
            //将七牛云中的图片存到一个集合

            /**
             * key：存的集合名称
             * fileName:文件名
             * 存入这个文件时的毫秒值
             * 也存到了redis里面
             */
            jedisPool.getResource().hset("fileName_hash",fileName,String.valueOf(System.currentTimeMillis()));

            return new Result(true,MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    @Reference
    private SetMealService setMealService;

    @RequestMapping("/findPage")
    public Result findpage(@RequestBody QueryPageBean queryPageBean){
        try{
            PageResult page = setMealService.findPage(queryPageBean);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,page);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
    //添加
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){
        //获取套餐id
        Integer setmealId = setmeal.getId();
        try{
            setMealService.add(setmeal,checkgroupIds);
            return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
    }


    //根据id查询中间表
    @RequestMapping("/findByCheckGroupId")
    public Result findByCheckGroupId(Integer id){
        try{
            Integer[] checkGroupIds =setMealService.findByCheckGroupId(id);
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroupIds);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    //更新
    @RequestMapping("/update")
    public Result update(Integer[] checkgroupIds,@RequestBody Setmeal setmeal){
        try{
            setMealService.update(checkgroupIds,setmeal);
            return new Result(true,MessageConstant.EDIT_SETMEAL_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_SETMEAL_FAIL);
        }

    }
    //删除
    @RequestMapping("/delete")
    public Result delete(Integer id){
        try{
            setMealService.delete(id);
            return new Result(true,"删除套餐成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"删除套餐失败");
        }
    }
}
