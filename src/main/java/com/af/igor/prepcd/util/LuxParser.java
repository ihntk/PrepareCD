package com.af.igor.prepcd.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

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

    private LuxParser(){}

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


    public LinkedList<String> getLanguage(String luxFile){
        setLuxFile(luxFile);
        LinkedList<String>languages=new LinkedList<>();
        XSSFSheet sheet=workbook.getSheetAt(0);
        XSSFCell row=sheet.getRow(53).getCell(3);

        return languages;
    }
}
