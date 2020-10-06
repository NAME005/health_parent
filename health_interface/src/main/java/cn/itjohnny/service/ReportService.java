package cn.itjohnny.service;


import java.util.Map;

/**
 * 用来处理无法归类的report类数据查询封装处理
 * @author Johnny
 *
 */

public interface ReportService {


    /**
     * 封装运营数据
     * @return
     */
    Map<String,Object> getBusinessReportData() throws Exception;

}
