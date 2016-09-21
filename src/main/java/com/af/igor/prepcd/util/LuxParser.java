package com.af.igor.prepcd.util;

import java.util.LinkedList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * Created by ede on 30.08.2016.
 */
public class LuxParser extends ExcelParser{
    private static LuxParser instance;

    private LuxParser(){}

    public static LuxParser getInstance(){
        if (instance==null)
            instance=new LuxParser();
        return instance;
    }


    public String[] getLanguage(){
        List<String>languages=new LinkedList<>();
        XSSFSheet sheet=workbook.getSheet("Encodage Admin");
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

        return (String[]) languages.toArray();
    }

    public String getMachineType() {
        XSSFSheet sheet=workbook.getSheet("Encodage Admin");
        String machineType=String.format("%s", sheet.getRow(1).getCell(2)).replaceAll(" ","").trim();
        return machineType;
    }

    public String getMachineData() {
        app.logger.log("getMachineData() do nothing");
        return null;
    }
}
