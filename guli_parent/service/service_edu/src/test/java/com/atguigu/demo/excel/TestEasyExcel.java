package com.atguigu.demo.excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestEasyExcel {

    public static void main(String[] args) {

        //实现excel写操作
        //excelWrite();

        //实现excel读操作
        excelRead();

    }

    public static void excelWrite(){
        //1、设置写入文件夹地址和excel文件名称
        String filename = "D:\\Java\\项目\\谷粒学苑\\myCode\\EasyExcel\\write.xlsx";


        //2、调用easyexcel方法实现写操作
        //第一个参数是文件夹路径
        //第二个参数是实体类的class
        EasyExcel.write(filename, DemoData.class).sheet("学生列表").doWrite(getData());
    }

    public static void excelRead(){
        String fileName = "D:\\Java\\项目\\谷粒学苑\\myCode\\EasyExcel\\write.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(fileName, DemoData.class, new ExcelListener()).sheet().doRead();
    }

    //创建方法，返回list集合
    private static List<DemoData> getData() {
        List<DemoData> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setSno(i);
            data.setSname("lucy"+i);
            list.add(data);
        }
        return list;
    }
}
