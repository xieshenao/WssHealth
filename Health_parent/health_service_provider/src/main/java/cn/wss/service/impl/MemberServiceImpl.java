package cn.wss.service.impl;

import cn.wss.dao.MemberDao;
import cn.wss.pojo.Member;
import cn.wss.service.MemberService;
import cn.wss.utils.DateUtils;
import cn.wss.utils.MD5Utils;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;

    @Override
    public Member findMemberByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    /**
     * 添加会员信息
     * @param member
     */
    @Override
    public void addMember(Member member) {
        String password = member.getPassword();
        if(password != null){
            //使用md5将明文密码进行加密
            password = MD5Utils.md5(password);
            member.setPassword(password);
        }
        memberDao.addMember(member);
    }

    /**
     * 根据日期查询套餐数量
     * @param months
     * @return
     */
    @Override
    public List<Integer> findCountByRegTime(List<String> months) {
        try{
            //创建集合
            List<Integer>list = new ArrayList<>();
            if(months != null){
                //遍历
                for (String month : months) {//month:2019.08
                    //获取到下一个月,后面拼接字符串,变成下月份的第一天
                    String date1 = month + ".01";//2019.08.01
                    Integer count = memberDao.findCountByregTime(date1);
                    list.add(count);
                }
            }else{
                return null;
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("sdfd");
        }
    }


}
