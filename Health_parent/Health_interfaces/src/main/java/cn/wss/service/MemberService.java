package cn.wss.service;

import cn.wss.pojo.Member;

import java.util.List;

public interface MemberService {
    //根据手机号查询会员信息
    public Member findMemberByTelephone(String telephone);
    //添加会员信息
    public void addMember(Member member);
    //根据日期查询会员数量
    public List<Integer> findCountByRegTime(List<String>months);
}
