package cn.itjohnny.service;

import cn.itjohnny.dao.MemberDao;
import cn.itjohnny.pojo.Member;
import cn.itjohnny.util.MD5Utils;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service(interfaceClass = MemberServie.class)
@Transactional
public class MemberServiceImpl implements MemberServie {

    @Autowired
    private MemberDao memberDao;


    @Override
    public Member loginBytelephone(String telephone) {

        // 根据电话号码查询会员
        Member member = memberDao.findByTelephone(telephone);
        // 不是会员,注册
        if (member == null){
            member = new Member();
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            // 重新给member赋值因为注册前没id,以防后面用到
            member = register(member);
        }
        // 是会员,直接返回

        return member;
    }

    @Override
    public Member register(Member member) {
        // 若不是通过手机号注册,有密码,先加密
        if (member.getPassword() != null){
            member.setPassword(MD5Utils.md5(member.getPassword()));
        }
        // 添加会员
        memberDao.add(member);

        return member;
    }

    @Override
    public List<Integer> countNumberByMonths(List<String> dates) throws Exception {
        // 创建list装每月人数
        ArrayList<Integer> memberCounts = new ArrayList<>();
        for (String date : dates) {
//            date = new SimpleDateFormat("yyyy年MM月dd").parse(date + "31").toString();
            date = date.replace("年","/").replace("月","/") + "31";
            memberCounts.add(memberDao.findMemberCountBeforeDate(date));

        }


        return memberCounts;
    }
}
