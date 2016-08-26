package com.af.igor.prepcd;

import java.io.IOException;

/**
 * Created by ede on 11.08.2016.
 */
public class Machine {
    MainApp app = MainApp.getInstance();
    private String machineName;
    private String machineType;
    private String sm;
    private String machineSeries;

    MachineDir machineDir;
    CdDir cdDir;

    public Machine(String machineName) {
        this.machineName = machineName;
        sm = machineName.substring(0, 2);
        machineSeries = machineName.substring(2, 4);
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
