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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

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
    private Button copyThere;

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
    String installationName;

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
        machineDirFS.getFiles(Paths.get(getMachine().getMachineDir().getMachinePathString()));
        remoteMachineDirFS.getFiles(Paths.get(getMachine().getI_PLANS()));
        status.setText("Copy base installation drawing for " + getMachine().getMachineType());
        copyHere.setDisable(false);
        while (!copyHere.isDisable()) {
        }
        hostServices.showDocument(getMachine().getMachineDir().getMachinePathString() + installationName);
        remoteMachineDirFS.getFiles(Paths.get(getMachine().getRemoteMachinePath()));
    }

    @FXML
    private void handleXls() throws IOException {
        target.setText("Xls");
        if (!getMachine().getMachineXls())
            hostServices.showDocument(getMachine().getMachineDir().getMachinePathString() + getMachine().getMachineDir().getMachineXls());
        else {
            hostServices.showDocument(getMachine().getMachineDir().getMachinePathString() + getMachine().getMachineDir().getMachineXls());
            hostServices.showDocument(getMachine().getMachineDir().getMachinePathString() + getMachine().getLuxFileName());
        }

    }

    @FXML
    private void handleMachine() throws IOException {
        target.setText("Machine");
        if (!Files.exists(Paths.get(getMachine().getMachineDir().getMachinePathString() + getMachine().getMachineDir().getMachineXls()))) {
            status.setText("You haven't xls file yet! You could correct this now");
            handleXls();
            status.setText("You can continue process machine just pressing Machine button");
        } else {
            cdDirFS.getFiles(Paths.get(getMachine().getMachineDir().getMachinePathString()));
            basePlanDirFS.getFiles(Paths.get(app.PLANS));
            remoteMachineDirFS.getFiles(Paths.get(getMachine().getRemoteMachinePath()));

            app.initMachineExcelParser();
            String mPlans = machineExcelParser.getMPlans();
            ArrayList<String> machinePlansList = new ArrayList<>();

            machinePlansList.add("\tI" + app.getMachineCode());
            machinePlansList.add("\tE" + app.getMachineCode());
            machinePlansList.add("\tFS" + app.getMachineCode());

            for (String s : mPlans.split(",")) {
                machinePlansList.add("\t" + s.trim());
            }

            machineDirFS.getFiles(machinePlansList);
        }

        status.setText("Ready");
    }

    @FXML
    private void handleCD() {
        target.setText("CD");
    }

    @FXML
    private void handleCopyHere() throws IOException {
        Path sourceInstFile = remoteMachineDir.getSelectionModel().getSelectedItem();
        Path targetInstFile = Paths.get(getMachine().getMachineDir().getMachinePathString() + installationName);
        Files.copy(sourceInstFile, targetInstFile);
        copyHere.setDisable(true);
    }


    public void machineInit(String machineName) throws IOException, InterruptedException {
        app.initializeMachine(machineName);
        status.setText("Copying " + getMachine().getLuxFileName());

        getMachine().copyLuxFile();
        app.initLuxParser();
        getMachine().setMachineType(luxParser.getMachineType());
        machineType.setText(getMachine().getMachineType());
        machineCode.setText(app.getMachineCode());
        installationName = "I" + getMachine().getMachineCode() + "-" + getMachine().getMachineName().substring(2) + ".ckd";

        machineDirFS.getFiles(Paths.get(getMachine().getMachineDir().getMachinePathString()));
//        machineDir.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                status.setText(String.valueOf(newValue));
//            }
//        });

        remoteMachineDirFS.getFiles(Paths.get(getMachine().getRemoteMachinePath()));
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

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Choose file");
        alert.setHeaderText("Attention! There are " + list.size() + " files for this machine\n" +
                "Choose the correct file please");
        ListView<String> listView = new ListView<>();
        listView.setItems(FXCollections.observableArrayList(list));
        listView.setPrefHeight(120);
        alert.getDialogPane().setContent(listView);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK)
            fileName = list.get(listView.getSelectionModel().getSelectedIndex());

        return fileName;
    }
}
