package com.af.igor.prepcd;

import com.af.igor.prepcd.util.AdditionalOptions;
import com.af.igor.prepcd.util.ConsoleHelper;

import java.io.*;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Created by ede on 11.08.2016.
 */
public class Machine {
    protected final String luxFileName;
    MainApp app = MainApp.getInstance();
    private final String machineName;
    private String machineType;
    private final String sm;        //first two symbols in machine name (usually 20)
    private final String machineSeries;
    private String remoteMachinePath;
    private Path confFile;
    private Properties confFileProperty = new Properties();
    private String machineCodePure;
    private String machineCode;
    private HashMap<String, String> propertyElements = new HashMap<>();
    private String luxPathString;

    MachineDir machineDir;
    private AdditionalOptions additionalOption;

    public MachineDir getMachineDir() {
        return machineDir;
    }

    public String getRemoteMachinePath() {
        return remoteMachinePath;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public AdditionalOptions getAdditionalOption() {
        return additionalOption;
    }

    public String getLuxFileName() {
        return luxFileName;
    }

    public String getMachineType() {
        return machineType;
    }

    public String getMachineName() {
        return machineName;
    }

    public String getMachineSeries() {
        return machineSeries;
    }

    public String getSm() {
        return sm;
    }

    public boolean setRemoteMachinePath(String remoteMachinePath) {
        this.remoteMachinePath = remoteMachinePath;
        propertyElements.put("remoteMachinePath", this.remoteMachinePath);
        return saveConfigFile();
    }

    public boolean setMachineCode(String machineCodePure) {
        if (machineCodePure.equals("")) {
            machineCode = null;
            this.machineCodePure = app.getMachineCode();
        } else
            this.machineCodePure = machineCodePure;                            // if null, app.getMachineCode() calculates right code

        propertyElements.put("machineCodePure", machineCodePure);               // and save null config file
        calculateMachineCode();
        return saveConfigFile();
    }

    public void setAdditionalOption(AdditionalOptions option) {
        additionalOption = option;
        propertyElements.put("additionalOption", option.getText());
        calculateMachineCode();
        saveConfigFile();
    }

    public Machine(String machineName) throws IOException {
        this.machineName = machineName;
        additionalOption = AdditionalOptions.BASE_FRAME;
        remoteMachinePath = app.H_MACHINES + machineName + "/";

        sm = machineName.substring(0, 2);
        machineSeries = machineName.substring(2, 3).equals("Y") ? machineName.substring(2, 3) : machineName.substring(2, 4);

        luxPathString = remoteMachinePath + "010 Order/";
        luxFileName = app.searchFileNameStartWith(luxPathString, machineName);
        if (luxFileName == null) {
            app.logger.log("Program can't find luxemburg file\nluxPathString is:\n   " + luxPathString);
            app.logger.log("machineName is:\n   " + machineName);
            app.desktop.open(new File(luxPathString));
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                ConsoleHelper.writeMessage("Are you sure the machine " + machineName + " is exist? (y/N)");
                if (!reader.readLine().toLowerCase().equals("y")) machineName = null;
            }
        }

        if (machineName != null) {
            machineDir = new MachineDir(this);

            confFile = Paths.get(machineDir.getMachinePathString(), machineName + ".conf");
            loadFromConfigFile();
        }

    }

    public void initMachineCode() {
        if (machineCodePure == null)
            machineCodePure = app.getMachineCode();

        calculateMachineCode();
    }

    private void calculateMachineCode() {
        machineCode = String.valueOf(Integer.parseInt(machineCodePure) + additionalOption.getIncrement());
    }

