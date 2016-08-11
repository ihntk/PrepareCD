package com.af.igor.prepcd;

/**
 * Created by ede on 11.08.2016.
 */
public class Machine {
    private String machineName;
    private String machineType;

    MachineDir machineDir;
    CdDir cdDir;

    public Machine(String machineName) {
        this.machineName = machineName;
    }

    public Machine(String machineName, String machinePathDir, String cdPathDir) {
        this.machineName=machineName;
        this.machineDir=new MachineDir(machinePathDir);
        this.cdDir=new CdDir(cdPathDir);
    }

    public Machine() {
    }

    public String getMachineName() {
        return machineName;
    }
}
