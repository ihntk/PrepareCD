package com.af.igor.prepcd.controller;

import com.af.igor.prepcd.MainApp;
import com.af.igor.prepcd.PrepareCD;
import com.af.igor.prepcd.util.*;
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
import org.apache.commons.collections4.map.HashedMap;

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
    public Label languages;

    @FXML
    private Label currentMachine;

    @FXML
    private TextField machineName;

    @FXML
    private Label machineCode;

    @FXML
    private Label sgBar;

    @FXML
    private Label workModeLabel;

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
    private Path selectedPath;

    public void setApplication(PrepareCD application) {
        this.application = application;
        hostServices = application.getHostServices();
    }

    public void updateInstallationName() {
        installationName = "I" + app.getMachineCode() + "-" + getMachine().getMachineName().substring(2) + ".ckd";
        if (currentTarget == Targets.INSTALL && targetFileName != null) targetFileName = installationName;
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

        showWorkMode();
    }

    public void showWorkMode() {
        switch (app.getCurrentWorkMode()) {
            case OFFLINE:
                workModeLabel.setText("Offline");
                workModeLabel.setVisible(true);
                break;
            case REMOTE:
                workModeLabel.setText("Remote");
                workModeLabel.setVisible(true);
                break;
            case GENERAL:
            default:
                workModeLabel.setText("");
                workModeLabel.setVisible(false);
                break;
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
            updateWindowTitle();
            currentMachine.setText(getMachine().getMachineName());
            fillLanguagesLabel();
            machineName.setText(getMachine().getMachineName());
            openExistedTestPdf();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void openExistedTestPdf() {
        String testPdf = getMachine().getMachinePathString() + "Test.pdf";
        if (Files.exists(Paths.get(testPdf))) hostServices.showDocument(testPdf);
    }

    private void fillLanguagesLabel() {
        StringBuilder langs = new StringBuilder("");
        if (app.isMachineXlsExist()) {
            app.initMachineExcelParser();
            for (String lang : returnLanguagesOrder()) {
                langs.append(lang + " ");
            }
            if (!langs.equals("")) langs.insert(0, "lang: ").append("| ");
        }
        languages.setText(langs.toString());
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
        application.getRootLayoutController().enableRenameAllDrawingsFiles();
        status.setText("Copy base installation drawing for " + getMachine().getMachineType() + ". Then press ok");
        app.openWithFileMan("--t --l=\"" + getMachine().getMachinePathString() + "\"", "--t --r=\"" + getMachine().getRemoteMachinePathString() + DRAWINGS_DIR + "\"");
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

        if (!app.isMachineXlsExist()) {
            status.setText("You haven't xls file yet! You could correct this now");
            handleXls();
            status.setText("You can continue process machine just pressing Machine button");
        } else {

            targetFileName = null;
            application.getRootLayoutController().enableRenameAllDrawingsFiles();
            copyHere.setDisable(false);
            ok.setDisable(false);
            f4Button.setText("Apragaz");
            f4Button.setDisable(false);
            status.setText("Copy base drawings for " + getMachine().getMachineType() + ". Then press ok");
            app.openWithFileMan("--t --l=\"" + getMachine().getMachinePathString() + "\"", "--t --r=\"" + PLANS + "\"");

            String currentStatus = status.getText();
            basePlanDirFS.getFiles(Paths.get(getMachine().getMachinePathString()));     //???
            refreshMachinePlansList();
            fillLanguagesLabel();

            if (!app.isOfflineMode()) {
                remoteMachineDirFS.getFiles(Paths.get(app.PLANS));

                machineDir.setOnMouseClicked(mouseEvent -> {
                    try {
                        String planName = machineDir.getSelectionModel().getSelectedItem().toString().trim();
                        if (planName.length() > 3) {
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
        if (!app.isMachineXlsExist()) {
            status.setText("You haven't xls file yet! You could correct this now");
            handleXls();
        } else {
            currentTarget = Targets.CD;
            target.setText("CD");
            resetControlsDefault();
//            hostServices.showDocument(getMachine().getMachinePathString() + getMachine().getMachineXlsName());    //offline mode
            hostServices.showDocument(getMachine().getRemoteMachinePathString() + DRAWINGS_DIR + getMachine().getMachineXlsName());
            app.openWithFileMan("--t --l=\"" + app.getCdsString() + "\"", "--t --r=\"" + app.getCdCommenceString() + "\"");

            fillLanguagesLabel();
            Map<String, Path> plansMap = new HashedMap<>();
            List<String> languagesOrder = returnLanguagesOrder();

            if (languagesOrder.get(0).equals("nothing")) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Attention");
                alert.setHeaderText("Are you sure?");
                alert.setContentText("Looks like no CD is required for this order\n" +
                        "Do you really want to make this CD?");

                ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Yes");
                ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("No");
                application.positionDialog(alert);
                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.CANCEL) {
                    return;
                }
            }

            ObservableList<Pane> languageList = showCdLanguageSelectorAlert(languagesOrder);
            if (languageList.isEmpty()) {
                return;
            }

            List<Path> fileList = new LinkedList<>();
            Collections.addAll(fileList, CdLangFiles.getRegularFiles());
            for (Pane pane : languageList) {
                CheckBox checkBox = (CheckBox) pane.getChildren().get(0);
                if (checkBox.isSelected()) {
                    fileList.add(Paths.get(CdLangFiles.values()[languageList.indexOf(pane)].getFileName()));
                }
            }

            if (!Files.exists(Paths.get(app.getCdsString()).resolve(fileList.get(0)))) {
                copyBaseCD(fileList);
            }

//            refreshMachinePlansList();
//            copyDrawings(plansMap);
//            copyMopAndMin();
        }
    }

    private List<String> returnLanguagesOrder() {
        List<String> languagesOrder = new LinkedList<>();
        Collections.addAll(languagesOrder, machineExcelParser.getLanguages());
        return languagesOrder;
    }

    private ObservableList<Pane> showCdLanguageSelectorAlert(List<String> languagesOrder) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Select languages");
        alert.getDialogPane().setPrefHeight(430);
        ObservableList<Pane> languageList = FXCollections.observableArrayList();

        for (CdLangFiles lang : CdLangFiles.values()) {
            CheckBox checkBox = new CheckBox();
            languageList.add(new FlowPane(5, 5, checkBox, new Label(lang.getDescription())));
            if (languagesOrder.contains(lang.getDescription())) {
                checkBox.setSelected(true);
            }
        }
        alert.getDialogPane().setContent(new ListView<Pane>(languageList));
        application.positionDialog(alert);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.CANCEL) {
            return FXCollections.emptyObservableList();
        }
        return languageList;
    }

    private void copyBaseCD(List<Path> fileList) throws IOException {
        Path sourceDirPath = Paths.get(app.getCDTEMPLATE());
        Path targetDirPath = Paths.get(app.getCdsString());

        for (Path path : fileList) {
            Path sourcePath = sourceDirPath.resolve(path);
            if (Files.isDirectory(sourceDirPath)) {
                FSHelper.copyDirPath(sourcePath, targetDirPath.resolve(path));
            } else app.copyPath(sourceDirPath.resolve(path), targetDirPath.resolve(path));
        }
    }

    private void copyDrawings(Map<String, Path> planMap) {
//        Thread drawing = new Thread();
        Drawing drawing = new Drawing(planMap);
        handleSelectDrawing(drawing);
        drawing.start();
    }

    private synchronized void copyDrawingsThread(Map<String, Path> planMap) throws IOException, InterruptedException {
        for (int i = 0; i < machineDirList.size(); i++) {
            String planName = machineDirList.get(i).toString().trim();
            if (planName.length() > 5) {
                continue;
            }

            if (planName.equals("HPET")) {
                machineDir.getSelectionModel().select(i);
                remoteMachineDirFS.getFiles(Paths.get(HPET_DIR));

                //тут потрібно зупинити цикл і чекати, доки не буде mouseClicked
                wait();

                //TODO remove after test and uncomment bellow
                planMap.put(planName, selectedPath);
                machineDirList.set(i, Paths.get(planName + " -> " + planMap.get(planName).getFileName()));
            } else

//            if (planName.equals("SE")) {
//                remoteMachineDirFS.getFiles(Paths.get(SE_DIR));
//                selectDrawing(planMap, planName);
//            }else

                if (planName.equals("REFR")) {
                    remoteMachineDirFS.getFiles(Paths.get(DRAWINGS_DIR));
                }

//            planMap.put(planName, selectedPath);
//            machineDirList.set(i, Paths.get(planName + " -> " + planMap.get(planName).getFileName()));
        }
    }

    private void handleSelectDrawing(Drawing drawing) {
        synchronized (drawing) {
            remoteMachineDir.setOnMouseClicked(mouseEvent -> {
                selectedPath = remoteMachineDirFS.getCurrentPath().resolve(remoteMachineDir.getSelectionModel().getSelectedItem());
                notify();
            });
        }
    }

    private class Drawing extends Thread {

        private Map<String, Path> plansMap;

        public Drawing(Map<String, Path> plansMap) {
            this.plansMap = plansMap;
        }

        @Override
        public void run() {
            try {
                copyDrawingsThread(plansMap);
            } catch (IOException e) {
                app.logger.log("File access error while select or copy drawings into CD");
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void copyMopAndMin() {
    }

    @FXML
    private void handleCopyHere() throws IOException {
        if (remoteMachineDir.getSelectionModel().getSelectedIndex() < 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setHeaderText("Source file isn't selected");
            alert.setContentText("Select source file you want to copy (in the right table)");
            application.positionDialog(alert);
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
                application.positionDialog(alert);
                alert.showAndWait();
                return;
            }
            filename = getMachine().defineFileName(machineDir.getSelectionModel().getSelectedItem());
            String ext = sourcePath.toString().substring(sourcePath.toString().lastIndexOf("."));
            targetPath = Paths.get(getMachine().getMachinePathString(), filename + ext);
        }

        if (!Files.exists(targetPath)) {
            Files.copy(sourcePath, targetPath);
            if (index > -1) machineDirList.set(index, targetPath.getFileName());
            else machineDirList.add(targetPath.getFileName());
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Attention");
            alert.setHeaderText("File " + targetPath.getFileName() + " exist");
            alert.setContentText("Do you really want to replace " + targetPath.getFileName() + " file?");

            ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Yes");
            ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("No");
            application.positionDialog(alert);
            Optional<ButtonType> optionalButtonType = alert.showAndWait();

            if (optionalButtonType.get() == ButtonType.OK) {
                Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }
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
                alert.setHeaderText("There is uncopied file");
                alert.setContentText("You haven't copied " + getMachine().defineFileName(item) + " file\n"
                        + "Are you sure you want to continue ");

                ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Yes");
                ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("No");
                application.positionDialog(alert);
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
        if (f4Button.getText().equals("Open Print")) app.openPrintDir();
        if (f4Button.getText().equals("Apragaz")) processApragaz();
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
                application.positionDialog(alert);
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

        app.openWithFileMan("", "--t --l=\"" + path + "\"");
        application.positionDialog(alert);
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
        ArrayList<String> machinePlanList = null;

        if (currentTarget == Targets.INSTALL) {
            updateInstallationName();
            return;
        } else if (currentTarget == Targets.MACHINE || currentTarget == Targets.APRAGAZ) {
            machinePlanList = new ArrayList<>();
            createMachinePlansList(machinePlanList);

            for (int i = 0; i < machinePlanList.size(); i++) {             //rename if file exist
                String name = getMachine().defineFileName(Paths.get(machinePlanList.get(i)));
                if (name == null)
                    continue;

                ArrayList<String> drawingsList = getMachine().getCkdFiles();
                drawingsList.addAll(getMachine().getSlddrwFiles());
                for (String file : drawingsList) {
                    if (file.startsWith(name)) machinePlanList.set(i, file);
                }
            }
        } else if (currentTarget == Targets.CD) {
            machinePlanList = new ArrayList<>();
            createMachinePlansList(machinePlanList);
        } else return;
        machineDirFS.getFiles(machinePlanList);
    }

    private void createMachinePlansList(ArrayList<String> machinePlanList) {
        if (currentTarget != Targets.CD) {
            machinePlanList.add("   I" + app.getMachineCode() + "-");
            machinePlanList.add("   E" + app.getMachineCode() + "-");
            machinePlanList.add("   FS" + app.getMachineCode() + "-");
        } else {
            machinePlanList.add("   I");
            machinePlanList.add("   E");
            machinePlanList.add("   FS");
        }

        if (currentTarget == Targets.APRAGAZ) {
            currentTarget = Targets.MACHINE;
            return;
        }

        app.initMachineExcelParser();
        String mPlans = machineExcelParser.getMPlans();

        for (String mPlan : mPlans.split("\\+")) {
            machinePlanList.add("   " + mPlan.trim() + "-");
        }

        if (currentTarget == Targets.CD) {
            String cdPlans = machineExcelParser.getCdPlans();

            for (String cdPlan : cdPlans.split(",")) {
                machinePlanList.add("   " + cdPlan.trim());
            }
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
                if (app.isRemoteMode()) {
                    getMachine().moveFilesToRemote();
                    getMachine().openPdmExplorer();
                    status.setText("Mowe slddrw files to PDM.");
                } else {
                    getMachine().open4CkdFiles();
                    status.setText("Ready");
                    app.openWithFileMan("", "--r=\"" + getMachine().getRemoteMachinePathString() + DRAWINGS_DIR + "\"");
                }
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
                app.openWithFileMan("--t --l=\"" + getMachine().getMachinePathString() + "\"", "--t --r=\"" + getMachine().getRemoteMachinePathString() + DRAWINGS_DIR + "\"");
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
        application.getRootLayoutController().disableRenameAllDrawingsFiles();
        status.setTextFill(Color.BLACK);
        machineCode.setTextFill(Color.BLACK);
        machineDir.setOnMouseClicked(null);
    }

    public enum Targets {
        INSTALL,
        XLS,
        MACHINE,
        APRAGAZ,
        CD,
    }

    private void updateWindowTitle() {
        String title = getMachine().getMachineName();
        String titleSuffix = "";

        if (app.getCurrentWorkMode() == WorkMode.GENERAL) titleSuffix = "";
        else titleSuffix = " - " + workModeLabel.getText() + " mode";

        application.setTitle(title + titleSuffix);
    }

    public void onWorkModeChanged() {
        showWorkMode();
        if (getMachine() != null) {
            updateWindowTitle();
        }
    }
}
