package cn.wss.service.impl;

import cn.wss.dao.CheckItemDao;
import cn.wss.entity.PageResult;
import cn.wss.entity.QueryPageBean;
import cn.wss.entity.Result;
import cn.wss.pojo.CheckItem;
import cn.wss.service.CheckItemService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//如果要加事务管控，就必须在service加上
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    //注入Dao对象
    @Autowired
    private CheckItemDao checkItemDao;
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    @Override
    public List<CheckItem> findCheckItemCodeAndName(String code, String name) {
        return checkItemDao.findCheckItemCodeAndName(code,name);
    }

    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        /*//设置分页的当前页和一页要显示多少数据
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //查询出一页的数据
        List<CheckItem> itemList = checkItemDao.selectByCondition(queryPageBean.getQueryString());
        //封装
        PageInfo<CheckItem> pageInfo = new PageInfo<>(itemList);
        //计算总数量
        long total = pageInfo.getTotal();*/

        //分页优化(安全)
        PageInfo pageInfo = PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize())
                .doSelectPageInfo(() -> checkItemDao.selectByCondition(queryPageBean.getQueryString()));
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    public Long update(CheckItem checkItem) {
        return checkItemDao.update(checkItem);
    }

    @Override
    public void delete(Integer id) {
        //不能直接删除，因为要牵连到中间表
        //先判断中间表中是否有数据
        long countByCheckItemId = checkItemDao.findCountByCheckItemId(id);
        if(countByCheckItemId > 0){
            //就传给后端一个信息
            throw new  RuntimeException("id被占用，请换一个");
        }
        checkItemDao.delete(id);
    }

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }

}
