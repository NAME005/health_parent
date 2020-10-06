package cn.itjohnny.controller;

import cn.itjohnny.constant.MessageConstant;
import cn.itjohnny.constant.RedisMessageConstant;
import cn.itjohnny.entity.Result;
import cn.itjohnny.pojo.Order;
import cn.itjohnny.service.OrderService;
import cn.itjohnny.util.SMSUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;


    /**
     * 处理手机提交套餐预约请求
     * @param map
     * @return
     */
    @RequestMapping("/submit")
    public Result orderSubmit(@RequestBody Map map){

        try {
            // 校验验证码
            String telephone = (String) map.get("telephone");
            String validateCode = (String) map.get("validateCode");
            String validateCodeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
            if (validateCode == null || validateCodeInRedis == null || !validateCode.equals(validateCodeInRedis)){
                return new Result(false,MessageConstant.VALIDATECODE_ERROR);
            }

            // 验证码校验通过后提交订单
            // 专门写个OrderService处理
            Result result = orderService.submitOrder(map);
            // 若保存成功,发送短信通知  // 但是没发送成功
            if (result.isFlag()){
                String orderDate = (String) map.get("orderDate");
                String param = orderDate;
                try {// 这里单独包裹
                    SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone,param);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return result;

        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDER_FULL);
        }

    }


    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Map successPageMap = orderService.getSuccessPageByOrderId(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,successPageMap);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }


















}
