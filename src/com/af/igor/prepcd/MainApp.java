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
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainApp {
    private static MainApp instance;
    private static Machine machine;         //in future this field will replace static ArrayList<Machine>
    static final String XLS = "d:\\my_docs\\workDir\\XL's\\20XX0000.xls";
    static final String MACHINES = "d:\\my_docs\\plans\\";
    static final String CDS = "d:\\my_docs\\cdrom\\";
    static final String LUX_DIR="";         //need to add luxemburg dir path
    static Desktop desktop = Desktop.getDesktop();

    private MainApp() {
    }

    public static MainApp getInstance() {
        if (instance == null) {
            instance = new MainApp();
            return instance;
        } else return instance;
    }

    void initializePath(String[] args) {
        /*
        Use current dir as path to machine
         */
        Path path = Paths.get("").toAbsolutePath();
        String pwd = path.toString();
        String machineName = path.getFileName().toString();

//        machine=new Machine(machineName);

        /*
        next code mast be in Machine class and Machine(a,b,c) constructor mast be deleted!
         */
        String machinePathDir;
        String cdPathDir;

        if (args.length > 1) {
            machinePathDir = args[0];
            cdPathDir = args[1];
        } else if (args.length == 1) {
            machinePathDir = pwd;
            cdPathDir = args[0];
        } else {
            machinePathDir = pwd;
            cdPathDir = CDS + machineName;
        }

        machine = new Machine(machineName, machinePathDir, cdPathDir);

    }

    public static void main(String[] args) throws IOException {
        getInstance().initializePath(args);

        //create machine xls
        getInstance().machine.machineDir.getMachineXls();

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

}
