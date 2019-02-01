package com.af.igor.prepcd.controller;

import com.af.igor.prepcd.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.io.IOException;

public class RootLayoutController {
    private MainApp app = MainApp.getInstance();

    @FXML
    private MenuItem About;

    @FXML
    private Menu openMenu;

    @FXML
    private MenuItem renameAllCkdFiles;

    public RootLayoutController() {
        app.getGui().setRootLayoutController(this);
    }

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

    public void enableMenu(){
        openMenu.setDisable(false);
        renameAllCkdFiles.setDisable(false);
    }

    public void handleRenameAllCkdFiles() throws IOException {
        app.getMachine().renameAllCkd();
    }
}
