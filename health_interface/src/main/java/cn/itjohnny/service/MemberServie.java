package cn.itjohnny.service;

import cn.itjohnny.pojo.Member;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface MemberServie {

    /**
     * 会员通过手机号验证码登录
     * @param telephone
     */
    Member loginBytelephone(String telephone);

    /**
     * 通用的会员注册方法
     * @param member
     * @return
     */
    Member register(Member member);


    /**
     * 查询每个月的会员数量,有序
     * @param dates
     * @return
     */
    List<Integer> countNumberByMonths(List<String> dates) throws Exception;


}
