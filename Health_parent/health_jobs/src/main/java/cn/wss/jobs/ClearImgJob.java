package cn.wss.jobs;

import cn.wss.constant.RedisConstant;
import cn.wss.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClearImgJob {
    @Autowired
    private JedisPool jedisPool;
    public void clearImg(){
        //取出redis中存的数据库图片和七牛云图片并进行插值计算
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);

        /**
         * 这个set集合中就存储了多余的图片
         * 先进性判断，后删除
         */
        if(set != null){
            for (String picName : set) {
                /**
                 * 判断这个垃圾图片上传的时间是否超过一天，如果超过一天就删掉
                 */
                //获取hash中存的所有七牛云图片
                Map<String, String> fileName_hash = jedisPool.getResource().hgetAll("fileName_hash");
                //创建集合，需要删除的图片都存进去
                List<String> list = new ArrayList<>();
                //计算当前时间
                long l = System.currentTimeMillis();
                //遍历hash
                for (String filename : fileName_hash.keySet()) {
                    //获取上传图片的时间
                    String time = fileName_hash.get(filename);
                    long l1 = Long.parseLong(time);
                    //判断如果上传时间超过一天，就存到删除集合中
                    if((l - l1) >= 1000L * 60L * 60L * 24L){
                        list.add(filename);
                    }
                }

                //遍历删除集合
                for (String s : list) {
                    //删除七牛云上的图片
                    QiniuUtils.deleteFileFromQiniu(s);
                    //删除redis存储的多余的图片
                    jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,s);
                }


            }
        }


    }
}
