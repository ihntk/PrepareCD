package com.af.igor.prepcd.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class MainFrameController {
    @FXML
    private Label machine;

    @FXML
    private Label machineType;

    @FXML
    private Label status;

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
        machineName.setText("20GA2869");
        machineCode.setText("5000");
    }
}