    private void loadFromConfigFile() {
        if (!Files.exists(confFile))
            return;

        try (FileInputStream stream = new FileInputStream(confFile.toFile())) {
            Properties confFileProperty = new Properties();

            confFileProperty.load(stream);
            if (confFileProperty.containsKey("remoteMachinePath")) {
                remoteMachinePath = confFileProperty.getProperty("remoteMachinePath");
                propertyElements.put("remoteMachinePath", remoteMachinePath);
            }
            if (confFileProperty.containsKey("machineCodePure")) {
                machineCode = confFileProperty.getProperty("machineCodePure");
                propertyElements.put("machineCodePure", machineCodePure);
            }
            if (confFileProperty.containsKey("surbaise")) {
                if (Boolean.parseBoolean(confFileProperty.getProperty("surbaise"))) {
                    additionalOption = AdditionalOptions.SURBAISE;
                    propertyElements.put("additionalOption", additionalOption.getText());
                }
            }
            if (confFileProperty.containsKey("additionalOption")) {
                additionalOption = AdditionalOptions.valueOf(confFileProperty.getProperty("additionalOption"));
                propertyElements.put("additionalOption", additionalOption.getText());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean saveConfigFile() {
        boolean result = false;

        if (!Files.exists(confFile)) {
            try {
                Files.createFile(confFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (FileOutputStream stream = new FileOutputStream(confFile.toFile())) {
            for (Map.Entry<String, String> element : propertyElements.entrySet()) {
                if (element.getValue().equals(""))
                    confFileProperty.remove(element.getKey());
                else
                    confFileProperty.setProperty(element.getKey(), element.getValue());
            }
            confFileProperty.store(stream, null);
            result = true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void setMachineType(String machineType) {
        this.machineType = machineType;
        app.logger.log("Machine type is: " + this.machineType);
    }


    /*
    getMachineXls() check machineXls exist, if false, copy base machineXls
     */
    public boolean getMachineXls() throws IOException {
        boolean isXlsCreated = false;
        if (!new File(machineDir.machinePath + machineDir.machineXls).exists()) {
            copyXls();
            isXlsCreated = true;
        }
        app.logger.log("isXlsCreated is: " + isXlsCreated);
        return isXlsCreated;
    }

    public void openLuxFile() throws IOException {
        copyLuxFile();
        app.desktop.open(new File(machineDir.machinePath + luxFileName));
    }

    public String getXls() {
        return machineDir.machineXls;
    }

    public void renameAllCkd() throws IOException {
        String machineCode = app.getMachineCode();
        ArrayList<String> ckdFiles = machineDir.getCkdFiles();
        for (String file : ckdFiles) {
            if ((file.startsWith("E")) && (!file.startsWith("Etiqclas"))) {
                try {
                    if (Integer.parseInt(file.substring(1, file.indexOf("-"))) >= 700) {
                        String renamedCkd = "E" + machineCode + "-" + getMachineName().substring(2) + ".ckd";
                        renameInLocalDir(file, renamedCkd);
                    }
                } catch (Exception e) {
                }

            }
            if (file.startsWith("FS")) {
                String renamedCkd = "FS" + machineCode + "-" + getMachineName().substring(2) + ".ckd";
                renameInLocalDir(file, renamedCkd);
            }
            if (file.startsWith("I")) {
                String renamedCkd = "I" + machineCode + "-" + getMachineName().substring(2) + ".ckd";
                renameInLocalDir(file, renamedCkd);
            }

            if (file.startsWith("M10") &&
                    !file.startsWith("M100")) {
                String renamedCkd = "M10" + "-" + getMachineName().substring(2) + ".ckd";
                renameInLocalDir(file, renamedCkd);
            }
            if (file.startsWith("M20")) {
                String renamedCkd = "M20" + "-" + getMachineName().substring(2) + ".ckd";
                renameInLocalDir(file, renamedCkd);
            }
            if (file.startsWith("M30")) {
                String renamedCkd = "M30" + "-" + getMachineName().substring(2) + ".ckd";
                renameInLocalDir(file, renamedCkd);
            }
            if (file.startsWith("M40")) {
                String renamedCkd = "M40" + "-" + getMachineName().substring(2) + ".ckd";
                renameInLocalDir(file, renamedCkd);
            }
            if (file.startsWith("M50")) {
                String renamedCkd = "M50" + "-" + getMachineName().substring(2) + ".ckd";
                renameInLocalDir(file, renamedCkd);
            }
            if (file.startsWith("M60")) {
                String renamedCkd = "M60" + "-" + getMachineName().substring(2) + ".ckd";
                renameInLocalDir(file, renamedCkd);
            }
            if (file.startsWith("M70")) {
                String renamedCkd = "M70" + "-" + getMachineName().substring(2) + ".ckd";
                renameInLocalDir(file, renamedCkd);
            }
            if (file.startsWith("M80")) {
                String renamedCkd = "M80" + "-" + getMachineName().substring(2) + ".ckd";
                renameInLocalDir(file, renamedCkd);
            }
            if (file.startsWith("M90")) {
                String renamedCkd = "M90" + "-" + getMachineName().substring(2) + ".ckd";
                renameInLocalDir(file, renamedCkd);
            }
            if (file.startsWith("M100")) {
                String renamedCkd = "M100" + "-" + getMachineName().substring(2) + ".ckd";
                renameInLocalDir(file, renamedCkd);
            }
            if (file.startsWith("M110")) {
                String renamedCkd = "M110" + "-" + getMachineName().substring(2) + ".ckd";
                renameInLocalDir(file, renamedCkd);
            }

        }
    }

    public void open4CkdFiles() throws IOException {
        ArrayList<String> ckdFiles = machineDir.getCkdFiles();
        for (String file : ckdFiles) {
            if (file.startsWith("FS"))
                app.desktop.open(new File(machineDir.machinePath + file));
            if (file.startsWith("Etiqclas"))
                app.desktop.open(new File(machineDir.machinePath + file));
            if ((file.startsWith("E")) && (!file.startsWith("Etiqclas")))
                app.desktop.open(new File(machineDir.machinePath + file));
            if (file.startsWith("I"))
                app.desktop.open(new File(machineDir.machinePath + file));
        }
    }

    public void openMCkdFiles() throws IOException {
        ArrayList<String> ckdFiles = machineDir.getCkdFiles();
        for (String file : ckdFiles) {
            if (file.startsWith("M"))
                app.desktop.open(new File(machineDir.machinePath + file));
        }
    }

    public void copyEtiq() throws IOException {
        String machineEtiq = "Etiqclas" + "-" + getMachineName().substring(2) + ".ckd";
        if (!new File(machineDir.machinePath + machineEtiq).exists())
            app.copy(app.ETIQCLAS, machineDir.machinePath + machineEtiq);
    }

    protected void copyXls() throws IOException {
        app.copy(app.XLS, machineDir.machinePath + machineDir.machineXls);
        app.logger.log("Copied xls");

        copyLuxFile();
    }

    public void copyLuxFile() throws IOException {
        if (luxFileName != null) {
            try {
                app.copy(luxPathString + luxFileName, machineDir.machinePath + luxFileName, REPLACE_EXISTING);
                app.logger.log("Copied luxFileName:\n   " + luxFileName);
            } catch (FileSystemException e) {
                ConsoleHelper.writeMessage("I can't replace " + luxFileName + " file because it is being used by another process");
                app.logger.log("Could'nt replace:\n   " + luxFileName);
            }
        }
    }

    public void renameInLocalDir(String sourceName, String targetName) throws IOException {
        app.rename(machineDir.machinePath + sourceName, machineDir.machinePath + targetName);
    }

    public String defineFileName(Path selectedItem) {
        String machineCode = app.getMachineCode();
        String item = selectedItem.getFileName().toString().trim();
        String fileName = null;

        if (item.startsWith("I")) {
            fileName = "I" + machineCode + "-" + getMachineName().substring(2) + ".ckd";
        }
        if ((item.startsWith("E")) && (!item.startsWith("Etiqclas"))) {
            fileName = "E" + machineCode + "-" + getMachineName().substring(2) + ".ckd";
        }
        if (item.startsWith("FS")) {
            fileName = "FS" + machineCode + "-" + getMachineName().substring(2) + ".ckd";
        }

        if (item.startsWith("M10") &&
                !item.startsWith("M100")) {
            fileName = "M10" + "-" + getMachineName().substring(2) + ".ckd";
        }
        if (item.startsWith("M20")) {
            fileName = "M20" + "-" + getMachineName().substring(2) + ".ckd";
        }
        if (item.startsWith("M30")) {
            fileName = "M30" + "-" + getMachineName().substring(2) + ".ckd";
        }
        if (item.startsWith("M40")) {
            fileName = "M40" + "-" + getMachineName().substring(2) + ".ckd";
        }
        if (item.startsWith("M50")) {
            fileName = "M50" + "-" + getMachineName().substring(2) + ".ckd";
        }
        if (item.startsWith("M60")) {
            fileName = "M60" + "-" + getMachineName().substring(2) + ".ckd";
        }
        if (item.startsWith("M70")) {
            fileName = "M70" + "-" + getMachineName().substring(2) + ".ckd";
        }
        if (item.startsWith("M80")) {
            fileName = "M80" + "-" + getMachineName().substring(2) + ".ckd";
        }
        if (item.startsWith("M90")) {
            fileName = "M90" + "-" + getMachineName().substring(2) + ".ckd";
        }
        if (item.startsWith("M100")) {
            fileName = "M100" + "-" + getMachineName().substring(2) + ".ckd";
        }
        if (item.startsWith("M110")) {
            fileName = "M110" + "-" + getMachineName().substring(2) + ".ckd";
        }

        return fileName;
    }


}
