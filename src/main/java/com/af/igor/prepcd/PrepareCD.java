package com.af.igor.prepcd;

import com.af.igor.prepcd.controller.MainFrameController;
import com.af.igor.prepcd.controller.RootLayoutController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class PrepareCD extends Application {
    private MainApp app;
    private Stage primaryStage;
    private VBox rootLayout;
    private static final String APP_NAME = "PrepareCD";
    private MainFrameController controller;
    private RootLayoutController rootLayoutController;

    public MainFrameController getController() {
        return controller;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle(APP_NAME);

        app = MainApp.getInstance();
        app.setGui(this);

        initRootLayout();

        showMainFrame();
    }

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/RootLayout.fxml"));
            rootLayout = (VBox) loader.load();
            Scene scene = new Scene(rootLayout);

            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMainFrame() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/MainFrame.fxml"));
            BorderPane mainFrame = (BorderPane) loader.load();
            controller = loader.getController();
            controller.setApplication(this);

            VBox.setVgrow(mainFrame, Priority.ALWAYS);
            rootLayout.getChildren().add(mainFrame);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void positionDialog(final Dialog<?> dialog) {
        final Bounds position = rootLayout.localToScreen(rootLayout.getBoundsInLocal());
        dialog.setX(position.getMinX() + position.getWidth() / 2 - 200);
        dialog.setY(position.getMinY() + position.getHeight() / 2 - 150);
    }

    public void setTitle(String machineName) {
        primaryStage.setTitle(APP_NAME + " - " + machineName);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public RootLayoutController getRootLayoutController() {
        return rootLayoutController;
    }

    public void setRootLayoutController(RootLayoutController rootLayoutController) {
        this.rootLayoutController = rootLayoutController;
    }
}
