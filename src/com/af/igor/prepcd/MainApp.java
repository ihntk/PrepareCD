package com.af.igor.prepcd;

/* This program copy filtered .pdf files to cd's plan directory.
I..., E..., FS... plans rename to I,E,FS    M...-... -> M...pdf se, refr, hpet
First argument is a source directory path, second - target directory path. If argument only one it is a target path,
source path is a current directory. If there is not args, source path is a current directory and
target path is an appropriate cd directory.

else it can rename base .ckd to machine files, alike I...ckd -> I5500-GA1881.ckd
автоматичне створення каталога диску з подальшим копіюванням html файлів, файлів креслень, розпарсювання .xls файла
і автоматичного видалення зайвих директорій, відкриття пдф'ів інсттрукцій для ручного додаваня самих інструкцій.
 */

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MainApp {
    static String machinePathDir;
    static String cdPathDir;
    static String machineName;
    static String machineType;
    static final String XLS="d:\\my_docs\\workDir\\XL's\\20XX0000.xls";
    static final String MACHINEXLS=machineName+".xlsx";


    public static void main(String[] args) throws IOException {
        initializePath(args);
        Desktop desktop=Desktop.getDesktop();

        //list of exist files
        File machineDirectory=new File(machinePathDir);
        String[]files=machineDirectory.list();
        List<String> xls=new ArrayList<>();
        List<String> pdf=new ArrayList<>();
        List<String> ckd=new ArrayList<>();
        for (String file:files) {
            if (file.contains(".xls")) xls.add(file);
            if (file.contains(".pdf")) pdf.add(file);
            if (file.contains(".ckd")) ckd.add(file);
        }

        //create machine xls
        if (!xls.contains(MACHINEXLS)) {
            Files.copy(Paths.get(XLS),Paths.get(machinePathDir+"\\"+MACHINEXLS));
            desktop.open(new File(MACHINEXLS));
        }

        //
//        if (xls.contains(MACHINEXLS))


        /*
        create ArrayList<String> files in directory (or other list of files)
        analize numbers of files and type files
        case1 if there is only 1 xls file - copy base xls and rename it by the name of directory and (if need)
        correct type of machine inside it (using dialog) and open it
        case2 if there are .ckd files rename it according to type of machine (using the previos .xls)
        case3 if there are .pdf files copy it to the print directory (or to the machine directory using flag)
        case4 if there are .pdf files and used flag prepare CD directory using .xls and copy .pdf to plans directory
            maybe will better combine case3 and case4 together
         */
    }
    static void initializePath(String[] args){
        Path path=Paths.get("").toAbsolutePath();
        String pwd=path.toString();
        String cd="d:\\my_docs\\cdrom\\";
        machineName=path.getFileName().toString();

        if (args.length>1) {
            machinePathDir = args[0];
            cdPathDir = args[1];
        }else if (args.length==1){
            machinePathDir = pwd;
            cdPathDir = args[0];
        }else {
            machinePathDir = pwd;
            cdPathDir = cd+machineName;
        }

    }
}
