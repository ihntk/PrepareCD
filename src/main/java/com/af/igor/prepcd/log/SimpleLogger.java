package com.af.igor.prepcd.log;

import com.af.igor.prepcd.MainApp;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ede on 19.09.2016.
 */
public class SimpleLogger {
    private FileOutputStream outputStream;
    private MainApp app = MainApp.getInstance();

    public SimpleLogger() {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String time = dateFormat.format(date);
        try {
            outputStream = new FileOutputStream(app.LOGFILE, true);
            log("=====\nProgram started at " + time + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void log(String message) {
        try {
            outputStream.write((message + "\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopLogging() {
        try {
            log("\nProgram finished\n-----\n\n");
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
