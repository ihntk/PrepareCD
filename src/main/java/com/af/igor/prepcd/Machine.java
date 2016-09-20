package com.af.igor.prepcd;

import java.io.BufferedReader;
import java.io.File;
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
    protected final String hMachinePath;
    protected final String I_PLANS=app.PLANS+"002 - Plan d'installation 2008\\";

    MachineDir machineDir;
    CdDir cdDir;

    public Machine(String machineName) throws IOException {
        this.machineName = machineName;
        sm = machineName.substring(0, 2);
        machineSeries = machineName.substring(2, 4);
        String luxPath = app.LUX_DIR + getSm() + " " + getMachineSeries().substring(0, 1) + "\\";
        String machSer=machineSeries.substring(0,1);
        String smMachSer = sm+machineSeries;
        if (machSer.equals("H")||machSer.equals("I")||machSer.equals("J")) smMachSer=sm+" "+machineSeries;
        luxPathString = luxPath + app.searchFileName(luxPath, smMachSer) + "\\";
        if (app.searchFileName(getLuxPathString(), getMachineName()) == null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                System.out.println("Are you sure the machine " + machineName + " is exist? (y/N)");
                if (!reader.readLine().toLowerCase().equals("y")) Thread.currentThread().stop();
            }
        }
        String hMachPath=app.H_MACHINES+getSm()+getMachineSeries().substring(0,1)+"\\";
        hMachinePath=hMachPath+app.searchFileName(hMachPath,smMachSer)+"\\";
        machineDir = new MachineDir(this);
        cdDir = new CdDir(this);
    }

    public void setMachineType(String machineType) {
        this.machineType = machineType;
        app.logger.log("Machine type is: "+this.machineType);
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
            isXlsCreated=true;
        }
        app.logger.log("isXlsCreated is: "+isXlsCreated);
        return isXlsCreated;
    }

    public void prepareCd() throws IOException {
        String[] langs=app.machineExcelParser.getLanguage(machineDir.machinePath + machineDir.luxFile);
        for (String lang:langs){
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
}
