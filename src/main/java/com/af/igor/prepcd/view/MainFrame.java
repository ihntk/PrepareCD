package com.af.igor.prepcd.view;

import com.af.igor.prepcd.MainApp;

import javax.swing.*;
import java.awt.*;

/**
 * Created by ede on 18.04.2017.
 */
public class MainFrame extends JFrame {
    private static final String INSTALL="install";
    private static final String XLS="xls";
    private static final String MACHINE="machine";
    private static final String CD="cd";

    private MainApp app=MainApp.getInstance();

    public MainFrame() throws HeadlessException {

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame mainFrame=new MainFrame();
                mainFrame.setDefaultCloseOperation(HIDE_ON_CLOSE);
                mainFrame.setVisible(true);
            }
        });
    }
}
