package com.af.igor.prepcd;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

/**
 * Created by ede on 11.08.2016.
 */
public class MachineDir {

    MainApp app = MainApp.getInstance();
    private Machine machine;
    private final File machinePathDir;
    protected String machinePath;
    protected final String machineXls;

    public String getMachinePathString() {
        return machinePath;
    }

    public String getMachineXls() {
        return machineXls;
    }

    public MachineDir(Machine machine) {
        this.machine = machine;
        machinePath = app.MACHINES + machine.getSm() + machine.getMachineSeries() + "/" + machine.getMachineName() + "/";
        machinePathDir = new File(machinePath);
        machineXls = machine.getMachineName() + ".xlsx";
        machinePathDir.mkdir();
    }

    /*
    list of exist files
     */
    public String[] getFiles() {
        return machinePathDir.list();
    }

    public ArrayList<String> getXlsFiles() {
        ArrayList<String> xls = new ArrayList<>();
        for (String file : getFiles()) {
            if (file.endsWith(".xlsx")
                    || file.endsWith(".xls"))
                xls.add(file);
        }
        return xls;
    }

    public ArrayList<String> getPdfFiles() {
        ArrayList<String> pdf = new ArrayList<>();
        for (String file :
                getFiles()) {
            if (file.endsWith(".pdf"))
                pdf.add(file);
        }
        return pdf;
    }

    public ArrayList<String> getCkdFiles() {
        ArrayList<String> ckd = new ArrayList<>();
        for (String file : getFiles()) {
            if (file.endsWith(".ckd"))
                ckd.add(file);
        }
        return ckd;
    }

}
