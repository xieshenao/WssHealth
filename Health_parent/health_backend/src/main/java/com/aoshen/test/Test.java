/*
package com.aoshen.test;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

*/
/**
 * POI测试
 *//*

public class Test {

    @org.junit.Test
    public void test01() throws Exception {
        //加载指定文件，创建Excel对象
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File("D:\\poi.xlsx")));
        //获取文件中第一个标签页Sheet
        XSSFSheet sheetAt = workbook.getSheetAt(0);
        //获取当前工作表中最后一个行号
        int lastRowNum = sheetAt.getLastRowNum();
        System.out.println("最后一个行号："+lastRowNum);
        //根据行号建立循环，获取行号
        for (int i = 0; i <= lastRowNum; i++) {//行号索引从0开始
            //根据行号获取每一行
            XSSFRow row = sheetAt.getRow(i);
            //获取最后一个单元格索引
            short lastCellNum = row.getLastCellNum();
            System.out.println("最后一个单元格："+lastCellNum);
            for (int j = 1; j <= lastCellNum; j++) {//单元格索引从1开始
                //获取每个单元格对象
                XSSFCell cell = row.getCell(j);
                System.out.println(cell.getStringCellValue());
            }
        }
        //关闭资源
        workbook.close();
    }

    //向Excel表中写入数据
    @org.junit.Test
    public void test02() throws Exception{
        //创建工作表
        XSSFWorkbook excel = new XSSFWorkbook();
        //创建一个工作表对象
        XSSFSheet sheet = excel.createSheet("奥申");
        //向工作表中创建行对象
        XSSFRow row = sheet.createRow(0);
        //在行对象中创建单元格对象
        row.createCell(0).setCellValue("姓名");
        row.createCell(1).setCellValue("地址");
        row.createCell(2).setCellValue("年龄");

        XSSFRow dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue("小明");
        dataRow.createCell(1).setCellValue("北京");
        dataRow.createCell(2).setCellValue(20);
        //创建一个输出流
        FileOutputStream fileOutputStream = new FileOutputStream(new File("D:\\hello.xlsx"));
        excel.write(fileOutputStream);
        //关流
        fileOutputStream.close();
        //关资源
         excel.close();
    }

    //使用EasyPOI（导入）
    @org.junit.Test
    public void test03() throws Exception {
        ImportParams params = new ImportParams();
        //params.setTitleRows(0);
        params.setHeadRows(0);
        long start = new Date().getTime();
        List<ExcelEntity> list = ExcelImportUtil.importExcel(
                new File("D:\\hello.xlsx"), ExcelEntity.class, params);

        System.out.println(new Date().getTime() - start);
        System.out.println(list.size());
        System.out.println(ReflectionToStringBuilder.toString(list.get(0)));
        //导出代码
        */
/*List<ExcelEntity> list = new ArrayList<>();
        ExcelEntity entity = new ExcelEntity();
        entity.setName("张三");
        entity.setAddress("上海");
        entity.setAge(30);
        list.add(entity);

        ExcelEntity entity2 = new ExcelEntity();
        entity2.setName("李四");
        entity2.setAddress("天津");
        entity2.setAge(25);
        list.add(entity2);


        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("标题","测试"),
                ExcelEntity .class, list);
        workbook.write(new FileOutputStream("D:\\hello2.xlsx"));
        workbook.close();*//*

    }

}
*/
