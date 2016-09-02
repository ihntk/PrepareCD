package com.af.igor.prepcd.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Created by ede on 30.08.2016.
 */
public class LuxParser {
    private String luxFile;
    private XSSFWorkbook workbook;
    private static LuxParser instance;

    private LuxParser(){}

    public static LuxParser getinstance(){
        if (instance==null)
            instance=new LuxParser();
        return instance;
    }

    public void setLuxFile(String luxFile) {
        this.luxFile = luxFile;
        try(FileInputStream file=new FileInputStream(new File(luxFile))) {
            workbook=new XSSFWorkbook(luxFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public List<String> getLanguage(String luxFile){
        setLuxFile(luxFile);
        List<String>languages=new LinkedList<>();
        XSSFSheet sheet=workbook.getSheetAt(0);
        System.out.println("last row "+sheet.getLastRowNum());
        for (int i = 0; i < sheet.getLastRowNum(); i++) {
            XSSFCell cell=sheet.getRow(i).getCell(2);
            //                                              need to test!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        }
        for (int i = 0; i < 7; i++) {
            if (i==4)languages.add("-");
            XSSFCell cell=sheet.getRow(34+i).getCell(2);
            String value=cell.getStringCellValue();
            if (value!=null)languages.add(value);
        }

        return languages;
    }
}
