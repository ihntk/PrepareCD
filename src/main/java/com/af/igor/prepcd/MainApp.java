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
    public static String H_MACHINES;
    private static String CDS;
    public static String CDTEMPLATE;
    public static String PLANS;
    static String TOTALCOMMANDER;
    private static String PRINTDIR;
    private static String CDCOMMENCE;
    public static String DRAWINGS_DIR;

    private static MainApp instance;
    private static String version = "0.6.7";
    private static Machine machine;         //in future this field will replace static ArrayList<Machine>
    public static LuxParser luxParser;
    public static MachineExcelParser machineExcelParser;
    static Desktop desktop = Desktop.getDesktop();
    public SimpleLogger logger;
    private Properties properties = new Properties();
    private static final String PROP_FILE = System.getProperty("user.home") + "/.PrepareCD/PrepareCD.conf";
    private static PrepareCD gui = null;

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
        CDTEMPLATE = properties.getProperty("CDTEMPLATE");
        CDCOMMENCE = properties.getProperty("CDCOMMENCE");
        PLANS = properties.getProperty("PLANS");
        TOTALCOMMANDER = properties.getProperty("TOTALCOMMANDER");
        PRINTDIR = properties.getProperty("PRINTDIR");
        DRAWINGS_DIR = properties.getProperty("DRAWINGS_DIR");
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

    public static String getPropFile() {
        return PROP_FILE;
    }

    public static PrepareCD getGui() {
        return gui;
    }

    public static void setGui(PrepareCD gui) {
        MainApp.gui = gui;
    }

    public static Machine getMachine() {
        return machine;
    }

    public void initializeMachine(String machineName) throws IOException, InterruptedException {
        if (machineName == null) initializePath(machineName);

        machine = new Machine(machineName);
        logger.log("machine name is: " + machineName);
    }

    private void initializePath(String machineName) throws IOException, InterruptedException {
        /*
        Use current dir as path to machine
         */

        Path path = Paths.get("").toAbsolutePath();
        if (!path.toString().startsWith(MACHINES)) {
            ConsoleHelper.writeMessage("\nYou are not in \"plans\" directory");
            logger.log("You are not in \"plans\" directory");
            getInstance().help();
            return;
        }
        machineName = path.getFileName().toString().toUpperCase();
        logger.log("path got from pwd");
    }

    private void help() throws InterruptedException {
        ConsoleHelper.writeMessage("_________________\n" +
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
                if (gui == null) {
                    desktop.open(new File(path));
                    ConsoleHelper.writeMessage("Attention! There are " + count + " files for this machine");

                    for (int i = 0; i < count; i++) {
                        ConsoleHelper.writeMessage("   " + (i + 1) + " - " + fileNames.get(i));
                    }

                    while (true) {
                        ConsoleHelper.writeMessage("choose number of correct file ");
                        try {
                            int number = Integer.parseInt(ConsoleHelper.readString()) - 1;
                            fileName = fileNames.get(number);
                            break;
                        } catch (ArrayIndexOutOfBoundsException e) {
                            ConsoleHelper.writeMessage("You input incorrect number, please try again");
                        }
                    }
                } else {
                    fileName = gui.getController().processChooseFile(fileNames);
                }
            } else fileName = fileNames.get(0);
        } catch (Exception e) {
            logger.log("Error in mainApp.searchFileName\n   path is: " + path + "\n     pattern is: " + pattern + "\n");
            logger.log(e.toString());
            e.printStackTrace();
        }
        return fileName;
    }

    /**
     * tc("--l=path/to/dir/")
     * parameter change /l=path\to\dir\
     *
     * @param pathParameters
     * @throws IOException
     */
    public void tc(String pathParameters) throws IOException {
        pathParameters = pathParameters.replaceAll("/", "\\\\").replaceAll("--", "/");
        Runtime runtime = Runtime.getRuntime();
        String command = TOTALCOMMANDER + " /O " + pathParameters;
        Process process = runtime.exec(command);
        logger.log("total commander got parameters:\n   " + command);
    }

    public void openPrintDir() throws IOException {
        Path printDir = Paths.get(PRINTDIR, machine.getMachineName());
        if (!Files.exists(printDir))
            Files.createDirectory(printDir);

        tc("--t --r=\"" + printDir + "\"");
    }


    public String getCdsString() throws IOException {
        String cd = CDS + machine.getMachineName() + "/";
        if (!Files.exists(Paths.get(cd)))
            Files.createDirectory(Paths.get(cd));
        return cd;
    }

    public String getCdCommenceString() throws IOException {
        String cdCom = CDCOMMENCE + machine.getMachineName() + "/";
        if (!Files.exists(Paths.get(cdCom)))
            Files.createDirectory(Paths.get(cdCom));
        return cdCom;
    }

    public String getMachineCode() {
        String machineType = null;
        String machineCode;
        if (machine.getMachineCode() == null) {
            if (machine.getMachineType().contains("-"))
                machineType = machine.getMachineType().substring(0, machine.getMachineType().indexOf("-"));
            else machineType = machine.getMachineType();

            try {
                machineCode = MachinesCode.valueOf(machineType).toString();
            } catch (IllegalArgumentException e) {
                ConsoleHelper.writeMessage("Input machine's code");
                machineCode = ConsoleHelper.readString();
            }
        } else machineCode = machine.getMachineCode();

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
                ConsoleHelper.writeMessage(getVersion());
                logger.log("Showed version");
                return;
            }
        }

        /*
        start GUI
         */
        for (int i = 0; i < args.length; i++) {
            String flag = args[i];
            if (flag.equals("-g")) {
                logger.log("GUI started");
                PrepareCD.main(args);
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
                ConsoleHelper.writeMessage("\nYou must specify the filename after -n flag");
                help();
                logger.log("Machine was not specify");
                return;
            }
        }

        initializeMachine(machineName);
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
            ConsoleHelper.writeMessage("\nYou must specify target. Application wont to know what to do");
            tc("--l=\"" + machine.machineDir.machinePath + "\"");
            help();
        }
        if (use == 1)
            xlsTarget();

        if (use == 4) {
            logger.log("target is: installation");
            machine.openLuxFile();
            initLuxParser();
            tc("--l=\"" + machine.machineDir.machinePath + "\" --t --r=\"" + machine.I_PLANS + "\"");
            logger.log("Opened in tc: \n   " + machine.machineDir.machinePath + "\n   " + machine.I_PLANS);
            machine.setMachineType(luxParser.getMachineType());
            ConsoleHelper.writeMessage("Copy base installation drawing for " + machine.getMachineType() + "\nand then press enter");
            ConsoleHelper.readString();
            String installationName = "I" + getMachineCode() + "-" + machine.getMachineName().substring(2) + ".ckd";
            Files.move(Paths.get(machine.machineDir.machinePath + machine.machineDir.getCkdFiles().get(0)), Paths.get(machine.machineDir.machinePath + installationName));  //rename installation
            desktop.open(new File(machine.machineDir.machinePath + installationName));
            logger.log("Installation " + installationName + " opened");
            tc("--r=\"" + machine.getRemoteMachinePath() + "\"");
            logger.log("Opened in tc :\n   " + machine.getRemoteMachinePath());
        }

        if (use == 8) {
            logger.log("target is: machine");
            if (!Files.exists(Paths.get(machine.machineDir.machinePath + machine.machineDir.machineXls)))
                xlsTarget();
            tc("--l=\"" + machine.machineDir.machinePath + "\" --t --r=\"" + PLANS + "\"");
            logger.log("Opened in tc: \n   " + machine.machineDir.machinePath + "\n   " + PLANS);
            initMachineExcelParser();
            initLuxParser();
            String machineType = luxParser.getMachineType();
            machine.setMachineType(machineType);
            String mPlans = machineExcelParser.getMPlans();
            logger.log(mPlans);
            ConsoleHelper.writeMessage("Copy base drawings and then press enter\n------\nFor machine " + machineType + "\nYou need to copy E, FS and " + mPlans.replaceAll("\\+", "") + "\n");
            ConsoleHelper.readString();
            machine.renameAllCkd();
            logger.log("ckd files renamed");
            machine.copyEtiq();
            machine.open4CkdFiles();
            logger.log("ckd files opened");
            tc("--r=\"" + machine.getRemoteMachinePath() + "\"");
            logger.log("Opened in tc :\n   " + machine.getRemoteMachinePath());
        }

    }

    private void xlsTarget() throws IOException {
        logger.log("target is: xls");
        tc("--l=\"" + machine.machineDir.machinePath + "\" --t --r=\"" + machine.getRemoteMachinePath() + "\"");
        if (!machine.getMachineXls()) {
            desktop.open(new File(machine.machineDir.machinePath + machine.machineDir.machineXls));
            logger.log("xls is opened");
        } else {
            initLuxParser();
            machineExcelParser.setExcelFile(machine.machineDir.machinePath + machine.getXls());
            machine.setMachineType(luxParser.getMachineType());
            luxParser.getMachineData();
//                machineExcelParser.setMachineType(machine.getMachineType());

            desktop.open(new File(machine.machineDir.machinePath + machine.machineDir.machineXls));
            logger.log("xls is opened");
            desktop.open(new File(machine.machineDir.machinePath + machine.getLuxFileName()));
            logger.log("lux is opened");
        }
    }

    public void initLuxParser() {
        luxParser.setExcelFile(machine.machineDir.machinePath + machine.getLuxFileName());
    }

    public void initMachineExcelParser() {
        machineExcelParser.setExcelFile(machine.machineDir.machinePath + machine.getXls());
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
