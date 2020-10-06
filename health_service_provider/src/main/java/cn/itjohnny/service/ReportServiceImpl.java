package cn.itjohnny.service;

import cn.itjohnny.dao.MemberDao;
import cn.itjohnny.dao.OrderDao;
import cn.itjohnny.pojo.Member;
import cn.itjohnny.util.DateUtils;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;


    @Override
    public Map<String, Object> getBusinessReportData() throws Exception {
        HashMap<String, Object> resultMap = new HashMap<>();
        // 封装日期
        String reportDate = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        resultMap.put("reportDate",reportDate);
        // 今日新增会员
        Integer todayNewMember = memberDao.findMemberCountByDate(reportDate);
        resultMap.put("todayNewMember",todayNewMember);
        // 总会员数
        Integer totalMember = memberDao.findMemberCountBeforeDate(reportDate);
        resultMap.put("totalMember",totalMember);
        // 本周新增会员
        Integer thisWeekNewMember = memberDao.findMemberCountAfterDate(DateUtils.parseDate2String(DateUtils.getThisWeekMonday()));
        resultMap.put("thisWeekNewMember",thisWeekNewMember);
        //本月新增会员
        Integer thisMonthNewMember = memberDao.findMemberCountAfterDate(DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth()));
        resultMap.put("thisMonthNewMember",thisMonthNewMember);
        // 今日预约数
        Integer todayOrderNumber = orderDao.findOrderCountByDate(reportDate);
        resultMap.put("todayOrderNumber",todayOrderNumber);
        // 今日到诊数
        Integer todayVisitsNumber = orderDao.findVisitsCountByDate(reportDate);
        resultMap.put("todayVisitsNumber",todayVisitsNumber);
        // 本周预约数量
        Integer thisWeekOrderNumber = orderDao.findOrderCountAfterDate(DateUtils.parseDate2String(DateUtils.getThisWeekMonday()));
        resultMap.put("thisWeekOrderNumber",thisWeekOrderNumber);
        // 本周到诊数量
        Integer thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(DateUtils.parseDate2String(DateUtils.getThisWeekMonday()));
        resultMap.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        // 本月预约数量
        Integer thisMonthOrderNumber = orderDao.findOrderCountAfterDate(DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth()));
        resultMap.put("thisMonthOrderNumber",thisMonthOrderNumber);
        // 本月到诊数量
        Integer thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth()));
        resultMap.put("thisMonthVisitsNumber",thisMonthVisitsNumber);

        // 封装热门套餐
        List<Map> hotSetmeal = orderDao.findHotSetmeal();
        resultMap.put("hotSetmeal",hotSetmeal);


        return resultMap;
    }
}
