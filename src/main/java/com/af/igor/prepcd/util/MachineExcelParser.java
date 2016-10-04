package com.af.igor.prepcd.util;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Created by ede on 30.08.2016.
 */
public class MachineExcelParser extends ExcelParser{
    private static MachineExcelParser instance;

    private MachineExcelParser() {}

    public static MachineExcelParser getInstance() {
        if (instance==null)
            instance=new MachineExcelParser();
        return instance;
    }

    public String[] getLanguage() {
        XSSFSheet sheet=workbook.getSheetAt(0);
        String languages=String.format("%8s",sheet.getRow(0).getCell(0));
        languages=languages.substring(languages.indexOf("CD")+3,languages.indexOf("suv")).trim();
        return languages.split("/");
    }

    public String getMPlans() {
        XSSFSheet sheet=workbook.getSheetAt(0);
        String mPlans=String.format("/s",sheet.getRow(19).getCell(1));
        mPlans=mPlans.substring(0,mPlans.indexOf("-"));
        return mPlans;
    }
}
