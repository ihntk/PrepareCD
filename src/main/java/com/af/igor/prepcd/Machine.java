package com.af.igor.prepcd;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by ede on 11.08.2016.
 */
public class Machine {
    protected final String luxFile;
    MainApp app = MainApp.getInstance();
    private final String machineName;
    private String machineType;
    private final String sm;        //first two symbols in machine name (usually 20)
    private final String machineSeries;
    private final String luxPathString;
    protected final String hMachinePath;
    protected final String I_PLANS = app.PLANS + "002 - Plan d'installation\\";

    MachineDir machineDir;
    CdDir cdDir;

    public Machine(String machineName) throws IOException {
        sm = machineName.substring(0, 2);
        machineSeries = machineName.substring(2, 3).equals("Y") ? machineName.substring(2, 3) : machineName.substring(2, 4);
        String luxPath = app.LUX_DIR + getSm() + " " + getMachineSeries().substring(0, 1) + "\\";
        String machSer = String.valueOf(machineSeries.charAt(0));
        String smMachSer = sm + machineSeries;
        String smMachSerLux = smMachSer;
        if (!machSer.equals("Y")) {
            if (machSer.equals("H") || machSer.equals("I") || (machSer.equals("J")&&machineSeries.equals("JA")))
                smMachSerLux = sm + " " + machineSeries;
            luxPath = luxPath + app.searchFileName(luxPath, smMachSerLux) + "\\";
        }
        luxPathString = luxPath;
        luxFile = app.searchFileName(luxPathString, machineName);
        if (luxFile == null) {
            app.logger.log("Program can't find luxemburg file\nluxPathString is:\n   " + luxPathString);
            app.logger.log("machineName is:\n   " + machineName);
            app.desktop.open(new File(luxPathString));
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                System.out.println("Are you sure the machine " + machineName + " is exist? (y/N)");
                if (!reader.readLine().toLowerCase().equals("y")) machineName = null;
            }
        }
        this.machineName = machineName;
        String hMachPath = app.H_MACHINES + getSm() + getMachineSeries().substring(0, 1) + "\\";
        hMachinePath = hMachPath + app.searchFileName(hMachPath, smMachSer) + "\\";
        if (machineName != null) {
            machineDir = new MachineDir(this);
            cdDir = new CdDir(this);
        }
    }

    public void setMachineType(String machineType) {
        this.machineType = machineType;
        app.logger.log("Machine type is: " + this.machineType);
    }

    public String getMachineType() {
        return machineType;
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
    getMachineXls() chec machineXls exist, if no copy and launch it in excel
    also copy Luxemburd xls and launch it too
     */
    public boolean getMachineXls() throws IOException {
        boolean isXlsCreated = false;
        if (!new File(machineDir.machinePath + machineDir.machineXls).exists()) {
            machineDir.copyXls();
            isXlsCreated = true;
        }
        app.logger.log("isXlsCreated is: " + isXlsCreated);
        return isXlsCreated;
    }

    public void prepareCd() throws IOException {
        String[] langs = app.machineExcelParser.getLanguage();
        for (String lang : langs) {
            System.out.println(lang);
        }
//        new File(cdDir.cdPathDir).mkdir();
//        Files.copy(Paths.get(app.CDTEMPLATE),Paths.get(cdDir.cdPathDir));           //вибрати потрібні мови і файли
//        if (isEnglish)copyPlans();
//        else ...;
//        copyMinMop();
//        addCertif();
//        app.desktop.open(lang/index.html);
//        app.desktop.open(index.html);
    }

    public void openLuxFile() throws IOException {
        machineDir.copyLuxFile();
        app.desktop.open(new File(machineDir.machinePath + machineDir.luxFile));
    }

    public String getLuxFile() {
        return machineDir.luxFile;
    }

    public String getXls() {
        return machineDir.machineXls;
    }

    public void renameAllCkd() throws IOException {
        String machineCode=app.getMachineCode();
        ArrayList<String> ckdFiles = machineDir.getCkdFiles();
        for (String file : ckdFiles) {
            if ((file.startsWith("E")) && (!file.startsWith("Etiqclas"))) {
                String renamedCkd = "E" + machineCode + "-" + getMachineName().substring(2) + ".ckd";
                machineDir.rename(file, renamedCkd);
            }
            if (file.startsWith("FS")) {
                String renamedCkd = "FS" + machineCode + "-" + getMachineName().substring(2) + ".ckd";
                machineDir.rename(file, renamedCkd);
            }

            if (file.startsWith("M10") &&
                    !file.startsWith("M100")) {
                String renamedCkd = "M10" + "-" + getMachineName().substring(2) + ".ckd";
                machineDir.rename(file, renamedCkd);
            }
            if (file.startsWith("M20")) {
                String renamedCkd = "M20" + "-" + getMachineName().substring(2) + ".ckd";
                machineDir.rename(file, renamedCkd);
            }
            if (file.startsWith("M30")) {
                String renamedCkd = "M30" + "-" + getMachineName().substring(2) + ".ckd";
                machineDir.rename(file, renamedCkd);
            }
            if (file.startsWith("M40")) {
                String renamedCkd = "M40" + "-" + getMachineName().substring(2) + ".ckd";
                machineDir.rename(file, renamedCkd);
            }
            if (file.startsWith("M50")) {
                String renamedCkd = "M50" + "-" + getMachineName().substring(2) + ".ckd";
                machineDir.rename(file, renamedCkd);
            }
            if (file.startsWith("M60")) {
                String renamedCkd = "M60" + "-" + getMachineName().substring(2) + ".ckd";
                machineDir.rename(file, renamedCkd);
            }
            if (file.startsWith("M70")) {
                String renamedCkd = "M70" + "-" + getMachineName().substring(2) + ".ckd";
                machineDir.rename(file, renamedCkd);
            }
            if (file.startsWith("M80")) {
                String renamedCkd = "M80" + "-" + getMachineName().substring(2) + ".ckd";
                machineDir.rename(file, renamedCkd);
            }
            if (file.startsWith("M90")) {
                String renamedCkd = "M90" + "-" + getMachineName().substring(2) + ".ckd";
                machineDir.rename(file, renamedCkd);
            }
            if (file.startsWith("M100")) {
                String renamedCkd = "M100" + "-" + getMachineName().substring(2) + ".ckd";
                machineDir.rename(file, renamedCkd);
            }
            if (file.startsWith("M110")) {
                String renamedCkd = "M110" + "-" + getMachineName().substring(2) + ".ckd";
                machineDir.rename(file, renamedCkd);
            }

        }
    }

    public void open4CkdFiles() throws IOException {
        ArrayList<String> ckdFiles = machineDir.getCkdFiles();
        for (String file : ckdFiles) {
            if (file.startsWith("FS")) app.desktop.open(new File(machineDir.machinePath + file));
            if (file.startsWith("Etiqclas")) app.desktop.open(new File(machineDir.machinePath + file));
            if ((file.startsWith("E")) && (!file.startsWith("Etiqclas")))
                app.desktop.open(new File(machineDir.machinePath + file));
            if (file.startsWith("I")) app.desktop.open(new File(machineDir.machinePath + file));
        }
    }

    public void copyEtiq() throws IOException {
        String machineEtiq = "Etiqclas" + "-" + getMachineName().substring(2) + ".ckd";
        if (!new File(machineDir.machinePath + machineEtiq).exists())
            machineDir.copy(app.ETIQCLAS, machineDir.machinePath + machineEtiq);
    }
}
