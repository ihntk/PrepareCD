package com.af.igor.prepcd;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;
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
    protected final String machineXls;
    private String hMachine = null;
    protected String luxFile = null;


    public MachineDir(Machine machine) {
        this.machine = machine;
        machinePath = app.MACHINES + machine.getSm() + machine.getMachineSeries() + "\\" + machine.getMachineName() + "\\";
        machinePathDir = new File(machinePath);
        machineXls = machine.getMachineName() + ".xlsx";
        luxFile=machine.luxFile;
        machinePathDir.mkdir();
    }

    /*
    list of exist files
     */
    private String[] getFiles() {
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

    protected void copyXls() throws IOException {
        Files.copy(Paths.get(app.XLS), Paths.get(machinePath + machineXls));
        app.logger.log("Copied xls");

        copyLuxFile();
    }

    protected void copyLuxFile() throws IOException {
        if (luxFile != null) {
            try {
                Files.copy(Paths.get(machine.getLuxPathString() + luxFile), Paths.get(machinePath + luxFile), REPLACE_EXISTING);
                app.logger.log("Copied luxFile:\n   " + luxFile);
            } catch (FileSystemException e) {
                System.out.println("I can't replace " + luxFile + " file because it is being used by another process");
                app.logger.log("Could'nt replace:\n   " + luxFile);
            }
        }
    }

    public void rename(String sourceName, String targetName) throws IOException {
        Files.move(Paths.get(machine.machineDir.machinePath+sourceName),Paths.get(machine.machineDir.machinePath+targetName));
    }

    public void copy(String sourceFile, String targetFile) throws IOException {
        Files.copy(Paths.get(sourceFile), Paths.get(targetFile));
    }
}
