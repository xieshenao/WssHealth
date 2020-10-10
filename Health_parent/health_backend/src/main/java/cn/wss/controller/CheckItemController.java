package cn.wss.controller;

import cn.wss.constant.MessageConstant;
import cn.wss.entity.PageResult;
import cn.wss.entity.QueryPageBean;
import cn.wss.entity.Result;
import cn.wss.pojo.CheckItem;
import cn.wss.service.CheckItemService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/checkitem")
public class CheckItemController {

    @Reference
    private CheckItemService checkItemService;

    //添加
    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem){
        String code = checkItem.getCode();
        String name = checkItem.getName();
        try{
            List<CheckItem> checkItemList = checkItemService.findCheckItemCodeAndName(code, name);
            //判断是否为空
            if(checkItemList == null || checkItemList.size() == 0){
                //如果为空证明数据库没有值,就进行添加
                checkItemService.add(checkItem);
            }else{
                //如果不为空就证明有重复的值
                return new Result(false, MessageConstant.ADD_CHECKITEM_REPITITION);
            }
        }catch (Exception e){
            //打印错误信息
            e.printStackTrace();
            //如果失败了，就返回前段错误信息
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        //添加成功，返回前端正确信息
        return new Result(true,MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    //查询
    @RequestMapping("/findPage")
    public Result fidPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult;
        try{
            pageResult = checkItemService.pageQuery(queryPageBean);
            if(pageResult.getTotal() == 0){
                //判断查询的数据是否存在，不存在就提示用户错误信息
                return new Result(false,MessageConstant.QUERY_CHECKITEM_NULL);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,pageResult);
    }

    //更新
    @RequestMapping("/update")
    public Result update(@RequestBody CheckItem checkItem){
        try{
            Long i = checkItemService.update(checkItem);
            if(i > 0){
                return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
            }else{
                return new Result(true,MessageConstant.EDIT_CHECKITEM_FAIL);
            }
        }catch (Exception e){
            System.out.println(e);
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }
    }

    //删除
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")//赋予权限（必须拥有删除权限才能删除）
    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            checkItemService.delete(id);
            return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            //有异常或者服务调用失败
            return new Result(true, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
    }

    //查询
    @RequestMapping("/findAll")
    public Result findAll(){
        try{
           List<CheckItem> checkItemList = checkItemService.findAll();
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItemList);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

}
