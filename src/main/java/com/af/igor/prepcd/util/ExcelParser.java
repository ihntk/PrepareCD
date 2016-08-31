package com.af.igor.prepcd.util;

/**
 * Created by ede on 30.08.2016.
 */
public class ExcelParser {
    private static ExcelParser instance;

    private ExcelParser() {}

    public static ExcelParser getinstance() {
        if (instance==null)
            instance=new ExcelParser();
        return instance;
    }
}
