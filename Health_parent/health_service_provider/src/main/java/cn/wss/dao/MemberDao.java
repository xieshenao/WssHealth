package cn.wss.dao;

import cn.wss.pojo.Member;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface MemberDao {
    //根据电话查询会员表信息
    public Member findByTelephone(@Param("telephone") String telephone);
    //增加会员信息
    public void addMember(Member member);
    //根据日期查询会员数量
    public Integer findCountByregTime(@Param("date") String date);
    //获取今日增加的会员数
    public Integer findByThisToday(@Param("reportDate") String reportDate);
    //获取本周新增会员
    public Integer findByThisWeek(@Param("thisWeekMonday") String thisWeekMonday);
    //获取本月新增会员
    public Integer findByThisMonth(@Param("firstDay4ThisMonth") String firstDay4ThisMonth);
    //总会员数
    public Integer findCount();


}

