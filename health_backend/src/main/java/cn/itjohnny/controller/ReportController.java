package cn.itjohnny.controller;

import cn.itjohnny.constant.MessageConstant;
import cn.itjohnny.entity.Result;
import cn.itjohnny.service.MemberServie;
import cn.itjohnny.service.ReportService;
import cn.itjohnny.service.SetMealService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.omg.CORBA.MARSHAL;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberServie memberServie;

    @Reference
    private SetMealService setMealService;

    @Reference
    private ReportService reportService;


    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){
        try {
            HashMap<String, Object> map = new HashMap<>();
            // 封装月份列表
            ArrayList<String> months = new ArrayList<>(); // 用来返回页面展示
            Calendar calendar = Calendar.getInstance();
//            calendar.add(Calendar.MONTH,-11);
            calendar.add(Calendar.MONTH,-50);
//            for (int i = 0; i < 12; i++) {
            for (int i = 0; i < 51; i++) {
                StringBuffer date = new StringBuffer();
                date.append(calendar.get(Calendar.YEAR))
                        .append("年")
                        .append(calendar.get(Calendar.MONTH) + 1)
                        .append("月");
                months.add(date.toString());
                calendar.add(Calendar.MONTH,1);
            }
            System.out.println("months = " + months);

            // 查询封装每个月的会员数
            List<Integer> memberCount = memberServie.countNumberByMonths(months);

            // 封装map返回
            map.put("months",months);
            map.put("memberCount",memberCount);

            return new Result(true,MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }


    }


    @RequestMapping("/getMemberReportByIndication")
    public Result getMemberReportByIndication(String startTime, String endTime){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date start = format.parse(startTime);
            Date end = format.parse(endTime);
            // 后台健壮性判断
            if (start == null || end == null || start.after(end) || end.after(new Date())){
                return new Result(false,MessageConstant.DATE_ERROR);
            }

            // 封装结果的map
            HashMap<String, Object> map = new HashMap<>();

            Calendar startCal = Calendar.getInstance();
            startCal.setTime(start);
            Calendar endCal = Calendar.getInstance();
            endCal.setTime(end);
            // 封装月份列表
            ArrayList<String> months = new ArrayList<>(); // 用来返回页面展示
            while (startCal.before(endCal)){
                String month = new SimpleDateFormat("yyyy年MM月").format(startCal.getTime());
                months.add(month);
                // 步进
                startCal.add(Calendar.MONTH,1);
            }
            // 最后再添加结束月,减少判断
            String endMonth = new SimpleDateFormat("yyyy年MM月").format(endCal.getTime());
            months.add(endMonth);

            // 查询封装每个月的会员数
            List<Integer> memberCount = memberServie.countNumberByMonths(months);

            // 封装map返回
            map.put("months",months);
            map.put("memberCount",memberCount);

            return new Result(true,MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }

    }


    /**
     * 获取各个套餐的占比信息,包括套餐名字的集合,每个套餐预定的数量
     * @return
     */
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){

        try {
            Map<String,Object> resultMap = setMealService.getSetmealReport();

            return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,resultMap);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }

    }


    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){

        try {

        Map<String,Object> businessReportData =  reportService.getBusinessReportData();


        return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,businessReportData);

        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }

    }

    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletResponse response, HttpSession session){

        try {
            // 准备数据
            Map<String,Object> businessReportData =  reportService.getBusinessReportData();
            // 设置响应头
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");

            //取出返回结果数据，准备将报表数据写入到Excel文件中
            String reportDate = (String) businessReportData.get("reportDate");
            Integer todayNewMember = (Integer) businessReportData.get("todayNewMember");
            Integer totalMember = (Integer) businessReportData.get("totalMember");
            Integer thisWeekNewMember = (Integer) businessReportData.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) businessReportData.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) businessReportData.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) businessReportData.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) businessReportData.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) businessReportData.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) businessReportData.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) businessReportData.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) businessReportData.get("hotSetmeal");

            //获得Excel模板文件绝对路径
            String temlateRealPath = session.getServletContext().getRealPath("template") +
                    File.separator + "report_template_poi.xlsx";

            //读取模板文件创建Excel表格对象
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(temlateRealPath)));
            XSSFSheet sheet = workbook.getSheetAt(0);

            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);//日期

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            int rowNum = 12;
            for(Map map : hotSetmeal){//热门套餐
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum ++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }

            //通过输出流进行文件下载
            ServletOutputStream out = response.getOutputStream();
            workbook.write(out);

            out.flush();
            out.close();
            workbook.close();

//            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS);
                return null;
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }

    }


    @RequestMapping("/exportBusinessPDFReport")
    public Result exportBusinessPDFReport(HttpServletResponse response, HttpSession session){
        try {
            // 准备数据
            Map<String,Object> businessReportData =  reportService.getBusinessReportData();
            List<Map> hotSetmeal = (List<Map>) businessReportData.get("hotSetmeal");
            // 设置响应头
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-Disposition", "attachment;filename=report.pdf");

            String jrxmlPath = session.getServletContext().getRealPath("template") +
                    File.separator + "health_business_pdf_template.jrxml";
            String jasperPath = session.getServletContext().getRealPath("template") +
                    File.separator + "health_business_pdf_template.jasper";
            //编译模板
            JasperCompileManager.compileReportToFile(jrxmlPath,jasperPath);

            //填充数据---使用JDBC数据源方式填充
            JasperPrint jasperPrint =
                    JasperFillManager.fillReport(jasperPath,
                            businessReportData,
                            new JRBeanCollectionDataSource(hotSetmeal));

            JasperExportManager.exportReportToPdfStream(jasperPrint,response.getOutputStream());

            return null;
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }



   /* @RequestMapping("/exportBusinessReport")  // poi版本不匹配
    public Result exportBusinessReport(HttpServletResponse response, HttpSession session){

        try {
            // 准备数据
            Map<String,Object> businessReportData =  reportService.getBusinessReportData();
            // 设置响应头
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition","attachment;filename=business_report");

            // 创建工作簿对象
            ExcelWriterBuilder workBook = EasyExcel.write(response.getOutputStream(),Map.class);

            // 使用模板
            workBook.withTemplate(session.getServletContext().getRealPath("template/report_template.xlsx"));


            // 创建工作表对象
            ExcelWriterSheetBuilder sheet = workBook.sheet();


            // 实体类对象或者实体类对象的集合或者map
            // 直接写入文件（填充文件）
            sheet.doFill(businessReportData);

            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS);

        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }

    }*/




}
