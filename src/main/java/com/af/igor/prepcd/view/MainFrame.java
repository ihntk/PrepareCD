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
        getContentPane().setLayout(new BorderLayout());

        JPanel topPanel=new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Machine"));

        JTextField machineChooser=new JTextField();
        machineChooser.setColumns(7);

//        JTextArea machineType=new JTextArea(app.getMachine().getMachineType());
        JTextArea machineType=new JTextArea("L5A");
        topPanel.add(machineChooser);
        topPanel.add(machineType);

        JButton instBut=new JButton("Install");
        instBut.setName(INSTALL);

        JButton xlsBut=new JButton("Xls");
        xlsBut.setName(XLS);

        JButton machineBut=new JButton("Machine");
        machineBut.setName(MACHINE);

        JButton cdBut=new JButton("Cd");
        cdBut.setName(CD);

        JPanel buttonsPanel= new JPanel();
        buttonsPanel.setLayout(new GridLayout(1,4));
        buttonsPanel.add(instBut);
        buttonsPanel.add(xlsBut);
        buttonsPanel.add(machineBut);
        buttonsPanel.add(cdBut);

        topPanel.add(buttonsPanel,BorderLayout.CENTER);


        getContentPane().add(topPanel,BorderLayout.NORTH);
        setBounds(100,100,530,800);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }
                MainFrame mainFrame=new MainFrame();
                mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                mainFrame.setVisible(true);
            }
        });
    }
}
