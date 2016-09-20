package com.af.igor.prepcd.util;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Created by ede on 30.08.2016.
 */
public class MachineExcelParser extends ExcelParser{
    private String machineExcelFile;
    private XSSFWorkbook workbook;
    private static MachineExcelParser instance;

    private MachineExcelParser() {}

    public static MachineExcelParser getInstance() {
        if (instance==null)
            instance=new MachineExcelParser();
        return instance;
    }

    public String[] getLanguage(String machineExcelFile) {
        setExcelFile(machineExcelFile);
        XSSFSheet sheet=workbook.getSheetAt(0);
        String languages=String.format("%8s",sheet.getRow(0).getCell(0));
        languages=languages.substring(languages.indexOf("CD")+3,languages.indexOf("suv")).trim();
        return languages.split("/");
    }
}
