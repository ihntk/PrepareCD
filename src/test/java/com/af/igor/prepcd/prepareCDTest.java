package com.af.igor.prepcd;

import com.af.igor.prepcd.controller.MainFrameController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class prepareCDTest {
    private static MainApp mainApp;
    private static PrepareCD prepareCD;

    public static void main(String[] args) throws IOException, InterruptedException {
        PrepareCD.main(args);
        mainApp = MainApp.getInstance();
        prepareCD = mainApp.getGui();
        MainFrameController controller = prepareCD.getController();
        List<String> list = new ArrayList<>(3);
        list.add("file1.msg");
        list.add("file2.jpg");
        list.add("file3.xlsx");

        Thread.sleep(3000);
        controller.processChooseLuxFile(null, list);
    }
}
