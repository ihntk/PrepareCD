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

import com.af.igor.prepcd.util.MachineExcelParser;
import com.af.igor.prepcd.util.LuxParser;
import com.af.igor.prepcd.util.MachinesCode;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainApp {
    private static MainApp instance;
    private static String version = "0.1.3.1";
    private static Machine machine;         //in future this field will replace static ArrayList<Machine>
    static final String XLS = "d:\\my_docs\\workDir\\XL's\\20XX0000.xlsx";
    static final String MACHINES = "d:\\my_docs\\plans\\";
    static final String H_MACHINES = "h:\\DATAGEN\\Bt\\1.Client\\1.1.Machines\\";
    static final String CDS = "d:\\my_docs\\cdrom\\";
    static final String LUX_DIR = "k:\\Vente\\1. Customer\\1.3. Commandes\\";
    static final String CDTEMPLATE = "\\\\Serverua\\AF_UA\\1.4.CD\\WEB3_Operator Manual\\";
    static final String PLANS="\\\\Serverua\\af_ua\\1.2.Plans\\";
    static Desktop desktop = Desktop.getDesktop();
    static final String totalCommander="C:\\Program Files\\totalcmd\\TOTALCMD64";
    static public LuxParser luxParser;
    static MachineExcelParser machineExcelParser;

    private MainApp() {
    }

    public static MainApp getInstance() {
        if (instance == null) {
            instance = new MainApp();
            luxParser=LuxParser.getinstance();
            machineExcelParser =MachineExcelParser.getinstance();
            return instance;
        } else return instance;
    }

    public static Machine getMachine() {
        return machine;
    }

    void initializePath(String machineName) throws IOException, InterruptedException {
        /*
        Use current dir as path to machine
         */
        if (machineName == null) {
            Path path = Paths.get("").toAbsolutePath();
            if (!path.toString().startsWith(MACHINES)) {
                System.out.println("\nYou are not in \"plans\" directory");
                getInstance().help();
                return;
            }
            machineName = path.getFileName().toString().toUpperCase();
        }

        machine = new Machine(machineName);
    }

    private void help() throws InterruptedException {
        System.out.println("_________________\n" +
                "Available parameters:\n" +
                "-n [machine name]    Name of machine\n" +
                "-i                   Make installation\n" +
                "-x                   Create xls\n" +
                "-c                   Prepare cd");
        Thread.sleep(5000);
    }

    public static String getVersion() {
        return "\nPrepareCD version " + version;
    }

    public String searchFileName(String path, String pattern) throws IOException {
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
            desktop.open(new File(path));
            System.out.println("Attention! There are " + count + " files for machine " + machine.getMachineName() +
                    "\ncopy it manually from currently opened directory\n" +
                    "Already done? (y/N)");           //in future we can copy both (or many) files to machineDir and show message in window
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                while (reader.readLine().toLowerCase().equals("y")) {
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

    public void tc(String parameters) throws IOException {
        Runtime runtime=Runtime.getRuntime();
        String[] command={totalCommander,"/o ",parameters};
        Process process=runtime.exec(command);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        /*
        available flags
        -x xls
        -n machine name
        -c cd
        -i installation
        -v version

        parsing args
        first of all we need to find -n flag and if it exist create machine instance with machine name as machine directory
        else we use current directory as machine dir
         */

        byte use = 0;
        String machineName = null;

        /*
        program version
         */
        for (int i = 0; i < args.length; i++) {
            String flag = args[i];
            if (flag.equals("-v")) {
                System.out.println(getVersion());
                return;
            }
        }

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
        if (machine == null) return;

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
            getInstance().tc("/l="+machine.machineDir.machinePath);
            getInstance().help();
        }
        if (use == 1) getInstance().machine.getMachineXls();
//        if (use == 2) getInstance().machine.prepareCd();
//        if (use == 3) {
//            getInstance().machine.getMachineXls();
//            getInstance().machine.prepareCd();
//        }
        if (use == 4) {
            getInstance().machine.openLuxFile();
            getInstance().tc("/l="+machine.machineDir.machinePath);
            getInstance().tc("/r="+machine.hMachinePath);
            getInstance().tc("/t /r="+machine.I_PLANS);
            System.out.println("Copy base installation drawing\nAlready done? (press enter)");
            machine.setMachineType(luxParser.getMachineType(machine.getLuxFile()));
            new BufferedReader(new InputStreamReader(System.in)).readLine();
            String installationName="I"+ MachinesCode.valueOf("_"+machine.getMachineType())+"-"+machine.getMachineName().substring(2);
            Files.move(Paths.get(machine.machineDir.getCkdFiles().get(0)),Paths.get(installationName));
            getInstance().desktop.open(new File(machine.machineDir.machinePath+installationName));
        }

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
