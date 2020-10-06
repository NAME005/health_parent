package cn.itjohnny.service;

import cn.itjohnny.pojo.OrderSetting;
import org.apache.poi.hssf.record.BOFRecord;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {

    /**
     * 批量添加OrderSettings
     * @param orderSettings
     * @return
     */
    boolean add(List<OrderSetting> orderSettings);

    /**
     * 根据月份查询
     * @param date
     * @return
     */
    List<Map> findOrderSettings(String date);

    /**
     * 编辑可预约人数
     * @param date
     * @param number
     * @return
     */
    boolean editOrderSettingNumber(String date, String number);
}
