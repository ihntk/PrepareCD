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

import com.af.igor.prepcd.log.SimpleLogger;
import com.af.igor.prepcd.util.ConsoleHelper;
import com.af.igor.prepcd.util.LuxParser;
import com.af.igor.prepcd.util.MachineExcelParser;
import com.af.igor.prepcd.util.MachinesCode;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;

public class MainApp {
    public static String LOGFILE;
    public static String LOGERROR;
    static String ETIQCLAS;
    static String XLS;
    static String MACHINES;
    static String H_MACHINES;
    static String CDS;
    static String LUX_DIR;
    static String CDTEMPLATE;
    static String PLANS;
    static String TOTALCOMMANDER;

    private static MainApp instance;
    private static String version = "0.5.2";
    private static Machine machine;         //in future this field will replace static ArrayList<Machine>
    static public LuxParser luxParser;
    static MachineExcelParser machineExcelParser;
    static Desktop desktop = Desktop.getDesktop();
    public SimpleLogger logger;
    private Properties properties = new Properties();
    private static final String PROP_FILE = System.getProperty("user.home")+"/.PrepareCD/PrepareCD.conf";

    private MainApp() {
        try {
            FileInputStream inputStream = new FileInputStream(PROP_FILE);
            properties.load(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        LOGFILE = properties.getProperty("LOGFILE");
        LOGERROR = properties.getProperty("LOGERROR");
        ETIQCLAS = properties.getProperty("ETIQCLAS");
        XLS = properties.getProperty("XLS");
        MACHINES = properties.getProperty("MACHINES");
        H_MACHINES = properties.getProperty("H_MACHINES");
        CDS = properties.getProperty("CDS");
        LUX_DIR = properties.getProperty("LUX_DIR");
        CDTEMPLATE = properties.getProperty("CDTEMPLATE");
        PLANS = properties.getProperty("PLANS");
        TOTALCOMMANDER = properties.getProperty("TOTALCOMMANDER");
    }

    public static MainApp getInstance() {
        if (instance == null) {
            instance = new MainApp();
            instance.logger = new SimpleLogger();
            luxParser = LuxParser.getInstance();
            machineExcelParser = MachineExcelParser.getInstance();
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
                logger.log("You are not in \"plans\" directory");
                getInstance().help();
                return;
            }
            machineName = path.getFileName().toString().toUpperCase();
            logger.log("path got from pwd");
        }

        machine = new Machine(machineName);
        logger.log("machine name is: " + machineName);
    }

    private void help() throws InterruptedException {
        System.out.println("_________________\n" +
                "Available parameters:\n" +
                "-n [machine name]    Name of machine\n" +
                "-i                   Make installation\n" +
                "-x                   Create xls\n" +
                "-c                   Prepare cd\n" +
                "-m                   Prepare machine files\n" +
                "-t                   Insert test string to log file");
        Thread.sleep(7000);
    }

    public static String getVersion() {
        return "\nPrepareCD version " + version;
    }

    /*
    global method
     */
    public String searchFileName(String path, String pattern) {
        ArrayList<String> fileNames = new ArrayList<>(3);
        String fileName = null;
        int count = 0;
        try {
            String[] files = new File(path).list();
            for (String file : files) {
                if (file.startsWith(pattern)) {
                    fileNames.add(file);
                }
            }
            count = fileNames.size();
            if (count > 1) {
                desktop.open(new File(path));
                System.out.println("Attention! There are " + count + " files for machine " + machine.getMachineName());

                for (int i = 0; i < count; i++) {
                    System.out.println("   " + (i + 1) + " - " + fileNames.get(i));
                }

                while (true) {
                    System.out.print("choose number of correct file ");
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                        int number = Integer.parseInt(reader.readLine()) - 1;
                        fileName = fileNames.get(number);
                        break;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("You input incorrect number, please try again");
                    }
                }
            } else fileName = fileNames.get(0);
        } catch (Exception e) {
            logger.log("Error in mainApp.searchFileName\n   path is: " + path + "\n     pattern is: " + pattern + "\n");
            logger.log(e.toString());
            e.printStackTrace();
        }
        return fileName;
    }

