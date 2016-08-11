package com.af.igor.prepcd;

/**
 * Created by ede on 11.08.2016.
 */
public class MachineDir extends Machine{
    private String machinePathDir;
    final String MACHINEXLS=super.getMachineName()+".xlsx";


    public MachineDir(String machinePathDir) {
        this.machinePathDir = machinePathDir;
    }


}
