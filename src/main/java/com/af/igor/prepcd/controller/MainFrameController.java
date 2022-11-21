package com.af.igor.prepcd.controller;

import com.af.igor.prepcd.MainApp;
import com.af.igor.prepcd.PrepareCD;
import com.af.igor.prepcd.util.AdditionalOptions;
import com.af.igor.prepcd.util.BaseDrawingPaths;
import com.af.igor.prepcd.util.CdLangFiles;
import com.af.igor.prepcd.util.FSHelper;
import javafx.application.HostServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

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
    private Label machineCode;

    @FXML
    private Label sgBar;
    @FXML
    private Label offline;

    @FXML
    private ListView<Path> machineDir;

    @FXML
    private ListView<Path> remoteMachineDir;

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
    private Button ok;

    @FXML
    private Button f1Button;

    @FXML
    private Button f4Button;

    @FXML
    public ComboBox<AdditionalOptions> additionalComboBox;


    private ObservableList<Path> machineDirList;
    private ObservableList<Path> remoteMachineDirList;
    private ObservableList<Path> basePlanDirList;
    private ObservableList<Path> cdDirList;

    private FSHelper machineDirFS;
    private FSHelper remoteMachineDirFS;
    private FSHelper basePlanDirFS;
    private FSHelper cdDirFS;

    private HostServices hostServices;
    private String installationName;
    private String targetFileName;
    private Targets currentTarget;


    public void setApplication(PrepareCD application) {
        this.application = application;
        hostServices = application.getHostServices();
    }

    public void updateInstallationName() {
        installationName = "I" + app.getMachineCode() + "-" + getMachine().getMachineName().substring(2) + ".ckd";
        if (currentTarget == Targets.INSTALL && targetFileName != null)
            targetFileName = installationName;
    }

    public String getSgBarString() {
        return sgBar.getText();
    }

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
        basePlanDirFS = FSHelper.getInstance(basePlanDirList);

        cdDirList = FXCollections.observableArrayList();
        cdDirFS = FSHelper.getInstance(cdDirList);

        additionalComboBox.setItems(FXCollections.observableArrayList(AdditionalOptions.values()));

        showOfflineMode();
    }

    public void showOfflineMode() {
        if (app.isOfflineMode()) {
            offline.setVisible(true);
        } else {
            offline.setVisible(false);
        }
    }

    @FXML
    private void handleSetMachine() {
        try {
            target.setText("Processing machine");
            status.setText("Wait");
            machineInit(machineName.getText().toUpperCase());
            if (getMachine() == null) return;
            status.setText("Machine is " + getMachine().getMachineName());
            target.setText("Ready");
            application.setTitle(getMachine().getMachineName() + (app.isOfflineMode() ? " - *OFFLINE*" : ""));
            currentMachine.setText(getMachine().getMachineName());
            machineName.setText(getMachine().getMachineName());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public String handleSetMachineCode() {
        String machineCodeString = machineCode.getText();
        Stage machineCodeStage = new Stage();
        status.setText("Enter machine code!");
        status.setTextFill(Color.RED);

        Label label = new Label();
        label.setText("Enter the code \n" +
                "without surbaise or industrialization");
        StackPane.setAlignment(label, Pos.TOP_CENTER);
        StackPane.setMargin(label, new Insets(5, 0, 0, 0));

        TextField textField = new TextField();
        textField.setText(machineCode.getText());
        StackPane.setAlignment(textField, Pos.BOTTOM_CENTER);

        StackPane pane = new StackPane(label, textField);

        textField.setOnAction(event -> {
            String code = textField.getText();
            if (!code.equals(machineCodeString)) {
                if (getMachine().setMachineCode(code)) {
                    status.setText("Machine code " + textField.getText() + " is saved");
                    status.setTextFill(Color.BLACK);
                } else status.setText("Impossible to save machine code " + textField.getText());

                machineCode.setText(app.getMachineCode());
                updateInstallationName();
            } else {
                status.setText("Machine code is remained the same");
                status.setTextFill(Color.BLACK);
            }
            machineCodeStage.close();
        });

        machineCodeStage.setTitle("Edit machine code");
        machineCodeStage.initModality(Modality.WINDOW_MODAL);
        machineCodeStage.initOwner(application.getPrimaryStage());
        machineCodeStage.setScene(new Scene(pane, 160, 70));
        machineCodeStage.showAndWait();

        return textField.getText();
    }

    @FXML
    private void handleSetAdditionalOptions() throws IOException {
        if (!currentMachine.getText().equals(machineName.getText())) {
            return;
        }

        getMachine().setAdditionalOption(additionalComboBox.getValue());
        machineCode.setText(app.getMachineCode());
        refreshMachinePlansList();
    }

    @FXML
    private void handleInstall() throws IOException {
        currentTarget = Targets.INSTALL;
        target.setText("Install");
        app.logger.log("target is: installation");
        resetControlsDefault();
        machineDirFS.getFiles(Paths.get(getMachine().getMachinePathString()));
        if (!app.isOfflineMode()) {
            remoteMachineDirFS.getFiles(Paths.get(app.PLANS, BaseDrawingPaths.I.toString()));
        }

        updateInstallationName();
        targetFileName = installationName;
        copyHere.setDisable(false);
        ok.setDisable(false);
        application.getRootLayoutController().enableRenameAllCkdFiles();
        status.setText("Copy base installation drawing for " + getMachine().getMachineType() + ". Then press ok");
        app.openWithFileMan("--l=\"" + getMachine().getMachinePathString() + "\" --t --r=\"" + getMachine().getRemoteMachinePathString() + DRAWINGS_DIR + "\"");
    }

    @FXML
    private void handleXls() throws IOException {
        currentTarget = Targets.XLS;
        target.setText("Xls");
        app.logger.log("target is: xls");
        resetControlsDefault();
        if (!getMachine().copyMachineXlsIfNotExist())
            hostServices.showDocument(getMachine().getMachinePathString() + getMachine().getMachineXlsName());
        else {
            app.initMachineExcelParser();
            machineExcelParser.fillInMachineXls();
            hostServices.showDocument(getMachine().getMachinePathString() + getMachine().getMachineXlsName());
            hostServices.showDocument(getMachine().getMachinePathString() + getMachine().getLuxFileName());
        }
        status.setText("You can processing xls file");
        endCurrentTarget();
    }

    @FXML
    private void handleMachine() throws IOException {
        currentTarget = Targets.MACHINE;
        target.setText("Machine");
        app.logger.log("target is: machine");
        resetControlsDefault();

        if (!Files.exists(Paths.get(getMachine().getMachinePathString() + getMachine().getMachineXlsName()))) {
            status.setText("You haven't xls file yet! You could correct this now");
            handleXls();
            status.setText("You can continue process machine just pressing Machine button");
        } else {

            targetFileName = null;
            application.getRootLayoutController().enableRenameAllCkdFiles();
            copyHere.setDisable(false);
            ok.setDisable(false);
            f4Button.setText("Apragaz");
            f4Button.setDisable(false);
            status.setText("Copy base drawings for " + getMachine().getMachineType() + ". Then press ok");
            app.openWithFileMan("--l=\"" + getMachine().getMachinePathString() + "\" --t --r=\"" + PLANS + "\"");

            String currentStatus = status.getText();
            basePlanDirFS.getFiles(Paths.get(getMachine().getMachinePathString()));
            refreshMachinePlansList();

            if (!app.isOfflineMode()) {
                remoteMachineDirFS.getFiles(Paths.get(app.PLANS));

                machineDir.setOnMouseClicked(mouseEvent -> {
                    try {
                        String planName = machineDir.getSelectionModel().getSelectedItem().toString().trim();
                        if (planName.length() > 4) {
                            planName = planName.split("-")[0];
                            int machCodeIndex = planName.indexOf(app.getMachineCode());
                            if (machCodeIndex > 0)
                                planName = planName.substring(0, machCodeIndex);
                        }
                        status.setText(currentStatus + "   " + planName);
                        remoteMachineDirFS.getFiles(Paths.get(app.PLANS, BaseDrawingPaths.valueOf(planName).toString()));
                    } catch (IOException e) {
                        app.logger.log("Wrong plan name");
                    }
                });
            }
        }

    }

    @FXML
    private void handleCD() throws IOException {
        currentTarget = Targets.CD;
        target.setText("CD");
        resetControlsDefault();
        hostServices.showDocument(getMachine().getMachinePathString() + getMachine().getMachineXlsName());
        app.openWithFileMan("--l=\"" + app.getCdsString() + "\" --t --r=\"" + app.getCdCommenceString() + "\"");

        app.initMachineExcelParser();
        List<String> languagesOrder = new LinkedList<>();
        Collections.addAll(languagesOrder, machineExcelParser.getLanguages());

        if (languagesOrder.get(0).equals("nothing")) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Attention");
            alert.setHeaderText("Are you sure?");
            alert.setContentText("Looks like no CD is required for this order\n" +
                    "Do you really want to make this CD?");

            ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Yes");
            ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("No");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.CANCEL) {
                return;
            }
        }

        ObservableList<Pane> languageList = showCdLanguageSelectorAlert(languagesOrder);

        List<Path> fileList = new LinkedList<>();
        Collections.addAll(fileList, CdLangFiles.getRegularFiles());
        for (Pane pane : languageList) {
            CheckBox checkBox = (CheckBox) pane.getChildren().get(0);
            if (checkBox.isSelected()) {
                fileList.add(Paths.get(CdLangFiles.values()[languageList.indexOf(pane)].getFileName()));
            }
        }

        copyBaseCD(fileList);
        /*
        скопіювати вибрані мови
         */
    }

    private void copyBaseCD(List<Path> fileList) throws IOException {
        System.out.println("The method copyBaseCD() do nothing");
//        Path sourceDirPath = Paths.get(app.getCDTEMPLATE());
//        Path targetDirPath = Paths.get(app.getCdsString());
//        for (Path path: fileList){
//            if (Files.isDirectory(sourceDirPath.resolve(path))){
//                Files.walk(sourceDirPath.resolve(path)).forEach(source ->{
//                    Path destination = targetDirPath.resolve(source.getFileName());
//                    try {
//                        app.copyPath(source,destination);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                });
//            }else app.copyPath(sourceDirPath.resolve(path),targetDirPath.resolve(path));
//        }
    }

    private ObservableList<Pane> showCdLanguageSelectorAlert(List<String> languagesOrder) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Select languages");
        ObservableList<Pane> languageList = FXCollections.observableArrayList();

        for (CdLangFiles lang : CdLangFiles.values()) {
            CheckBox checkBox = new CheckBox();
            languageList.add(new FlowPane(5, 5, checkBox, new Label(lang.getDescription())));
            if (languagesOrder.contains(lang.getDescription())) {
                checkBox.setSelected(true);
            }
        }
        alert.getDialogPane().setContent(new ListView<Pane>(languageList));
        alert.showAndWait();
        return languageList;
    }

    @FXML
    private void handleCopyHere() throws IOException {
        if (remoteMachineDir.getSelectionModel().getSelectedIndex() < 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setHeaderText("Source file isn't selected");
            alert.setContentText("Select source file you want to copy (in the right table)");
            alert.showAndWait();
            return;
        }

        Path sourcePath = remoteMachineDirFS.getCurrentPath().resolve(remoteMachineDir.getSelectionModel().getSelectedItem());
        String filename;
        Path targetPath;
        int index = -1;
        if (targetFileName != null) {       // Target.INSTALL
            filename = targetFileName;
            targetPath = Paths.get(getMachine().getMachinePathString() + filename);
        } else {                            // Target.MACHINE
            index = machineDir.getSelectionModel().getSelectedIndex();
            if (index < 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attention");
                alert.setHeaderText("Target file isn't selected");
                alert.setContentText("Select target file you want to copy (in the left table)");
                alert.showAndWait();
                return;
            }
            filename = getMachine().defineFileName(machineDir.getSelectionModel().getSelectedItem());
            targetPath = Paths.get(getMachine().getMachinePathString() + filename);
        }

        if (!Files.exists(targetPath)) {
            Files.copy(sourcePath, targetPath);
            if (index > -1)
                machineDirList.set(index, targetPath.getFileName());
            else
                machineDirList.add(targetPath.getFileName());
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Attention");
            alert.setHeaderText("File " + targetPath.getFileName() + " exist");
            alert.setContentText("Do you really want to replace " + targetPath.getFileName() + " file?");

            ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Yes");
            ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("No");
            Optional<ButtonType> optionalButtonType = alert.showAndWait();

            if (optionalButtonType.get() == ButtonType.OK)
                Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            else
                return;
        }
    }

    @FXML
    private void handleOk() throws IOException {
        for (Path item : machineDirList) {
            if (item.getFileName().toString().startsWith("   ")) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Attention");
                alert.setHeaderText("There is uncopied files");
                alert.setContentText("You haven't copied " + getMachine().defineFileName(item) + " file\n"
                        + "Are you sure you want to continue ");

                ((Button) alert.getDialogPane().lookupButton(ButtonType.YES)).setText("Yes");
                ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("No");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK)
                    break;
                else return;
            }
        }
        copyHere.setDisable(true);
        ok.setDisable(true);
        endCurrentTarget();
    }

    @FXML
    private void handleF1Button() throws IOException {
        if (f1Button.getText().equals("Open M")) {
            getMachine().openMCkdFiles();
            machineDirFS.getFiles(machineDirFS.getCurrentPath());
        }
    }

    @FXML
    private void handleF4Button() throws IOException {
        if (f4Button.getText().equals("Open Print"))
            app.openPrintDir();
        if (f4Button.getText().equals("Apragaz"))
            processApragaz();
    }

    public void machineInit(String machineName) throws IOException, InterruptedException {
        app.logger.log("-----\n");
        if (!app.isOfflineMode()) {
            if (!Files.exists(Paths.get(app.H_MACHINES + machineName))) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Machine does'nt exists");
                alert.setHeaderText("Machine " + machineName + " doesn't exists");
                alert.setContentText("There is no directory for machine " + machineName + ".\n" +
                        "Make sure you enter correct machine name");
                alert.showAndWait();
                app.logger.log(machineName + " doesn't exists");
                return;
            }
        }
        app.initializeMachine(machineName);
        if (getMachine() == null) return;
        currentTarget = null;
        status.setText("Copying " + getMachine().getLuxFileName());

        getMachine().copyLuxFile();
        app.initLuxParser();
        getMachine().setMachineType(luxParser.getMachineType());
        machineType.setText(getMachine().getMachineType());
        if (machineType.getText().startsWith("OPC")) {
            sgBar.setText("");
        } else {
            String machinePressure = luxParser.getSgBar();
            sgBar.setText("SG" + machinePressure);
            if (Integer.parseInt(machinePressure) < 40) {
                sgBar.setTextFill(Color.BLUE);
            } else {
                sgBar.setTextFill(Color.BLACK);
            }
        }
        additionalComboBox.setValue(getMachine().getAdditionalOption());
        getMachine().initMachineCode();
        machineCode.setText(app.getMachineCode());

        machineDirFS.getFiles(Paths.get(getMachine().getMachinePathString()));

        if (!app.isOfflineMode()) {
            remoteMachineDirFS.getFiles(Paths.get(getMachine().getRemoteMachinePathString(), DRAWINGS_DIR));
        } else {
            remoteMachineDirList.clear();
        }

        installButton.setDisable(false);
        xlsButton.setDisable(false);
        machineButton.setDisable(false);
        cdButton.setDisable(false);
        application.getRootLayoutController().enableMenu();
        resetControlsDefault();

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

    }

    public String processChooseLuxFile(String path, List<String> list) throws IOException {
        String fileName = null;
        int index = -1;
        Optional<ButtonType> result;

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Choose file");
        alert.setHeaderText("Attention! There are " + list.size() + " files for this machine\n" +
                "Choose the correct file please");
        ListView<String> listView = new ListView<>();
        listView.setItems(FXCollections.observableArrayList(list));
        listView.setPrefHeight(120);
        alert.getDialogPane().setContent(listView);

        app.openWithFileMan("--t --l=\"" + path + "\"");
        result = alert.showAndWait();

        try {
            if (result.get() == ButtonType.OK) {
                index = listView.getSelectionModel().getSelectedIndex();
                if (index < 0) {
                    return processChooseLuxFile(path, list);
                } else fileName = list.get(index);
            }
        } catch (NoSuchElementException e) {
            app.logger.log("processChooseLuxFile: alert was closed");
        }

        return fileName;
    }

    private void processApragaz() throws IOException {
        currentTarget = Targets.APRAGAZ;
        refreshMachinePlansList();
    }

    protected void refreshMachinePlansList() throws IOException {
        if (currentTarget == Targets.INSTALL)
            updateInstallationName();

        if (currentTarget == Targets.MACHINE || currentTarget == Targets.APRAGAZ) {
            ArrayList<String> machinePlansList = new ArrayList<>();
            createMachinePlansList(machinePlansList);

            for (int i = 0; i < machinePlansList.size(); i++) {             //rename if file exist
                String name = getMachine().defineFileName(Paths.get(machinePlansList.get(i)));
                if (name != null)
                    for (String file : getMachine().getCkdFiles()) {
                        if (file.startsWith(name.substring(0, name.indexOf("."))))
                            machinePlansList.set(i, file);
                    }
            }

            machineDirFS.getFiles(machinePlansList);
        }
    }

    private void createMachinePlansList(ArrayList<String> machinePlansList) {
        machinePlansList.add("   I" + app.getMachineCode());
        machinePlansList.add("   E" + app.getMachineCode());
        machinePlansList.add("   FS" + app.getMachineCode());

        if (currentTarget == Targets.APRAGAZ) {
            currentTarget = Targets.MACHINE;
            return;
        }

        app.initMachineExcelParser();
        String mPlans = machineExcelParser.getMPlans();

        for (String mPlan : mPlans.split("\\+")) {
            machinePlansList.add("   " + mPlan.trim());
        }
    }

    private void endCurrentTarget() throws IOException {
        switch (currentTarget) {
            case INSTALL: {
                hostServices.showDocument(getMachine().getMachinePathString() + getMachine().getLuxFileName());
                hostServices.showDocument(getMachine().getMachinePathString() + installationName);
                if (!app.isOfflineMode()) {
                    remoteMachineDirFS.getFiles(Paths.get(getMachine().getRemoteMachinePathString(), DRAWINGS_DIR));
                }
                status.setText(installationName + " is opened");
            }
            break;

            case MACHINE: {
                getMachine().copyEtiq();
                getMachine().open4CkdFiles();
                status.setText("Ready");
                app.openWithFileMan("--r=\"" + getMachine().getRemoteMachinePathString() + DRAWINGS_DIR + "\"");
                f1Button.setText("Open M");
                f1Button.setDisable(false);

                machineDirFS.getFiles(Paths.get(getMachine().getMachinePathString()));
                if (!app.isOfflineMode()) {
                    remoteMachineDirFS.getFiles(Paths.get(getMachine().getRemoteMachinePathString(), DRAWINGS_DIR));
                }

                f4Button.setText("Open Print");
                f4Button.setDisable(false);
            }
            break;

            case CD:
                break;

            case XLS: {
                app.openWithFileMan("--l=\"" + getMachine().getMachinePathString() + "\" --t --r=\"" + getMachine().getRemoteMachinePathString() + DRAWINGS_DIR + "\"");
            }
            break;
        }
    }

    private void resetControlsDefault() {
        copyHere.setDisable(true);
        ok.setDisable(true);
        f1Button.setDisable(true);
        f1Button.setText("-");
        f4Button.setDisable(true);
        f4Button.setText("-");
        basePlanDirList.clear();
        cdDirList.clear();
        application.getRootLayoutController().disableRenameAllCkdFiles();
        status.setTextFill(Color.BLACK);
        machineCode.setTextFill(Color.BLACK);
        machineDir.setOnMouseClicked(null);
    }

    public enum Targets {
        INSTALL,
        XLS,
        MACHINE,
        APRAGAZ,
        CD;
    }

}
