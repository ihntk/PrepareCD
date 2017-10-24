package com.af.igor.prepcd.controller;

import com.af.igor.prepcd.MainApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;

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
    private ListView<String> machineDir;

    @FXML
    private ListView<String> remoteMachineDir;

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



    public MainFrameController() {
    }

    public void initialize(){
        target.setText("Ready");
        status.setText("Select the machine");
    }

    @FXML
    private void handleSetMachine(){
        try {
            target.setText("Processing machine");
            status.setText("Waiting");
            machineInit(machineName.getText().toUpperCase());
            status.setText("Machine is "+ getMachine().getMachineName());
            target.setText("Ready");
            machineName.setText(getMachine().getMachineName());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSetMachineCode(){
        status.setText("Machine code is "+machineCode.getText());
    }

    @FXML
    private void handleInstall(){
        target.setText("Install");
    }

    @FXML
    private void handleXls(){
        target.setText("Xls");
    }

    @FXML
    private void handleMachine(){
        target.setText("Machine");
    }

    @FXML
    private void handleCD(){
        target.setText("CD");
    }


    public void machineInit(String machineName) throws IOException, InterruptedException {
        app.initializeMachine(machineName);
        status.setText("Copying "+getMachine().getLuxFile());
        getMachine().openLuxFile();
        luxParser.setExcelFile(getMachine().getMachineDir().getMachinePath() + getMachine().getLuxFile());
        getMachine().setMachineType(luxParser.getMachineType());
        machineType.setText(getMachine().getMachineType());
        machineCode.setText(app.getMachineCode());
        ObservableList<String> localMachineFiles = FXCollections.observableArrayList(getMachine().getMachineDir().getFiles());
        machineDir.setItems(localMachineFiles);
//        ObservableList<String> remoteMachineFiles = FXCollections.observableArrayList(getMachine().gethMachinePath())
    }
}
