package com.af.igor.prepcd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

    public Machine(String machineName) throws IOException {
        this.machineName = machineName;
        sm = machineName.substring(0, 2);
        machineSeries = machineName.substring(2, 4);
        String luxPath = app.LUX_DIR + getSm() + " " + getMachineSeries().substring(0, 1) + "\\";
        luxPathString = luxPath + app.searchFileName(luxPath, getSm() + getMachineSeries()) + "\\";
        if(app.searchFileName(getLuxPathString(), getMachineName())==null){
            try(BufferedReader reader=new BufferedReader(new InputStreamReader(System.in))){
                System.out.println("Are you sure the machine "+machineName+" is exist? (y/N)");
                if (!reader.readLine().toLowerCase().equals("y")) Thread.currentThread().stop();
            }
        }
        machineDir = new MachineDir(this);
        cdDir = new CdDir(this);
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
