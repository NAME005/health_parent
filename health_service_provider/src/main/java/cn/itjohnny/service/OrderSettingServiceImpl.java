package cn.itjohnny.service;

import cn.itjohnny.dao.OrderSettingDao;
import cn.itjohnny.pojo.OrderSetting;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public boolean add(List<OrderSetting> orderSettings) {

        if (orderSettings == null || orderSettings.size() == 0){
            return true;
        }

        for (OrderSetting orderSetting : orderSettings) {
            // 先判断是否存在
            long count = orderSettingDao.countByOrderDate(orderSetting.getOrderDate());
            if (count > 0){
                // 存在则更新
                orderSettingDao.updateByOrderDate(orderSetting);
            }else {
                // 不存在则添加
                orderSettingDao.add(orderSetting);
            }
        }


        return true;
    }

    // 根据月份查询
    @Override
    public List<Map> findOrderSettings(String date) {

        String start = date + "-1";
        String end = date + "-31";

       /* HashMap hashMap = new HashMap();
        hashMap.put("start",start);
        hashMap.put("end",end);*/

        List<OrderSetting> orderSettingList = orderSettingDao.findOrderSettingsByMonth(start,end);

        ArrayList<Map> maps = new ArrayList<>();

        for (OrderSetting orderSetting : orderSettingList) {

            HashMap<String, Object> map = new HashMap<>();
            map.put("date",orderSetting.getOrderDate().getDate());
            map.put("number",orderSetting.getNumber());
            map.put("reservations",orderSetting.getReservations());

            maps.add(map);

        }

        return maps;
    }

    @Override
    public boolean editOrderSettingNumber(String date, String number) {

        // 忘记用SettingService接收了,所以转换一下
        date = date.replaceAll("-", "/");

        Date orderDate = new Date(date);
        // 先判断
        long count = orderSettingDao.countByOrderDate(orderDate);

        if (count > 0){
            // 存在则更新
            orderSettingDao.updateByOrderDate(new OrderSetting(orderDate,Integer.parseInt(number)));
        }else {
            // 不存在则添加
            orderSettingDao.add(new OrderSetting(orderDate,Integer.parseInt(number)));
        }

        return true;
    }
}
