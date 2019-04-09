package com.af.igor.prepcd.controller;

import com.af.igor.prepcd.MainApp;
import com.af.igor.prepcd.PrepareCD;
import com.af.igor.prepcd.util.FSHelper;
import javafx.application.HostServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.af.igor.prepcd.MainApp.*;

public class MainFrameController {
    private MainApp app = MainApp.getInstance();
    private PrepareCD application;

    @FXML
    private Label machine;

    @FXML
    private Label machineType;

    @FXML
    private Label status;

    @FXML
    private Label target;

    @FXML
    private Label currentMachine;

    @FXML
    private TextField machineName;

    @FXML
    private TextField machineCode;

    @FXML
    private ListView<Path> machineDir;

    @FXML
    private ListView<Path> remoteMachineDir;

    @FXML
    private ListView<Path> basePlanDir;

    @FXML
    private ListView<Path> cdDir;

    @FXML
    private Button installButton;

    @FXML
    private Button xlsButton;

    @FXML
    private Button machineButton;

    @FXML
    private Button cdButton;

    @FXML
    private Button copyHere;

    @FXML
    private Button ok;

    @FXML
    private Button f1Button;

    @FXML
    private Button f4Button;

    public void setApplication(PrepareCD application) {
        this.application = application;
        hostServices = application.getHostServices();
    }

    private ObservableList<Path> machineDirList;
    private ObservableList<Path> remoteMachineDirList;
    private ObservableList<Path> basePlanDirList;
    private ObservableList<Path> cdDirList;

    private FSHelper machineDirFS;
    private FSHelper remoteMachineDirFS;
    private FSHelper basePlanDirFS;
    private FSHelper cdDirFS;

    private HostServices hostServices;
    private String installationName;
    private String targetFileName;
    private Targets currenttarget;

    public MainFrameController() {
    }

    @FXML
    private void initialize() {
        target.setText("Ready");
        status.setText("Select the machine");

        machineDirList = FXCollections.observableArrayList();
        machineDir.setItems(machineDirList);
        machineDirFS = FSHelper.getInstance(machineDirList);

        remoteMachineDirList = FXCollections.observableArrayList();
        remoteMachineDir.setItems(remoteMachineDirList);
        remoteMachineDirFS = FSHelper.getInstance(remoteMachineDirList);

        basePlanDirList = FXCollections.observableArrayList();
        basePlanDir.setItems(basePlanDirList);
        basePlanDirFS = FSHelper.getInstance(basePlanDirList);

        cdDirList = FXCollections.observableArrayList();
        cdDir.setItems(cdDirList);
        cdDirFS = FSHelper.getInstance(cdDirList);
    }