    public void tc(String parameters) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        String command = TOTALCOMMANDER + " /O " + parameters;
        Process process = runtime.exec(command);
        logger.log("total commander got parameters:\n   " + command);
    }

    public String getMachineCode() {
        String machineType = null;
        String machineCode;
        if (machine.getMachineType().contains("-"))
            machineType = machine.getMachineType().substring(0, machine.getMachineType().indexOf("-"));
        else machineType = machine.getMachineType();

        try{
            machineCode=MachinesCode.valueOf(machineType).toString();
        }catch (IllegalArgumentException e){
            ConsoleHelper.writeMessage("Input machine's code");
            machineCode=ConsoleHelper.readString();
        }

        return machineCode;
    }

    public void run(String[] args) throws IOException, InterruptedException {
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
                logger.log("Showed version");
                return;
            }
        }

        /*
        test record
         */
        for (int i = 0; i < args.length; i++) {
            String flag = args[i];
            if (flag.equals("-t"))
                logger.log("========THIS RECORD IS FOR TEST ONLY========");
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
                help();
                logger.log("Machine was not specify");
                return;
            }
        }

        initializePath(machineName);
        if (machine == null || machine.getMachineName() == null) {
            logger.log("Error in mainApp.initializePath\n   machineName is: " + machineName + " not exist");
            return;
        }

        /*
        identify target
         */
        for (int i = 0; i < args.length; i++) {
            String flag = args[i];
            if (flag.equals("-x")) use = (byte) (use | 1);
            if (flag.equals("-c")) use = (byte) (use | 2);
            if (flag.equals("-i")) use = (byte) (use | 4);
            if (flag.equals("-m")) use = (byte) (use | 8);
        }


        /*
        use - marker control program:
        0 - do nothing
        1 - xls
        2 - cd
        3 - xls+cd
        4 - installation
        8 - machine
         */
        if (use == 0) {
            logger.log("target isn't specified");
            System.out.println("\nYou must specify target. Application wont to know what to do");
            tc("/l=\"" + machine.machineDir.machinePath + "\"");
            help();
        }
        if (use == 1) {
            logger.log("target is: xls");
            if (!machine.getMachineXls()) {
                tc("/l=\"" + machine.machineDir.machinePath + "\" /t /r=\"" + machine.hMachinePath + "\"");
                desktop.open(new File(machine.machineDir.machinePath + machine.machineDir.machineXls));
                logger.log("xls is opened");
            } else {
                luxParser.setExcelFile(machine.machineDir.machinePath + machine.getLuxFile());
                machineExcelParser.setExcelFile(machine.machineDir.machinePath + machine.getXls());
                machine.setMachineType(luxParser.getMachineType());
                luxParser.getMachineData();
//                machineExcelParser.setMachineType(machine.getMachineType());

                tc("/l=\"" + machine.machineDir.machinePath + "\" /t /r=\"" + machine.hMachinePath + "\"");
                desktop.open(new File(machine.machineDir.machinePath + machine.machineDir.machineXls));
                logger.log("xls is opened");
                desktop.open(new File(machine.machineDir.machinePath + machine.machineDir.luxFile));
                logger.log("lux is opened");
            }
        }

        if (use == 4) {
            logger.log("target is: installation");
            machine.openLuxFile();
            luxParser.setExcelFile(machine.machineDir.machinePath + machine.getLuxFile());
            tc("/l=\"" + machine.machineDir.machinePath + "\" /t /r=\"" + machine.I_PLANS + "\"");
            logger.log("Opened in tc: \n   " + machine.machineDir.machinePath + "\n   " + machine.I_PLANS);
            machine.setMachineType(luxParser.getMachineType());
            System.out.println("Copy base installation drawing for "+machine.getMachineType()+"\nand then press enter");
            new BufferedReader(new InputStreamReader(System.in)).readLine();
            String installationName = "I" + getMachineCode() + "-" + machine.getMachineName().substring(2) + ".ckd";
            Files.move(Paths.get(machine.machineDir.machinePath + machine.machineDir.getCkdFiles().get(0)), Paths.get(machine.machineDir.machinePath + installationName));  //rename installation
            desktop.open(new File(machine.machineDir.machinePath + installationName));
            logger.log("Installation " + installationName + " opened");
            tc("/r=\"" + machine.hMachinePath + "\"");
            logger.log("Opened in tc :\n   " + machine.hMachinePath);
        }

        if (use == 8) {
            logger.log("target is: machine");
            tc("/l=\"" + machine.machineDir.machinePath + "\" /t /r=\"" + PLANS + "\"");
            logger.log("Opened in tc: \n   " + machine.machineDir.machinePath + "\n   " + PLANS);
            machineExcelParser.setExcelFile(machine.machineDir.machinePath + machine.getXls());
            luxParser.setExcelFile(machine.machineDir.machinePath + machine.getLuxFile());
            String machineType = luxParser.getMachineType();
            machine.setMachineType(machineType);
            String mPlans = machineExcelParser.getMPlans();
            logger.log(mPlans);
            System.out.println("Copy base drawings and then press enter\n------\nFor machine " + machineType + "\nYou need to copy E, FS and " + mPlans.replaceAll("\\+", "") + "\n");
            new BufferedReader(new InputStreamReader(System.in)).readLine();
            machine.renameAllCkd();
            logger.log("ckd files renamed");
            machine.copyEtiq();
            machine.open4CkdFiles();
            logger.log("ckd files opened");
            tc("/r=\"" + machine.hMachinePath + "\"");
            logger.log("Opened in tc :\n   " + machine.hMachinePath);
        }

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        try {
            getInstance().run(args);
        } catch (Exception e) {
            PrintStream printStream = new PrintStream(LOGERROR);
            e.printStackTrace(printStream);
            getInstance().logger.log("There is an error");
            printStream.close();
        }
        getInstance().logger.stopLogging();
    }


}
