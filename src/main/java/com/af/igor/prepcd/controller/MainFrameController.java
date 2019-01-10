package com.af.igor.prepcd.controller;

import com.af.igor.prepcd.MainApp;
import com.af.igor.prepcd.PrepareCD;
import com.af.igor.prepcd.util.FSHelper;
import javafx.application.HostServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.af.igor.prepcd.MainApp.getMachine;
import static com.af.igor.prepcd.MainApp.luxParser;

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
    private ListView<String> basePlanDir;

    @FXML
    private ListView<String> cdDir;

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
    }

    private ObservableList<Path> machineDirList;
    private ObservableList<Path> remoteMachineDirList;
    private ObservableList<Path> basePlanDirList;
    private ObservableList<Path> cdDirList;

    private FSHelper machineDirFS;
    private FSHelper remoteMachineDirFS;
    private FSHelper basePlanDirFS;
    private FSHelper cdDirFS;

    private String machinePathString = getMachine().getMachineDir().getMachinePathString();
    private HostServices hostServices = application.getHostServices();
    String installationName = "I" + getMachine().getMachineCode() + "-" + getMachine().getMachineName().substring(2) + ".ckd";

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
        machineDirFS.getFiles(Paths.get(machinePathString));
        remoteMachineDirFS.getFiles(Paths.get(getMachine().getI_PLANS()));
        status.setText("Copy base installation drawing for " + getMachine().getMachineType());
        copyHere.setDisable(false);
        while (!copyHere.isDisable()) {
        }
        hostServices.showDocument(machinePathString + installationName);
        remoteMachineDirFS.getFiles(Paths.get(getMachine().getRemoteMachinePath()));
    }

    @FXML
    private void handleXls() throws IOException {
        target.setText("Xls");
        if (!getMachine().getMachineXls())
            hostServices.showDocument(getMachine().getMachineDir().getMachinePathString()+getMachine().getMachineDir().getMachineXls());
        else{
            hostServices.showDocument(getMachine().getMachineDir().getMachinePathString()+getMachine().getMachineDir().getMachineXls());
            hostServices.showDocument(getMachine().getMachineDir().getMachinePathString()+ getMachine().getLuxFileName());
        }

    }

    @FXML
    private void handleMachine() {
        target.setText("Machine");
    }

    @FXML
    private void handleCD() {
        target.setText("CD");
    }

    @FXML
    private void handleCopyHere() throws IOException {
        Path sourceInstFile = remoteMachineDir.getSelectionModel().getSelectedItem();
        Path targetInstFile = Paths.get(machinePathString + installationName);
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

        machineDirFS.getFiles(Paths.get(machinePathString));
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
        remoteMachineDirFS.getFiles(list);
        final int[] index = new int[1];
        remoteMachineDir.setOnMouseClicked(event -> index[0] = remoteMachineDir.getSelectionModel().getSelectedIndex());
        String fileName = list.get(index[0]);
        return fileName;
    }
}
