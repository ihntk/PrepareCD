package com.af.igor.prepcd;

/**
 * Created by ede on 11.08.2016.
 */
public class Machine {
    private String machineName;
    private String machineType;
    private String machineSeries;

    MachineDir machineDir;
    CdDir cdDir;

    public Machine(String machineName) {
        this.machineName = machineName;
        machineSeries=machineName.substring(2,4);
    }

    public Machine(String machineName, String machinePathDir, String cdPathDir) {
        this.machineName = machineName;
        this.machineDir = new MachineDir(machinePathDir);
        this.cdDir = new CdDir(cdPathDir);
        machineSeries=machineName.substring(2,4);
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
