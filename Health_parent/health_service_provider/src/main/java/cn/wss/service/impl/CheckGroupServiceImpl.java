package cn.wss.service.impl;

import cn.wss.dao.CheckGroupDao;
import cn.wss.entity.PageResult;
import cn.wss.entity.QueryPageBean;
import cn.wss.pojo.CheckGroup;
import cn.wss.service.CheckGroupService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //分页优化
        PageInfo pageInfo = PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize()).doSelectPageInfo(()->{
            checkGroupDao.findAllBy(queryPageBean.getQueryString());
        });
        //把计算出的数据封装进pageResult对象中
        PageResult pageResult = new PageResult(pageInfo.getTotal(),pageInfo.getList());
        return pageResult;
    }

    /**
     * 添加
     * @param checkitemIds
     * @param checkGroup
     */
    @Override
    public void add(Integer[] checkitemIds, CheckGroup checkGroup) {
        //调方法
        checkGroupDao.addCheckGroup(checkGroup);
        //获取检查组id
        Integer id = checkGroup.getId();
        //把检查组id和检查项的id存到中间表中
        checkGroupDao.addCheckGroupCheckItem(id,checkitemIds);
    }

    /**
     * 根据检查组id查询对应的检查项id
     * @param id
     * @return
     */
    @Override
    public Integer[] findCheckItemIdsByCheckGroupId(Integer id) {
        Integer[] checkItemIds = checkGroupDao.findCheckItemIdsByCheckGroupId(id);
        return checkItemIds;
    }

    /**
     * 更新检查组
     * @param checkItemIds
     * @param checkGroup
     */
    @Override
    public void update(Integer checkItemIds[],CheckGroup checkGroup) {
        checkGroupDao.updateCheckGroup(checkGroup);

        /**
         * 我们用性能最好的一种方式来进行复选框的更新
         */
        //获取检查组id
        Integer checkGroupId = checkGroup.getId();
        //根据检查组id获取对应检查项的所有id(编辑前的，复选框还是原始的未动的状态)
        Integer[] checkItemIdsByCheckGroupId = checkGroupDao.findCheckItemIdsByCheckGroupId(checkGroupId);
        List<Integer> checkItemIdsByCheckGroupIds = new ArrayList<>(Arrays.asList(checkItemIdsByCheckGroupId));


        //将前端传过来的检查项数组转变集合(用户编辑后的检查项)
        List<Integer> checkitemIdEdit = new ArrayList<>(Arrays.asList(checkItemIds));
        //再复制一份
        List<Integer> tempList = new ArrayList<>(Arrays.asList(checkItemIds));

        //取消重复值，获取到需要新增的
        /**
         * 编辑后的：[1,2,3,4]
         * 编辑前的：[2,3,4,5]
         * 编辑后-编辑前 去掉重复：1（新增的）
         */
        checkitemIdEdit.removeAll(checkItemIdsByCheckGroupIds);

        /**
         * 编辑前的：[1,2,3,4]
         * 编辑后的：[2,3,4,5]
         * 编辑前-编辑后 去掉重复值：5(需要删除的)
         */
        checkItemIdsByCheckGroupIds.removeAll(tempList);

        if(checkitemIdEdit.size() > 0){
            //判断如果有数据，就调用添加方法，检查组id，检查项id（转换数组）
            checkGroupDao.addCheckGroupCheckItem(checkGroupId,
                    checkitemIdEdit.toArray(new Integer[checkitemIdEdit.size()]));
        }

        if(checkItemIdsByCheckGroupIds.size() > 0){
            checkGroupDao.batchDeleteCheckGroupAndCheckItem(checkGroupId,
                    checkItemIdsByCheckGroupIds.toArray(new Integer[checkItemIdsByCheckGroupIds.size()]));
        }

    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(Integer id) {
        //先删除中间表数据
        checkGroupDao.deleteCheckGroupAndCheckItem(id);
        //再删除检查组表数据
        checkGroupDao.deleteCheckGroup(id);
    }

    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }
}
