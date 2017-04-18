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
        testPath();
    }

    private static void testPath() throws IOException {
        desktop.open(new File(mainApp.LOGFILE));
        mainApp.tc(mainApp.LUX_DIR);
        desktop.open(new File(mainApp.LUX_DIR));
        mainApp.tc(mainApp.MACHINES);
        desktop.open(new File(mainApp.XLS));
    }
}
