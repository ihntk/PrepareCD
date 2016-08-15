package com.af.igor.prepcd;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by ede on 11.08.2016.
 */
public class MachineDir extends Machine {

    MainApp app= MainApp.getInstance();
    private File machinePathDir;
    private String machineXls = getMachineName() + ".xlsx";
    private String luxFile = null;



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
        if (xls.size()<=0){
            copyXls();
            machineXls=new File(this.machineXls);
            app.desktop.open(machineXls);
        }
        else{
            for (File file:xls){
            if (file.getName().equals(this.machineXls)){
                machineXls=file;
                break;
            }
        }
        }
//        return machineXls;
    }

    private void copyXls() throws IOException {
        Files.copy(Paths.get(app.XLS), Paths.get(machinePathDir + "\\" + machineXls));

        String luxPathString=app.LUX_DIR+"\\"+getMachineSeries().charAt(0)+"\\"+getMachineSeries()+"\\";
        /*
        Searching lux xls file
         */
        String[]luxFiles=new File(luxPathString).list();
        int count=0;
        for (String lux:luxFiles){
            if (lux.startsWith(getMachineName())){
                luxFile=lux;
                count++;
            }
        }
        if (count>1){
            app.desktop.open(new File(luxPathString));
            System.out.println("Attention! There are "+count+" files for machine "+getMachineName());           //in future we can copy both (or many) files to machineDir and show message in window
        }else Files.copy(Paths.get(luxPathString+luxFile), Paths.get(machinePathDir.toString()));

    }

}
