package com.af.igor.prepcd.util;

import com.af.igor.prepcd.MainApp;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

/**
 * Created by ede on 30.08.2016.
 */
public abstract class ExcelParser {
    MainApp app = MainApp.getInstance();
    protected String excelFile;
    protected XSSFWorkbook workbook;

    public void setExcelFile(String excelFile) {
        this.excelFile = excelFile;
        try (FileInputStream inputStream = new FileInputStream(new File(excelFile))) {
            workbook = new XSSFWorkbook(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            ConsoleHelper.writeMessage("====\nThere is a problem with machine xls");
        }
    }

    protected void writeXLS() {
        try (FileOutputStream outputStream = new FileOutputStream(new File(excelFile));) {
            workbook.write(outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract String[] getLanguage();

}
