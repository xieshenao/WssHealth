package cn.wss.service.impl;

import cn.wss.constant.RedisConstant;
import cn.wss.dao.SetMealDao;
import cn.wss.entity.PageResult;
import cn.wss.entity.QueryPageBean;
import cn.wss.pojo.CheckGroup;
import cn.wss.pojo.CheckItem;
import cn.wss.pojo.SetMealNumber;
import cn.wss.pojo.Setmeal;
import cn.wss.service.SetMealService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.*;

@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {
    @Autowired
    private SetMealDao setMealDao;
    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    //注入普通属性：生成html的地址
    @Value("${out_put_path}")
    private String outPath;

    //构造方法之后执行
    @PostConstruct
    public void Next(){
        //生成套餐详情页面
        //先查询出套餐数据
        List<Setmeal> list = setMealDao.findAll();
        //生成套餐列表静态页面(不加这个就不会生成列表页面)(防止第一次执行，用户没有添加的时候没有生成套餐列表页面)
        greateSetMealList(list);
        //遍历集合
        for (Setmeal setmeal : list) {
            //就是说有多少个套餐详情页面就生成多少个
            greateSetMealDatails(setmeal.getId());
        }
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //分页查询
        PageInfo pageInfo = PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize()).doSelectPageInfo(()->{
            setMealDao.findSetMealAll(queryPageBean.getQueryString());
        });
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        setMealDao.addSetMeal(setmeal);
        //得到套餐id
        Integer id = setmeal.getId();
        //判断是否为空
        if(checkgroupIds != null && checkgroupIds.length > 0){
            //加入到中间表中
            setMealDao.addSetMealAndCheckGroup(id,checkgroupIds);
        }

        //将添加的套餐图片保存到redis
        //jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
        //删除hash中存到数据库中的图片（剩下的都是七牛云中未存到数据库中的图片）
        jedisPool.getResource().hdel("fileName_hash",setmeal.getImg());
        //当用户添加了套餐信息之后，需要生成新的套餐信息（根据生成的id添加要生成的静态页面）
        greateSetMealAll(id);
    }


    //生成该id下所有的静态页面(套餐列表、套餐详情)
    public void greateSetMealAll(Integer id){
        //生成套餐列表页面
        greateSetMealList();
        //生成套餐详情页面
        greateSetMealDatails(id);
    }

    //套餐列表
    public void greateSetMealList(){
        //首先查询套餐的所有数据
        List<Setmeal> list = setMealDao.findAll();
        //调用方法重载生成页面
        this.greateSetMealList(list);
    }
    //套餐列表的有参构造方法(最终生成页面)
    public void greateSetMealList(List<Setmeal> list){
        //创建map集合封装
        Map map = new HashMap();
        map.put("setmealList",list);
        //创建页面
        greateHtml("setmeal_list.ftl","m_setmeal.html",map);
    }
    //生成套餐详情页面
    public void greateSetMealDatails(Integer id){
        //首先查询出该套餐id下的所有检查组和检查项的数据
        Setmeal setmeal = findById(id);
        //创建Map封装数据
        Map map = new HashMap();
        map.put("setmeal",setmeal);
        greateHtml("setmeal_detail.ftl","setmeal_detail_"+id+".html",map);
    }

    //通用的方法，用于生成静态页面 1.模板文件名 2.生成HTML页面名称 3.数据
    public void greateHtml(String templateName, String htmlPageName, Map map) {
        Configuration configuration = freeMarkerConfigurer.getConfiguration();//获得配置对象
        Writer out = null;
        try {
            Template template = configuration.getTemplate(templateName);
            //构造输出流
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outPath  + htmlPageName)),"utf-8"));
            //out = new FileWriter(outPutPath  + htmlPageName);
            //输出文件
            template.process(map, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Integer[] findByCheckGroupId(Integer id) {
        return setMealDao.findByCheckGroupId(id);
    }

    //编辑
    @Override
    public void update(Integer[] checkGroupIds, Setmeal setmeal) {
        //更新套餐表
        setMealDao.updateSetMeal(setmeal);
        //获取套餐id
        Integer id = setmeal.getId();
        //获取所有的检查组id
        Integer[] checkGroupId = setMealDao.findByCheckGroupId(id);
        //转成集合(编辑前id)
        List<Integer> integers = new ArrayList<>(Arrays.asList(checkGroupId));

        //创建编辑后集合
        List<Integer> checkitemIdEdit = new ArrayList<>(Arrays.asList(checkGroupIds));
        List<Integer> templateList = new ArrayList<>(Arrays.asList(checkGroupIds));

        //获取新增的id
        checkitemIdEdit.removeAll(integers);

        //获取需要删除的id
        integers.removeAll(templateList);

        if(checkitemIdEdit.size() > 0){
            //就执行添加
            setMealDao.addSetMealAndCheckGroup(id,checkitemIdEdit.toArray(new Integer[checkitemIdEdit.size()]));
        }
        if(integers.size() > 0){
            //就执行删除
            setMealDao.deleteSetMealAndCheckGroup(id,integers.toArray(new Integer[integers.size()]));
        }

    }

    @Override
    public void delete(Integer id) {
        if(id != null){
            //先删掉套餐数据
            setMealDao.delete(id);
            //再删除检查项id

            //删除套餐的时候，删掉该id下的静态页面(套餐列表+套餐详情)

        }

    }

    @Override
    public List<Setmeal> findAll() {
        return setMealDao.findAll();
    }

    @Override
    public Setmeal findById(Integer id) {
        //根据id查询出一条套餐数据
        Setmeal setmeal = setMealDao.findSetMealById(id);
        //根据套餐id查询出检查组数据(多条,用List封装)
        List<CheckGroup> checkGroups = setMealDao.findCheckGroupBySetMealId(setmeal.getId());
        //对检查组进行封装
        setmeal.setCheckGroups(checkGroups);
        //遍历检查组
        for (CheckGroup checkGroup : checkGroups) {
            //对每条查询出的而检查项数据进行封装
            List<CheckItem> checkItems = setMealDao.findCheckItemByCheckGroup(checkGroup.getId());
            checkGroup.setCheckItems(checkItems);
        }
        return setmeal;
    }

    @Override
    public List<SetMealNumber> findSetMealCount() {
        return setMealDao.findNameValue();
    }
}
