package com.af.igor.prepcd;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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

    public void prepareCd() throws IOException {
        getLuxFile();
        List<String>langs=app.luxParser.getLanguage(machineDir.machinePath + machineDir.luxFile);
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

    public void getLuxFile() throws IOException {
        machineDir.copyLuxFile();
//        app.desktop.open(new File(machineDir.machinePath + machineDir.luxFile));
        app.tc("/l="+machineDir.machinePath);
        //  open machine directory on Liege server ????????????????????????????????????????
    }
}
