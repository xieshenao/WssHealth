package cn.wss.controller;

import cn.wss.constant.MessageConstant;
import cn.wss.entity.Result;
import cn.wss.pojo.HotSetmeal;
import cn.wss.pojo.OrderSetting;
import cn.wss.pojo.SetMealNumber;
import cn.wss.service.MemberService;
import cn.wss.service.ReportService;
import cn.wss.service.SetMealService;
import cn.wss.utils.DateUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.*;

/**
 * Echarts图形展示
 */
@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    private MemberService memberService;
    @Reference
    private SetMealService setMealService;
    @Reference
    private ReportService reportService;
    /**
     * 会员数量折线图
     * @return
     * @throws Exception
     */
    @RequestMapping("/getMemberReport")
    public Result getMemberReport() throws Exception {
        try{
        //模拟数据
        Map map = new HashMap();
        List<String>months = new ArrayList<>();
        List<Integer>memberCount = new ArrayList<>();

        //使用日历类获取十二个月份
        Calendar calendar = Calendar.getInstance();
        //往前推十一个月，也就是计算一年的数据
        calendar.add(Calendar.MONTH,-11);
        //循环获取每月
        for (int i = 0; i < 12; i++) {
            //转换格式,2019.08
            String date = DateUtils.parseDate2String(calendar.getTime(),"yyyy.MM");
            //添加到list集合中
            months.add(date);
            calendar.add(Calendar.MONTH,1);//往后加一个月，以此遍历出十二个月

        }
            //添加到map
            map.put("months",months);
            //计算截止到每月的会员数量(根据日期查询)
            memberCount = memberService.findCountByRegTime(months);
            //添加到map中
            map.put("memberCount",memberCount);
            //返回前端数据
            return new Result(true,"查询会员折线图成功",map);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"查询会员折线图失败");
        }

    }


    /**
     * 套餐数据饼图
     * @return
     */
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){
        try{
            //使用Map接收返回前端数据
            Map<String,Object>map = new HashMap<>();
            //创建套餐数量集合,调用业务层方法查询出套餐及其被预约的数量，进行封装
            List<SetMealNumber>list = setMealService.findSetMealCount();
            //创建套餐集合
            List<String>list1 = new ArrayList<>();
            //遍历集合
            for (SetMealNumber setMealNumber : list) {
                String name = setMealNumber.getName();//套餐名
                list1.add(name);
            }
            //存储到map集合中
            map.put("setmealNames",list1);
            map.put("setmealCount",list);

            return new Result(true,"获取套餐占比成功",map);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"获取套餐占比失败");
        }
    }
    /**
     * 查询各种数据
     */
    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        try {
            Map<String, Object> map = reportService.getBusinessReport();
            return new Result(true,"查询成功",map);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"查询失败");
        }

    }
    /**
     * 导出excel表格
     */
    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        try{
            //从map中获取数据
            Map<String,Object> result = reportService.getBusinessReport();
            //取出返回结果数据，准备将报表数据写入到Excel文件中
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<HotSetmeal>hotSetmeal = (List<HotSetmeal>) result.get("hotSetmeal");

            //获取模板路径
            String filePath = request.getSession().getServletContext().getRealPath("template")+ File.separator+"report_template_easypoi.xlsx";
            //在内存中创建表格对象
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(new File(filePath)));
            //读取第一个工作表
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);

            //给每个单元格赋值
            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);//今天日期
            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//今天新增会员数
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
            //遍历套餐数据
            for (HotSetmeal setmeal : hotSetmeal) {
                String name = setmeal.getName();//名称
                String setmeal_count = setmeal.getSetmeal_count();//预约数量
                Double proportion = setmeal.getProportion();//预约占比
                row = sheet.getRow(rowNum ++);

                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }

            //使用输出流进行表格下载,基于浏览器作为客户端下载
            OutputStream outputStream = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");//代表的是Excel文件类型
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");//指定以附件形式进行下载
            xssfWorkbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            xssfWorkbook.close();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    //导出PDF文档
    @RequestMapping("/exportBusinessReport4PDF")
    public Result reportPDF(HttpServletRequest request,HttpServletResponse response){
        try{
            //获取数据
            Map<String, Object> map = reportService.getBusinessReport();
            //取出套餐数据
            List<HotSetmeal>hotSetmeal = (List<HotSetmeal>) map.get("hotSetmeal");


            //动态获取模板文件绝对磁盘文件路径
            String jrxmlPath = request.getSession().getServletContext().getRealPath("template")+File.separator + "health_business3.jrxml";

            String jasperPath = request.getSession().getServletContext().getRealPath("template") + File.separator + "health_business3.jasper";

            //编译模板
            JasperCompileManager.compileReportToFile(jrxmlPath,jasperPath);
            //填充数据:使用javaBean的方式
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperPath,map,new JRBeanCollectionDataSource(hotSetmeal));
            //获取输出流
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("application/pdf");
            response.setHeader("content-Disposition", "attachment;filename=report.pdf");
            //输出文件
            JasperExportManager.exportReportToPdfStream(jasperPrint,outputStream);

            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }

    }
    @Test
    public void test(){
        double d = 114.145;
        NumberFormat nf = NumberFormat.getNumberInstance();
        // 保留两位小数
        nf.setMaximumFractionDigits(2);
        nf.format(d);
        System.out.println(nf.format(d));
    }
}
