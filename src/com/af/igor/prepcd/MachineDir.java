package com.af.igor.prepcd;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ede on 11.08.2016.
 */
public class MachineDir extends Machine {

    MainApp app= MainApp.getInstance();
    private File machinePathDir;
    final String MACHINEXLS = getMachineName() + ".xlsx";


    public MachineDir(String machinePathDir) {
        this.machinePathDir = new File(machinePathDir);
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
    getMACHINEXLS() chec machineXls exist, if no copy and launch it in excel
    also need add line to copy Luxemburd xls and launch it too !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     */
    public void getMACHINEXLS() throws IOException {
        ArrayList<File>xls=getXlsFiles();
        File machineXls = null;
        if (xls.size()>0)
            for (File file:xls){
                if (file.getName().equals(MACHINEXLS)){
                    machineXls=file;
                    break;
                }
            }else{
            Files.copy(Paths.get(app.XLS), Paths.get(machinePathDir + "\\" + MACHINEXLS));
            machineXls=new File(MACHINEXLS);
            app.desktop.open(machineXls);
        }
//        return machineXls;
    }

}
