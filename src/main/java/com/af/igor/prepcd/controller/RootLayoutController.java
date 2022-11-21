package com.af.igor.prepcd.controller;

import com.af.igor.prepcd.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.io.IOException;

import static com.af.igor.prepcd.MainApp.*;

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

    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About PrepareCD");
        alert.setHeaderText("PrepareCD");
        alert.setContentText(app.getVersion());
        app.getGui().positionDialog(alert);
        alert.showAndWait();
    }

    @FXML
    private void handleOpenPrint() throws IOException {
        app.openPrintDir();
    }

    public void handleOpenLocalMachineDir() throws IOException {
        app.openWithFileMan("--t --l=\"" + getMachine().getMachinePathString() + "\"");
    }

    @FXML
    private void handleOpenRemoteMachineDir() throws IOException {
        app.openWithFileMan("", "--t --r=\"" + getMachine().getRemoteMachinePathString() + "\"");
    }

    @FXML
    private void handleOpenBaseDrawings() throws IOException {
        app.openWithFileMan("", "--t --r=\"" + PLANS + "\"");
    }

    @FXML
    private void handleOpenBaseCD() throws IOException {
        app.openWithFileMan("--t --r=\"" + app.getCDTEMPLATE() + "\"");
    }

    @FXML
    private void handleOpenRemoteCD() throws IOException {
        app.openWithFileMan("", "--t --r=\"" + app.getCdCommenceString() + "\"");
    }

    public void enableMenu() {
        openMenu.setDisable(false);
    }

    public void enableRenameAllCkdFiles() {
        renameAllCkdFiles.setDisable(false);
    }

    public void disableRenameAllCkdFiles() {
        renameAllCkdFiles.setDisable(true);
    }

    @FXML
    public void handleRenameAllCkdFiles() throws IOException {
        app.getMachine().renameAllCkd();
        app.getGui().getController().refreshMachinePlansList();
    }

    @FXML
    public void handleOffline() {
        app.toggleOffline();
    }
}
