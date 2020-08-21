package com.af.igor.prepcd;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by ede on 18.04.2017.
 */
public class MainAppTest {
    private static MainApp mainApp;
    private static Desktop desktop=Desktop.getDesktop();

    public static void main(String[] args) throws IOException {
        mainApp =MainApp.getInstance();
        mainApp.logger.log("MainAppTest started");
        mainApp.logger.log("testPart");
        testPath();

        mainApp.logger.log("MainAppTest stopped");
        mainApp.logger.stopLogging();
    }

    private static void testPath() throws IOException {
        mainApp.openWithFileMan(mainApp.MACHINES);
        desktop.open(new File(mainApp.XLS));
    }
}
