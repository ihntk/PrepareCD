package com.af.igor.prepcd.controller;

import com.af.igor.prepcd.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;

import java.io.IOException;

public class RootLayoutController {
    private MainApp app = MainApp.getInstance();

    @FXML
    private MenuItem About;

    public void handleAbout(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About PrepareCD");
        alert.setHeaderText("PrepareCD");
        alert.setContentText(app.getVersion());
        alert.showAndWait();
    }

    public void handleOpenPrint() throws IOException {
        app.openPrintDir();
    }
}
