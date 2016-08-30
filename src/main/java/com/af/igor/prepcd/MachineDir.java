package com.af.igor.prepcd;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Created by ede on 11.08.2016.
 */
public class MachineDir {

    MainApp app = MainApp.getInstance();
    private Machine machine;
    private final File machinePathDir;
    protected String machinePath;
    private final String machineXls;
    private String hMachine = null;
    protected String luxFile = null;


    public MachineDir(Machine machine) {
        this.machine = machine;
        machinePath = app.MACHINES + machine.getSm() + machine.getMachineSeries() + "\\" + machine.getMachineName() + "\\";
        machinePathDir = new File(machinePath);
        machineXls = machine.getMachineName() + ".xlsx";
        machinePathDir.mkdir();
    }

    /*
    list of exist files
     */
    private File[] getFiles() {
        return machinePathDir.listFiles();
    }

    public ArrayList<File> getXlsFiles() {
        ArrayList<File> xls = new ArrayList<>();
        for (File file : getFiles()) {
            if (file.getName().endsWith(".xlsx")
                    || file.getName().endsWith(".xls"))
                xls.add(file);
        }
        return xls;
    }

    public ArrayList<File> getPdfFiles() {
        ArrayList<File> pdf = new ArrayList<>();
        for (File file :
                getFiles()) {
            if (file.getName().endsWith(".pdf"))
                pdf.add(file);
        }
        return pdf;
    }

    public ArrayList<File> getCkdFiles() {
        ArrayList<File> ckd = new ArrayList<>();
        for (File file : getFiles()) {
            if (file.getName().endsWith(".ckd"))
                ckd.add(file);
        }
        return ckd;
    }

    /*
    getMachineXls() chec machineXls exist, if no copy and launch it in excel
    also copy Luxemburd xls and launch it too
     */
    public void getMachineXls() throws IOException {
        if (!new File(machinePath + machineXls).exists()) {
            copyXls();
            app.desktop.open(new File(machinePath + machineXls));
            app.desktop.open(new File(machinePath + luxFile));
        } else {
            app.desktop.open(new File(machinePath + machineXls));
        }
    }

    private void copyXls() throws IOException {
        Files.copy(Paths.get(app.XLS), Paths.get(machinePath + machineXls));

        copyLuxFile();
    }

    protected void copyLuxFile() throws IOException {
        /*
        Searching lux xls file
         */
        luxFile = app.searchFileName(machine.getLuxPathString(), machine.getMachineName());

        if (luxFile != null)
            Files.copy(Paths.get(machine.getLuxPathString() + luxFile), Paths.get(machinePath + luxFile), REPLACE_EXISTING);

    }
}
