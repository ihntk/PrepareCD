package com.af.igor.prepcd;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ede on 11.08.2016.
 */
public class MachineDir extends Machine{

    private File machinePathDir;
    final String MACHINEXLS=super.getMachineName()+".xlsx";


    public MachineDir(String machinePathDir) {
        this.machinePathDir = new File(machinePathDir);
    }

    private File[] getFiles(){
        return machinePathDir.listFiles();

        List<String> pdf=new ArrayList<>();
        List<String> ckd=new ArrayList<>();
        for (String file:files) {
            if (file.contains(".xls")) xls.add(file);
            if (file.contains(".pdf")) pdf.add(file);
            if (file.contains(".ckd")) ckd.add(file);
        }
    }

    public ArrayList<String>getPdfFiles(){
        return ;
    }

    public ArrayList<String>getXlsFiles(){
        List<File> xls=new ArrayList<>();
        for (File file:getFiles()) {
            if (file.contains(".xls")) xls.add(file);
        }
        }
    public ArrayList<String>getSkdFiles(){

    }
}
