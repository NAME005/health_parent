package cn.itjohnny.controller;

import cn.itjohnny.constant.MessageConstant;
import cn.itjohnny.constant.RedisMessageConstant;
import cn.itjohnny.entity.Result;
import cn.itjohnny.util.MyValidateCodeUtils;
import cn.itjohnny.util.SMSUtils;
import com.aliyuncs.exceptions.ClientException;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * 专门处理验证码请求的类
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;


    /**
     * 发送套餐预约验证码,并保存到redis
     * @param telephone
     * @return
     */
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone) {

        // 生成4位数验证码,这里用的是自己写的工具类
        String validateCode = MyValidateCodeUtils.getRandomCode(4);
        // 发送验证码
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode);
            jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER,3000,validateCode);
            return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }

    }

    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){
        // 生成6位数验证码,这里用的是自己写的工具类
        String validateCode = MyValidateCodeUtils.getRandomCode(6);
        // 发送验证码
        try {
            // 这里不发了
            System.out.println("validateCode = " + validateCode);
            //SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode);
            jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN,3000,validateCode);
            return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }

    }


}
