package com.af.igor.prepcd;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class PrepareCD extends Application {
    MainApp app;
    private Stage primaryStage;
    private VBox rootLayout;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("PrepareCD");

        app=MainApp.getInstance();

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

            VBox.setVgrow(mainFrame, Priority.ALWAYS);
            rootLayout.getChildren().add(mainFrame);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
