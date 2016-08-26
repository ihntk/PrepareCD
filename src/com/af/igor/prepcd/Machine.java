package com.af.igor.prepcd;

import java.io.IOException;

/**
 * Created by ede on 11.08.2016.
 */
public class Machine {
    MainApp app = MainApp.getInstance();
    private final String machineName;
    private String machineType;
    private final String sm;
    private final String machineSeries;
    private final String luxPathString;

    MachineDir machineDir;
    CdDir cdDir;

    public Machine(String machineName) {
        this.machineName = machineName;
        sm = machineName.substring(0, 2);
        machineSeries = machineName.substring(2, 4);
        String luxPath = app.LUX_DIR + getSm() + " " + getMachineSeries().substring(0, 1) + "\\";
        luxPathString = luxPath + searchFileName(luxPath, getSm() + getMachineSeries()) + "\\";

        machineDir = new MachineDir(this);
        cdDir = new CdDir(this);
    }

    private boolean isMachineExist() {
//        getLuxFile;         //??????????????????????????????
        return false;
    }


    public String getMachineName() {
        return machineName;
    }

    public String getMachineSeries() {
        return machineSeries;
    }

    public String getSm() {
        return sm;
    }

    public String getLuxPathString() {
        return luxPathString;
    }

    /*
        plugs!!!!!!!!!!!!!!!!
         */
    public void getMachineXls() throws IOException {
        machineDir.getMachineXls();
    }

    public void prepareCd() {
        cdDir.prepareCd();
    }

    public void getLuxFile() throws IOException {
        machineDir.getLuxFile();
    }
}
