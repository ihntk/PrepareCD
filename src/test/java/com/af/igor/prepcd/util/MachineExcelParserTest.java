package com.af.igor.prepcd.util;

/**
 * Created by ede on 26.04.2017.
 */
public class MachineExcelParserTest {
    private static MachineExcelParser machineExcelParser;
    private static String excelPath = "/home/igor/Public/preparecd/my_docs/workDir/XL's/20XX0000.xlsx";
    private static String machineName = "20JN2354";
    private static String machineType = "L6A";

    public static void main(String[] args) {
        machineExcelParser = MachineExcelParser.getInstance();
        machineExcelParser.setExcelFile(excelPath);
        System.out.println(machineExcelParser.getLanguages()[0]);
//        machineExcelParser.setMachineName(machineName);
//        machineExcelParser.setMachineType(machineType);

    }
}