    @FXML
    private void handleSetMachine() {
        try {
            target.setText("Processing machine");
            status.setText("Waiting");
            machineInit(machineName.getText().toUpperCase());
            status.setText("Machine is " + getMachine().getMachineName());
            target.setText("Ready");
            application.setTitle(getMachine().getMachineName());
            currentMachine.setText(getMachine().getMachineName());
            machineName.setText(getMachine().getMachineName());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSetMachineCode() {
        if (getMachine().setMachineCode(machineCode.getText()))
            status.setText("Machine code " + machineCode.getText() + " is saved");
        else status.setText("Impossible to save machine code " + machineCode.getText());
        machineCode.setText(app.getMachineCode());
    }

    @FXML
    private void handleInstall() throws IOException {
        target.setText("Install");
        app.logger.log("target is: installation");
        resetControlsDefault();
        machineDirFS.getFiles(Paths.get(getMachine().getMachineDir().getMachinePathString()));
        remoteMachineDirFS.getFiles(Paths.get(getMachine().getI_PLANS()));

        targetFileName = installationName;
        currenttarget = Targets.INSTALL;
        copyHere.setDisable(false);
        ok.setDisable(false);
        status.setText("Copy base installation drawing for " + getMachine().getMachineType() + ". Then press ok");
        app.tc("--l=\"" + getMachine().getMachineDir().getMachinePathString() + "\" --t --r=\"" + getMachine().getRemoteMachinePath() + "\"");
    }

    @FXML
    private void handleXls() throws IOException {
        target.setText("Xls");
        app.logger.log("target is: xls");
        resetControlsDefault();
        if (!getMachine().getMachineXls())
            hostServices.showDocument(getMachine().getMachineDir().getMachinePathString() + getMachine().getMachineDir().getMachineXls());
        else {
            hostServices.showDocument(getMachine().getMachineDir().getMachinePathString() + getMachine().getMachineDir().getMachineXls());
            hostServices.showDocument(getMachine().getMachineDir().getMachinePathString() + getMachine().getLuxFileName());
        }
        status.setText("You can processing xls file");
        currenttarget = Targets.XLS;
        endCurrentTarget();
    }

    @FXML
    private void handleMachine() throws IOException {
        target.setText("Machine");
        app.logger.log("target is: machine");
        resetControlsDefault();
        if (!Files.exists(Paths.get(getMachine().getMachineDir().getMachinePathString() + getMachine().getMachineDir().getMachineXls()))) {
            status.setText("You haven't xls file yet! You could correct this now");
            handleXls();
            status.setText("You can continue process machine just pressing Machine button");
        } else {
            basePlanDirFS.getFiles(Paths.get(getMachine().getMachineDir().getMachinePathString()));
            remoteMachineDirFS.getFiles(Paths.get(app.PLANS));
            cdDirFS.getFiles(Paths.get(getMachine().getRemoteMachinePath()));

            refreshMachinePlansList();

            cdDir.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    try {
                        Path selectedFile = cdDir.getSelectionModel().getSelectedItem();
                        cdDirFS.getFiles(selectedFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });

            targetFileName = null;
            currenttarget = Targets.MACHINE;
            application.getRootLayoutController().enableRenameAllCkdFiles();
            copyHere.setDisable(false);
            ok.setDisable(false);
            status.setText("Copy base drawings for " + getMachine().getMachineType() + ". Then press ok");
            app.tc("--l=\"" + getMachine().getMachineDir().getMachinePathString() + "\" --t --r=\"" + PLANS + "\"");
        }

    }

    @FXML
    private void handleCD() throws IOException {
        target.setText("CD");
        resetControlsDefault();
        app.tc("--l=\"" + app.getCdsString() + "\" --t --r=\"" + app.getCdCommenceString() + "\"");
        currenttarget = Targets.CD;
    }

    @FXML
    private void handleCopyHere() throws IOException {
        if (remoteMachineDir.getSelectionModel().getSelectedIndex() < 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setHeaderText("Source file isn't selected");
            alert.setContentText("Select source file you want to copy (in the right table)");
            alert.showAndWait();
            return;
        }

        Path sourcePath = remoteMachineDirFS.getCurrentPath().resolve(remoteMachineDir.getSelectionModel().getSelectedItem());
        String filename;
        Path targetPath;
        int index = -1;
        if (targetFileName != null) {
            filename = targetFileName;
            targetPath = Paths.get(getMachine().getMachineDir().getMachinePathString() + filename);
        } else {
            index = machineDir.getSelectionModel().getSelectedIndex();
            if (index < 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attention");
                alert.setHeaderText("Target file isn't selected");
                alert.setContentText("Select target file you want to copy (in the left table)");
                alert.showAndWait();
                return;
            }
            filename = getMachine().defineFileName(machineDir.getSelectionModel().getSelectedItem());
            targetPath = Paths.get(getMachine().getMachineDir().getMachinePathString() + filename);
        }

        if (!Files.exists(targetPath)) {
            Files.copy(sourcePath, targetPath);
            if (index > -1)
                machineDirList.set(index, targetPath.getFileName());
            else
                machineDirList.add(targetPath.getFileName());
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Attention");
            alert.setHeaderText("File " + targetPath.getFileName() + " exist");
            alert.setContentText("Do you really want to replace " + targetPath.getFileName() + " file?");
            Optional<ButtonType> optionalButtonType = alert.showAndWait();

            if (optionalButtonType.get() == ButtonType.OK)
                Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            else
                return;
        }
    }

    @FXML
    private void handleOk() throws IOException {
        for (Path item : machineDirList) {
            if (item.getFileName().toString().startsWith("   ")) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Attention");
                alert.setHeaderText("There is uncopied files");
                alert.setContentText("You didn't copy " + getMachine().defineFileName(item) + " file\n"
                        + "Are you sure you want to continue ");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK)
                    break;
                else return;
            }
        }
        copyHere.setDisable(true);
        ok.setDisable(true);
        endCurrentTarget();
    }

    @FXML
    private void handleF1Button() throws IOException {
        if (f1Button.getText().equals("Open M"))
            getMachine().openMCkdFiles();
    }

    @FXML
    private void handleF4Button() throws IOException {
        if (f4Button.getText().equals("Open Print"))
            app.openPrintDir();
    }

    public void machineInit(String machineName) throws IOException, InterruptedException {
        app.logger.log("-----\n");
        if (!Files.exists(Paths.get(app.H_MACHINES + machineName))) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Machine does'nt exists");
            alert.setHeaderText("Machine " + machineName + " does'nt exists");
            alert.setContentText("There is no directory for machine " + machineName + ".\n" +
                    "Make sure you enter correct machine name");
            alert.showAndWait();
            app.logger.log(machineName + " doesn't exists");
            return;
        }
        app.initializeMachine(machineName);
        status.setText("Copying " + getMachine().getLuxFileName());

