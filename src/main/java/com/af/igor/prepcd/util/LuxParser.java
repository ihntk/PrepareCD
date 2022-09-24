package com.af.igor.prepcd.util;

import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by ede on 30.08.2016.
 */
public class LuxParser extends ExcelParser {
    private static LuxParser instance;

    private LuxParser() {
    }

    public static LuxParser getInstance() {
        if (instance == null)
            instance = new LuxParser();
        return instance;
    }


    public String[] getLanguages() {
        List<String> languages = new LinkedList<>();
        sheet = workbook.getSheet("Encodage Admin");
        ConsoleHelper.writeMessage("last row " + sheet.getLastRowNum());
        for (int i = 0; i < sheet.getLastRowNum(); i++) {
            XSSFCell cell = sheet.getRow(i).getCell(2);
            //                                              need to test!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        }
        for (int i = 0; i < 7; i++) {
            if (i == 4) languages.add("-");
            XSSFCell cell = sheet.getRow(34 + i).getCell(2);
            String value = cell.getStringCellValue();
            if (value != null) languages.add(value);
        }

        return (String[]) languages.toArray();
    }

    public String getMachineType() {
        sheet = workbook.getSheet("Encodage Admin");
        String machineType = String.format("%s", sheet.getRow(1).getCell(2)).replaceAll(" ", "").trim();
        return machineType;
    }

    public String getSgBar() {
        sheet = workbook.getSheet("For Manual");
        String sgFormula = String.format("%s", sheet.getRow(2).getCell(3));
        String sgAddress = sgFormula.substring(sgFormula.indexOf("'!") + 2, sgFormula.indexOf("<>"));
        sheet = workbook.getSheet("Encodage Scope");
        String cellText = getCell(new CellReference(sgAddress)).getStringCellValue();

        return cellText.substring(0, cellText.indexOf(" "));
    }

    public String getMachineData() {
        app.logger.log("getMachineData() do nothing");
        return null;
    }
}
