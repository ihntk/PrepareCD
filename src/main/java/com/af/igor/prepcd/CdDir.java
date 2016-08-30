package com.af.igor.prepcd;

/**
 * Created by ede on 11.08.2016.
 */
public class CdDir {
    MainApp app = MainApp.getInstance();
    private final Machine machine;
    protected String cdPathDir;

    public CdDir(Machine machine) {
        this.machine = machine;
        this.cdPathDir = app.CDS + machine.getMachineName() + "\\";
    }

}
