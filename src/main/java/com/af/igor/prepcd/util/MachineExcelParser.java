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

    /**
     * When there are not any language, method will return "nothing"
     *
     * @retur languages[]
     */
    public String[] getLanguages() {
        String allLanguages = String.format("%8s", sheet.getRow(8).getCell(1));
        String cdLanguages = "nothing";
        for (String langs : allLanguages.split("\\+")) {
            if (langs.matches("(.*)CD(.*)")) {
                cdLanguages = langs.trim().substring(3).replaceAll("\\s", "");
            }
        }
        return cdLanguages.split("/");
    }

    public String getMPlans() {
        String mPlans = String.format("%s", sheet.getRow(19).getCell(7));
        return mPlans;
    }

}
