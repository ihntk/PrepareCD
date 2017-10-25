package com.af.igor.prepcd.controller;

import com.af.igor.prepcd.MainApp;
import com.af.igor.prepcd.util.FSHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

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

    private FSHelper machineDirFS;
    private FSHelper remoteMachineDirFS;
    private FSHelper basePlanDirFS;
    private FSHelper cdDirFS;



    public MainFrameController() {
    }

    public void initialize(){
        target.setText("Ready");
        status.setText("Select the machine");

        machineDirFS=FSHelper.getInstance();
        remoteMachineDirFS=FSHelper.getInstance();
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

        getMachine().copyLuxFile();
        luxParser.setExcelFile(getMachine().getMachineDir().getMachinePath() + getMachine().getLuxFile());
        getMachine().setMachineType(luxParser.getMachineType());
        machineType.setText(getMachine().getMachineType());
        machineCode.setText(app.getMachineCode());

        machineDir.setItems(machineDirFS.getList());
        remoteMachineDir.setItems(remoteMachineDirFS.getList());

        machineDirFS.getFiles(Paths.get(getMachine().getMachineDir().getMachinePath()));
        remoteMachineDirFS.getFiles(Paths.get(getMachine().getRemoteMachinePath()));

        remoteMachineDir.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Path>() {
            @Override
            public void changed(ObservableValue<? extends Path> observable, Path oldValue, Path newValue) {
                if (!newValue.getParent().getFileName().toString().startsWith(getMachine().getMachineName())) {
                    remoteMachineDirFS.getFiles(newValue);
                }
            }
        });
    }
}
