package cn.itjohnny.service;

import cn.itjohnny.entity.Result;
import cn.itjohnny.pojo.Order;
import com.alibaba.dubbo.config.annotation.Service;

import java.text.ParseException;
import java.util.Map;



public interface OrderService {

    /**
     * 提交预约套餐订单,包括各类校验
     * @param map
     * @return 返回Result,因为需要指定不同情况下的message,无法统一抛异常
     */
    Result submitOrder(Map map) throws Exception;


    /**
     * 根据订单id封装预约成功页面数据
     * @param id
     * @return
     */
    Map getSuccessPageByOrderId(Integer id);
}
