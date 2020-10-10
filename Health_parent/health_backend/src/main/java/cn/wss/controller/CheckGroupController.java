package cn.wss.controller;

import cn.wss.constant.MessageConstant;
import cn.wss.entity.PageResult;
import cn.wss.entity.QueryPageBean;
import cn.wss.entity.Result;
import cn.wss.pojo.CheckGroup;
import cn.wss.service.CheckGroupService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/checkGroup")
public class CheckGroupController {
    @Reference
    private CheckGroupService checkGroupService;

    @RequestMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        try{
            //调用方法，把前端传过来的数据封装
            PageResult page = checkGroupService.findPage(queryPageBean);
            //判断是否有数据
            if(page.getTotal() < 0){
                return new Result(false, MessageConstant.QUERY_CHECKITEM_NULL);
            }
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,page);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    //添加
    @RequestMapping("/add")
    public Result add(Integer checkitemIds[],@RequestBody CheckGroup checkGroup){
        try{
            checkGroupService.add(checkitemIds,checkGroup);
        }catch (Exception e){
            return new Result(false,MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        //执行成功之后，没有异常，就返回给前端数据信号
        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    //根据检查组id查询检查项id
    @RequestMapping("/findCheckItemIdsByCheckGroupId")
    public Result findByCheckItemIds(Integer id){
        try{
            //调用方法查询中间表对应的检查项id
            Integer[] checkItemIds =checkGroupService.findCheckItemIdsByCheckGroupId(id);
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItemIds);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    //更新
    @RequestMapping("/update")
    public Result update(Integer checkitemIds[],@RequestBody CheckGroup checkGroup){
        try{
            checkGroupService.update(checkitemIds,checkGroup);
            return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }

    //删除
    @RequestMapping("/delete")
    public Result delete(Integer id){
        try{
            checkGroupService.delete(id);
            return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
    }

    //查询
    @RequestMapping("/findAll")
    public Result findAll(){
        try{
            List<CheckGroup> checkGroupList = checkGroupService.findAll();
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroupList);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }
}

