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
    static final String XLS = "d:\\my_docs\\workDir\\XL's\\20XX0000.xlsx";
    static final String MACHINES = "d:\\my_docs\\plans\\";
    static final String H_MACHINES = "h:\\DATAGEN\\Bt\\1.Client\\1.1.Machines\\";
    static final String CDS = "d:\\my_docs\\cdrom\\";
    static final String LUX_DIR = "k:\\Vente\\1. Customer\\1.3. Commandes\\";
    static final String CDTEMPLATE = "\\\\Serverua\\AF_UA\\1.4.CD\\WEB3_Operator Manual\\";
    static Desktop desktop = Desktop.getDesktop();

    private MainApp() {
    }

    public static MainApp getInstance() {
        if (instance == null) {
            instance = new MainApp();
            return instance;
        } else return instance;
    }

    public static Machine getMachine() {
        return machine;
    }

    void initializePath(String machineName) {
        /*
        Use current dir as path to machine
         */
        if (machineName == null) {
            Path path = Paths.get("").toAbsolutePath();
            machineName = path.getFileName().toString().toUpperCase();
        }

        machine = new Machine(machineName);
    }

    private void help() {
        System.out.println("_________________\n" +
                "Available parameters:\n" +
                "-n [machine name]    Name of machine\n" +
                "-x xls               Create xls\n" +
                "-c                   Prepare cd");
    }

    public static void main(String[] args) throws IOException {
        /*
        available flags
        -x xls
        -n machine name
        -c cd
        -i installation

        parsing args
        first of all we need to find -n flag and if it exist create machine instance with machine name as machine directory
        else we use current directory as machine dir
         */

        byte use = 0;
        String machineName = null;

        /*
        identify machine name
         */
        for (int i = 0; i < args.length; i++) {
            String flag = args[i];
            machineName = null;
            try {
                if (flag.equals("-n")) {
                    machineName = args[i + 1].toUpperCase();
                    if (machineName.startsWith("-")) throw new ArrayIndexOutOfBoundsException();
                    break;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("\nYou must specify the filename after -n flag");
                getInstance().help();
                return;
            }
        }

        getInstance().initializePath(machineName);

        /*
        identify target
         */
        for (int i = 0; i < args.length; i++) {
            String flag = args[i];
            if (flag.equals("-x")) use = (byte) (use | 1);
            if (flag.equals("-c")) use = (byte) (use | 2);
            if (flag.equals("-i")) use = (byte) (use | 4);
        }


        /*
        use - marker control program:
        0 - do nothing
        1 - xls
        2 - cd
        3 - xls+cd
        4 - installation
         */
        if (use == 0) {
            System.out.println("\nYou must specify target. Application wont to know what to do");
            getInstance().help();
        }
        if (use == 1) getInstance().machine.machineDir.getMachineXls();
        if (use == 2) getInstance().machine.cdDir.prepareCd();
        if (use == 3) {
            getInstance().machine.machineDir.getMachineXls();
            getInstance().machine.cdDir.prepareCd();
        }
        if (use == 4) getInstance().machine.machineDir.getLuxFile();

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
