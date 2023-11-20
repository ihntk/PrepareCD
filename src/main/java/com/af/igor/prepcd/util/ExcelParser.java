package com.af.igor.prepcd.util;

import com.af.igor.prepcd.MainApp;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

/**
 * Created by ede on 30.08.2016.
 */
public abstract class ExcelParser {
    MainApp app = MainApp.getInstance();
    protected String excelFile;
    protected XSSFWorkbook workbook;
    protected XSSFSheet sheet;

    public void setExcelFile(String excelFile) {
        this.excelFile = excelFile;
        try (FileInputStream inputStream = new FileInputStream(new File(excelFile))) {
            ZipSecureFile.setMinInflateRatio(0);
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

    protected XSSFCell getCell(CellReference cellReference) {
        return sheet.getRow(cellReference.getRow()).getCell(cellReference.getCol());
    }

    public abstract String[] getLanguages();

}
