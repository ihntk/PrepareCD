package com.af.igor.prepcd.util;

import com.af.igor.prepcd.MainApp;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by ede on 30.08.2016.
 */
public abstract class ExcelParser {
    MainApp app = MainApp.getInstance();
    protected String excelFile;
    protected XSSFWorkbook workbook;

    public void setExcelFile(String excelFile) {
        this.excelFile= excelFile;
        try {
            workbook=new XSSFWorkbook(excelFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public abstract String[] getLanguage();

}
