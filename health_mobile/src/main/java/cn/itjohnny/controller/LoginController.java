package cn.itjohnny.controller;

import cn.itjohnny.constant.MessageConstant;
import cn.itjohnny.constant.RedisMessageConstant;
import cn.itjohnny.entity.Result;
import cn.itjohnny.pojo.Member;
import cn.itjohnny.service.MemberServie;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Johnny
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberServie memberServie;


    @RequestMapping("/check")
    public Result login(@RequestBody Map loginInfo, HttpServletResponse response){
        String telephone = (String) loginInfo.get("telephone");
        String validateCode = (String) loginInfo.get("validateCode");
        String validateCodeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        // 校验验证码
        if (validateCode == null || validateCodeInRedis == null || !validateCode.equals(validateCodeInRedis)){
            return new Result(false,MessageConstant.VALIDATECODE_ERROR);
        }


        try {
            Member member = memberServie.loginBytelephone(telephone);
            // 登录成功添加cookie
            Cookie cookie = new Cookie("login_member_telephone", telephone);
            cookie.setMaxAge(60 * 60 * 24 * 30); // 存30天
            cookie.setPath("/");  // 所有路径接收
            response.addCookie(cookie);

            // json序列化会员信息到redis
            String jsonMember = JSON.toJSONString(member);
            jedisPool.getResource().setex(telephone,60 * 30 , jsonMember); // redis中存30分钟

            // 返回登录结果
            return new Result(true,MessageConstant.LOGIN_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.LOGIN_FAIL);
        }

    }






}
