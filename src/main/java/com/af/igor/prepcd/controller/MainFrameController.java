package com.af.igor.prepcd.controller;

import com.af.igor.prepcd.MainApp;
import com.af.igor.prepcd.util.FSHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.af.igor.prepcd.MainApp.getMachine;
import static com.af.igor.prepcd.MainApp.luxParser;

public class MainFrameController {
    MainApp app = MainApp.getInstance();
    @FXML
    private Label machine;

    @FXML
    private Label machineType;

    @FXML
    private Label status;

    @FXML
    private Label target;

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

    private ObservableList<Path> machineDirList;
    private ObservableList<Path> remoteMachineDirList;
    private ObservableList<Path> basePlanDirList;
    private ObservableList<Path> cdDirList;

    private FSHelper machineDirFS;
    private FSHelper remoteMachineDirFS;
    private FSHelper basePlanDirFS;
    private FSHelper cdDirFS;


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
            machineName.setText(getMachine().getMachineName());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSetMachineCode() {
        getMachine().setMachineCode(machineCode.getText());
        status.setText("Machine code is " + machineCode.getText() + " saved");
    }

    @FXML
    private void handleInstall() {
        target.setText("Install");
    }

    @FXML
    private void handleXls() {
        target.setText("Xls");
    }

    @FXML
    private void handleMachine() {
        target.setText("Machine");
    }

    @FXML
    private void handleCD() {
        target.setText("CD");
    }


    public void machineInit(String machineName) throws IOException, InterruptedException {
        app.initializeMachine(machineName);
        status.setText("Copying " + getMachine().getLuxFile());

        getMachine().copyLuxFile();
        luxParser.setExcelFile(getMachine().getMachineDir().getMachinePath() + getMachine().getLuxFile());
        getMachine().setMachineType(luxParser.getMachineType());
        machineType.setText(getMachine().getMachineType());
        machineCode.setText(app.getMachineCode());

        machineDirFS.getFiles(Paths.get(getMachine().getMachineDir().getMachinePath()));
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
                    Path selectedFile=remoteMachineDir.getSelectionModel().getSelectedItem();
                    remoteMachineDirFS.getFiles(selectedFile);
//                    if (selectedFile.startsWith(getMachine().getMachineName()))
//                        getMachine().setRemoteMachinePath(remoteMachineDirFS.getCurrentPath());
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
}
