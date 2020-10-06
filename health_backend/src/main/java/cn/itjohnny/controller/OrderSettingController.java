package cn.itjohnny.controller;

import cn.itjohnny.constant.MessageConstant;
import cn.itjohnny.entity.Result;
import cn.itjohnny.pojo.OrderSetting;
import cn.itjohnny.service.OrderSettingService;
import cn.itjohnny.util.POIUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    /**
     * 模板表格上传
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile file){

        try {
            List<String[]> strings = POIUtils.readExcel(file);

            // 创建一个List用来装OrderSetting
            List<OrderSetting> orderSettings = new ArrayList<>();
            // 遍历添加
            for (String[] string : strings) {
                Date date = new Date(string[0]);
                int num = Integer.parseInt(string[1]);
                orderSettings.add(new OrderSetting(date,num));
            }
            // 批量添加
            orderSettingService.add(orderSettings);

            return new Result(true,MessageConstant.IMPORT_ORDERSETTING_SUCCESS);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }

    }


    /**
     * 根据月份查询预约设置状态
     * @param date
     * @return
     */
    @RequestMapping("/findOrderSettingByMonth")
    public Result findOrderSettingByMonth(String date){

        try {
            List<Map> orderSettingList = orderSettingService.findOrderSettings(date);
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,orderSettingList);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }

    }


    /**
     * 编辑可预约人数
     * @param date
     * @param number
     * @return
     */
    @RequestMapping("/editOrderSettingNumber")
    // 忘记用SettingService接收了
    public Result editOrderSettingNumber(String date, String number){

        try {
            orderSettingService.editOrderSettingNumber(date,number);
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }

    }











}
