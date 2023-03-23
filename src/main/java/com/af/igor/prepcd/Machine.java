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
    private String luxFileName;
    MainApp app = MainApp.getInstance();
    private final String machineName;
    private String machineType;
    private final String sm;        //first two symbols in machine name (usually 20)
    private final String machineSeries;
    private String machinePathString;
    private String remoteMachinePathString;
    private final File machinePathDir;
    private Path confFile;
    private Properties confFileProperty = new Properties();
    private String machineCodePure;
    private String machineCode;
    private HashMap<String, String> propertyElements = new HashMap<>();
    private String luxPathString;
    private final String machineXlsName;

    private AdditionalOptions additionalOption = AdditionalOptions.BASE_FRAME;


    public String getRemoteMachinePathString() {
        return remoteMachinePathString;
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

    public void setLuxFileName(String luxFileName) {
        this.luxFileName = luxFileName;
    }

    public String getMachineXlsName() {
        return machineXlsName;
    }

    public String getMachinePathString() {
        return machinePathString;
    }

    public String getMachineType() {
        return machineType;
    }

    public String getMachineName() {
        return machineName;
    }

    public String getLuxPathString() {
        return luxPathString;
    }

    public String getMachineSeries() {
        return machineSeries;
    }

    public String getSm() {
        return sm;
    }

    public boolean setRemoteMachinePath(String remoteMachinePath) {
        this.remoteMachinePathString = remoteMachinePath;
        propertyElements.put("remoteMachinePath", this.remoteMachinePathString);
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
        remoteMachinePathString = app.H_MACHINES + machineName + "/";
        sm = machineName.substring(0, 2);
        machineSeries = machineName.substring(2, 3).equals("Y") ? machineName.substring(2, 3) : machineName.substring(2, 4);
        luxPathString = remoteMachinePathString + "010 Order/";
        machineXlsName = machineName + ".xlsx";
        machinePathString = app.MACHINES + machineName + "/";
        confFile = Paths.get(machinePathString, machineName + ".conf");
        machinePathDir = new File(machinePathString);
        machinePathDir.mkdir();
        loadFromConfigFile();
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
                remoteMachinePathString = confFileProperty.getProperty("remoteMachinePath");
                propertyElements.put("remoteMachinePath", remoteMachinePathString);
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

    public boolean copyMachineXlsIfNotExist() throws IOException {
        boolean isXlsCreated = false;
        if (!new File(machinePathString + machineXlsName).exists()) {
            copyXls();
            isXlsCreated = true;
        }
        app.logger.log("isXlsCreated is: " + isXlsCreated);
        return isXlsCreated;
    }

    public void openLuxFile() throws IOException {
        copyLuxFile();
        app.desktop.open(new File(machinePathString + luxFileName));
    }

    public String getXls() {
        return machineXlsName;
    }

    public void renameAllCkd() throws IOException {
        String machineCode = app.getMachineCode();
        ArrayList<String> ckdFiles = getCkdFiles();
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
        ArrayList<String> ckdFiles = getCkdFiles();
        for (String file : ckdFiles) {
            if (file.startsWith("FS"))
                app.desktop.open(new File(machinePathString + file));
            if (file.startsWith("Etiqclas"))
                app.desktop.open(new File(machinePathString + file));
            if ((file.startsWith("E")) && (!file.startsWith("Etiqclas")))
                app.desktop.open(new File(machinePathString + file));
            if (file.startsWith("I"))
                app.desktop.open(new File(machinePathString + file));
        }
    }

    public void openMCkdFiles() throws IOException {
        ArrayList<String> ckdFiles = getCkdFiles();
        for (String file : ckdFiles) {
            if (file.startsWith("M"))
                app.desktop.open(new File(machinePathString + file));
        }
    }

    public void copyEtiq() throws IOException {
        String machineEtiq = "Etiqclas" + "-" + getMachineName().substring(2) + ".ckd";
        if (!new File(machinePathString + machineEtiq).exists())
            app.copy(app.ETIQCLAS, machinePathString + machineEtiq);
    }

    private void copyXls() throws IOException {
        app.copy(app.XLS, machinePathString + machineXlsName);
        app.logger.log("Copied xls");

        copyLuxFile();
    }

    public void copyLuxFile() throws IOException {
        if (luxFileName == null || app.isOfflineMode()) return;

        try {
            app.copy(luxPathString + luxFileName, machinePathString + luxFileName, REPLACE_EXISTING);
            app.logger.log("Copied luxFileName:\n   " + luxFileName);
        } catch (FileSystemException e) {
            ConsoleHelper.writeMessage("I can't replace " + luxFileName + " file because it is being used by another process");
            app.logger.log("Could'nt replace:\n   " + luxFileName);
        }
    }

    public void renameInLocalDir(String sourceName, String targetName) throws IOException {
        app.rename(machinePathString + sourceName, machinePathString + targetName);
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

    public String[] getFiles() {
        return machinePathDir.list();

    }

    public ArrayList<String> getCkdFiles() {
        ArrayList<String> ckd = new ArrayList<>();
        for (String file : getFiles()) {
            if (file.toLowerCase().endsWith(".ckd"))
                ckd.add(file);
        }
        return ckd;
    }

}
