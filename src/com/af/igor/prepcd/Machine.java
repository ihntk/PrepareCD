package com.af.igor.prepcd;

/**
 * Created by ede on 11.08.2016.
 */
public class Machine {
    MainApp app= MainApp.getInstance();
    private String machineName;
    private String machineType;
    private String machineSeries;

    MachineDir machineDir;
    CdDir cdDir;

    public Machine(String machineName) {
        this.machineName = machineName;
        machineSeries=machineName.substring(2,4);
        machineDir=new MachineDir(app.MACHINES+machineSeries+machineName);
        cdDir=new CdDir(app.CDS+machineName);
    }

    public Machine() {
    }

    public String getMachineName() {
        return machineName;
    }

    public String getMachineSeries() {
        return machineSeries;
    }
}
