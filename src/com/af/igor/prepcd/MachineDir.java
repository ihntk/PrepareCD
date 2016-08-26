package com.af.igor.prepcd;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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
    String machinePath;
    private final String machineXls;
    private String hMachine = null;
    private String luxFile = null;



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

    private void copyLuxFile() throws IOException {
        String luxPathString = app.LUX_DIR + machine.getSm() + " " + machine.getMachineSeries().substring(0, 1) + "\\";
        luxPathString = luxPathString + searchFileName(luxPathString, machine.getSm() + machine.getMachineSeries()) + "\\";
        /*
        Searching lux xls file
         */
        luxFile = searchFileName(luxPathString, machine.getMachineName());

        if (luxFile != null)
            Files.copy(Paths.get(luxPathString + luxFile), Paths.get(machinePath + luxFile), REPLACE_EXISTING);

    }

    public void getLuxFile() throws IOException {
        copyLuxFile();
        app.desktop.open(new File(machinePath + luxFile));
    }

    private String searchFileName(String path, String pattern) throws IOException {
        String fileName = null;
        int count = 0;
        String[] files = new File(path).list();
        for (String file : files) {
            if (file.startsWith(pattern)) {
                fileName = file;
                count++;
            }
        }
        if (count > 1) {
            app.desktop.open(new File(path));
            System.out.println("Attention! There are " + count + " files for machine " + machine.getMachineName() +
                    "\ncopy it manually from currently opened directory\n" +
                    "Already done? (yes/no)");           //in future we can copy both (or many) files to machineDir and show message in window
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                while (reader.readLine().equals("yes")) {
                    count++;
                    if (count == 5) {
                        System.out.println("something wrong!");
                        fileName = null;
                        break;
//                        Thread.currentThread().stop();        // тут лежить какашка. Потрібно це зробити акуратніше
                    }
                }
            }
        }
        return fileName;
    }

}
