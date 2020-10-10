package cn.wss.controller;

import cn.wss.constant.MessageConstant;
import cn.wss.entity.Result;
import cn.wss.pojo.OrderSetting;
import cn.wss.service.OrderSettingService;
import cn.wss.utils.POIUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;
    @RequestMapping("/upload")
    public Result upload(MultipartFile excelFile){
        try {
            //使用工具类解析提交的表格
            List<String[]> readExcel = POIUtils.readExcel(excelFile);
            //创建集合用于封装属性
            List<OrderSetting>orderSettings = new ArrayList<>();
            //遍历解析后的集合
            for (String[] strings : readExcel) {
                //获取日期
                String orderDate = strings[0];
                //获取预约数量
                String number = strings[1];
                //封装数据
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                OrderSetting orderSetting = new OrderSetting(format.parse(orderDate), Integer.parseInt(number));
                //封装到集合中
                orderSettings.add(orderSetting);
            }
            //批量导入到数据库中（不过要在service判断是否 是存储还是更新）
            orderSettingService.add(orderSettings);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            //文件解析失败
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    @RequestMapping("/getOrderSettingByMonth")
    public Result getMonth(String date){
        try{
            List<Map> orderSettingByMonth = orderSettingService.getOrderSettingByMonth(date);

            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,orderSettingByMonth);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }

    @RequestMapping("/updatePersonNumber")
    public Result update(@RequestBody OrderSetting orderSetting){
        try{
            orderSettingService.update(orderSetting);
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }

    }
}
