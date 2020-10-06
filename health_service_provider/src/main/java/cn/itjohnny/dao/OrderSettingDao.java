package cn.itjohnny.dao;

import cn.itjohnny.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {


    /**
     * 添加OrderSetting
     * @param orderSetting
     * @return
     */
    boolean add(OrderSetting orderSetting);


    /**
     * 查询日期是否已经存在
     * @param orderDate
     * @return
     */
    long countByOrderDate(Date orderDate);


    /**
     * 更新
     * @param orderSetting
     * @return
     */
    boolean updateByOrderDate(OrderSetting orderSetting);


    /**
     * 根据月份查询
     * @param
     * @param
     * @return
     */
    List<OrderSetting> findOrderSettingsByMonth(@Param("start") String start,@Param("end") String end);
//    List<OrderSetting> findOrderSettingsByMonth(Map map);

    /**
     * 查找该日期是否约满
     * @param orderDate
     * @return
     */
    OrderSetting findFullOrderSettingByDate(Date orderDate);


    /**
     * 根据日期查询
     * @return
     */
    OrderSetting findByDate(Date orderDate);












}
