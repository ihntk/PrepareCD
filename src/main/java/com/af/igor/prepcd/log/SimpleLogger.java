package com.af.igor.prepcd.log;

import com.af.igor.prepcd.MainApp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ede on 19.09.2016.
 */
public class SimpleLogger {
    private FileOutputStream outputStream;
    private MainApp app=MainApp.getInstance();

    public SimpleLogger() {
        try {
            outputStream=new FileOutputStream(new File(app.LOGFILE));
            log("=====\nProgram started\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void log(String message) {
        try {
            outputStream.write((message+"\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopLogging(){
        try {
            log("\nProgram finished\n-----\n");
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