        getMachine().copyLuxFile();
        app.initLuxParser();
        getMachine().setMachineType(luxParser.getMachineType());
        machineType.setText(getMachine().getMachineType());
        machineCode.setText(app.getMachineCode());
        installationName = "I" + app.getMachineCode() + "-" + getMachine().getMachineName().substring(2) + ".ckd";

        machineDirFS.getFiles(Paths.get(getMachine().getMachineDir().getMachinePathString()));
//        machineDir.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                status.setText(String.valueOf(newValue));
//            }
//        });

        remoteMachineDirFS.getFiles(Paths.get(getMachine().getRemoteMachinePath()));

        installButton.setDisable(false);
        xlsButton.setDisable(false);
        machineButton.setDisable(false);
        cdButton.setDisable(false);
        application.getRootLayoutController().enableMenu();
        resetControlsDefault();

        remoteMachineDir.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    Path selectedFile = remoteMachineDir.getSelectionModel().getSelectedItem();
                    remoteMachineDirFS.getFiles(selectedFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

//        remoteMachineDir.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Path>() {
//            @Override
//            public void changed(ObservableValue<? extends Path> observable, Path oldValue, Path newValue) {
////                if (!newValue.getParent().getFileName().toString().startsWith(getMachine().getMachineName())) {
//                    try {
//                        remoteMachineDirFS.getFiles(newValue);
//                    } catch (IOException e) {
//
//                    }
////                }
//            }
//        });
    }

    public String processChooseFile(List<String> list) throws IOException {
        String fileName = null;
        int index = -1;

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Choose file");
        alert.setHeaderText("Attention! There are " + list.size() + " files for this machine\n" +
                "Choose the correct file please");
        ListView<String> listView = new ListView<>();
        listView.setItems(FXCollections.observableArrayList(list));
        listView.setPrefHeight(120);
        alert.getDialogPane().setContent(listView);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            index = listView.getSelectionModel().getSelectedIndex();
            if (index < 0)
                return processChooseFile(list);
        }

        fileName = list.get(index);
        return fileName;
    }

    protected void refreshMachinePlansList() {
        if (currenttarget != Targets.MACHINE)
            return;

        app.initMachineExcelParser();
        String mPlans = machineExcelParser.getMPlans();
        ArrayList<String> machinePlansList = new ArrayList<>();

        machinePlansList.add("   I" + app.getMachineCode());
        machinePlansList.add("   E" + app.getMachineCode());
        machinePlansList.add("   FS" + app.getMachineCode());

        for (String mPlan : mPlans.split("\\+")) {
            machinePlansList.add("   " + mPlan.trim());
        }
        for (int i = 0; i < machinePlansList.size(); i++) {
            String name = getMachine().defineFileName(Paths.get(machinePlansList.get(i)));
            if (name != null)
                for (String file : getMachine().getMachineDir().getCkdFiles()) {
                    if (file.startsWith(name.substring(0, name.indexOf("."))))
                        machinePlansList.set(i, file);
                }
        }

        machineDirFS.getFiles(machinePlansList);
    }

    private void endCurrentTarget() throws IOException {
        switch (currenttarget) {
            case INSTALL: {
                hostServices.showDocument(getMachine().getMachineDir().getMachinePathString() + getMachine().getLuxFileName());
                hostServices.showDocument(getMachine().getMachineDir().getMachinePathString() + installationName);
                remoteMachineDirFS.getFiles(Paths.get(getMachine().getRemoteMachinePath()));
                status.setText(installationName + " is opened");
            }
            break;

            case MACHINE: {
                getMachine().copyEtiq();
                getMachine().open4CkdFiles();
                status.setText("Ready");
                app.tc("--r=\"" + getMachine().getRemoteMachinePath() + "\"");
                f1Button.setText("Open M");
                f1Button.setDisable(false);

                machineDirFS.getFiles(Paths.get(getMachine().getMachineDir().getMachinePathString()));
                remoteMachineDirFS.getFiles(Paths.get(getMachine().getRemoteMachinePath()));
                basePlanDirList.clear();
                cdDirList.clear();

                f4Button.setText("Open Print");
                f4Button.setDisable(false);
            }
            break;

            case CD:
                break;

            case XLS: {
                app.tc("--l=\"" + getMachine().getMachineDir().getMachinePathString() + "\" --t --r=\"" + getMachine().getRemoteMachinePath() + "\"");
            }
            break;
        }
    }

    private void resetControlsDefault() {
        copyHere.setDisable(true);
        ok.setDisable(true);
        f1Button.setDisable(true);
        f1Button.setText("-");
        f4Button.setDisable(true);
        f4Button.setText("-");
        basePlanDirList.clear();
        cdDirList.clear();
        application.getRootLayoutController().disableRenameAllCkdFiles();
    }

    public enum Targets {
        INSTALL,
        XLS,
        MACHINE,
        CD;
    }

}
