package com.af.igor.prepcd.util;

import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * Created by ede on 30.08.2016.
 */
public class MachineExcelParser extends ExcelParser {
    private static MachineExcelParser instance;

    private MachineExcelParser() {
    }

    XSSFSheet sheet;

    @Override
    public void setExcelFile(String excelFile) {
        super.setExcelFile(excelFile);
        sheet = workbook.getSheetAt(0);
    }

    public static MachineExcelParser getInstance() {
        if (instance == null)
            instance = new MachineExcelParser();
        return instance;
    }

    public String[] getLanguage() {
        String languages = String.format("%8s", sheet.getRow(0).getCell(0));
        languages = languages.substring(languages.indexOf("CD") + 3, languages.indexOf("suv")).trim();
        return languages.split("/");
    }

    public String getMPlans() {
        String mPlans = String.format("%s", sheet.getRow(19).getCell(7));
        return mPlans;
    }
    /*
    setMachineName() and setMachineType() methods works incorrect
     */
//    public void setMachineName(String machineName) {
//        sheet.getRow(2).createCell(7).setCellValue(machineName);
//    }
//
//    public void setMachineType(String machineType) {
//        sheet.getRow(4).createCell(7).setCellValue(machineType);
//        writeXLS();
//    }

}
