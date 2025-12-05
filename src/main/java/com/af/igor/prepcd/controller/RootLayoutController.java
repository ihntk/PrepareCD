package com.af.igor.prepcd.controller;

import com.af.igor.prepcd.MainApp;
import com.af.igor.prepcd.util.WorkMode;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.io.IOException;

import static com.af.igor.prepcd.MainApp.*;

public class RootLayoutController {

    private MainApp app = MainApp.getInstance();

    @FXML
    private Menu openMenu;

    @FXML
    private MenuItem renameAllDrawingsFiles;

    @FXML
    private CheckMenuItem generalModeMenuItem;

    @FXML
    private CheckMenuItem remoteModeMenuItem;

    @FXML
    private CheckMenuItem offlineModeMenuItem;

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
        app.openWithFileMan("", "--t --r=\"" + getMachine().getRemoteMachinePathString() + DRAWINGS_DIR + "\"");
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

    public void enableRenameAllDrawingsFiles() {
        renameAllDrawingsFiles.setDisable(false);
    }

    public void disableRenameAllDrawingsFiles() {
        renameAllDrawingsFiles.setDisable(true);
    }

    @FXML
    public void handleRenameAllDrawingsFiles() throws IOException {
        app.getMachine().renameAllDrawings();
        app.getGui().getController().refreshMachinePlansList();
    }

    @FXML
    public void handleOffline() {
        app.toggleOffline();
        updateWorkModeMenuItems();
    }

    @FXML
    public void handleGeneral() {
        app.toggleGeneral();
        updateWorkModeMenuItems();
    }

    @FXML
    public void handleRemote() {
        app.toggleRemote();
        updateWorkModeMenuItems();
    }

    public void updateWorkModeMenuItems() {
        WorkMode currentMode = app.getCurrentWorkMode();
        boolean wasGeneralSelected = generalModeMenuItem.isSelected();
        boolean wasRemoteSelected = remoteModeMenuItem.isSelected();
        boolean wasOfflineSelected = offlineModeMenuItem.isSelected();

        generalModeMenuItem.setSelected(currentMode == WorkMode.GENERAL);
        remoteModeMenuItem.setSelected(currentMode == WorkMode.REMOTE);
        offlineModeMenuItem.setSelected(currentMode == WorkMode.OFFLINE);

    }

    @FXML
    private void initialize() {
        updateWorkModeMenuItems();
    }

    public void postInitialize() {
        updateWorkModeMenuItems();
    }
}
