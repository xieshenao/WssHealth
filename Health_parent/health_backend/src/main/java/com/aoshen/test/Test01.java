package com.aoshen.test;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

public class Test01 {
    @Test
    public void test(){
        Vector vector = new Vector();
        vector.add("张天一");
        vector.add("奥申");
        vector.add("阿皮");
        for (int i = 0; i < vector.size(); i++) {
            System.out.println("第"+i+"个对象是"+vector.get(i));
        }
    }

    @Test
    public void test01(){
        Vector vector = new Vector();
        vector.add("张天一");
        vector.add("奥申");
        vector.add("阿皮");

        int i = 0;
        for (Object s : vector) {
            i++;
            System.out.println("第"+i+"个元素是"+s);
        }
    }
    @Test
    public void test02(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(date);
        System.out.println(format);
    }
}
