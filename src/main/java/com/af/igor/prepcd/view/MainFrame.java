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

//    private MainApp app=MainApp.getInstance();

    public MainFrame() throws HeadlessException {
        getContentPane().setLayout(new BorderLayout());

        JPanel topPanel=new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        topPanel.add(new JLabel("Machine"));

        JTextField machineChooser=new JTextField();
        machineChooser.setColumns(7);
        machineChooser.setText("20");
        topPanel.add(machineChooser);

        JTextArea machineType=new JTextArea();
        machineType.setText("L5A");
        machineType.setOpaque(false);
//        machineType.setText(app.getMachine().getMachineType());
        topPanel.add(machineType);

        JLabel label=new JLabel();
        label.setText("L5A");
        topPanel.add(label);
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setPreferredSize(new Dimension(5,20));
        topPanel.add(separator);


        JButton instBut=new JButton("Install");
        instBut.setName(INSTALL);

        JButton xlsBut=new JButton("Xls");
        xlsBut.setName(XLS);

        JButton machineBut=new JButton("Machine");
        machineBut.setName(MACHINE);

        JButton cdBut=new JButton("Cd");
        cdBut.setName(CD);

        topPanel.add(instBut);
        topPanel.add(xlsBut);
        topPanel.add(machineBut);
        topPanel.add(cdBut);

        JPanel mainPanel= new JPanel();
        mainPanel.setLayout(new GridLayout(2,2));

        String[] data={"dksk;lgj","lksdj","sl;gksfo","spsgfbs","orewoi","sdklfj","dlkfg","sdfsdfadsafa","dksk;lgj","lksdj","sl;gksfo","spsgfbs","orewoi","sdklfj","dlkfg","sdfsdfadsafa","dksk;lgj","lksdj","sl;gksfo","spsgfbs","orewoi","sdklfj","dlkfg","sdfsdfadsafa"};
        JList machineDirList = new JList(data);
//        machineDirList.setPreferredSize(new Dimension(260,360));
        JScrollPane machineDirPanel=new JScrollPane(machineDirList);

        JList machineRemoteDirList = new JList(data);
//        machineRemoteDirList.setPreferredSize(new Dimension(260,360));
        JScrollPane machineRemoteDirPanel=new JScrollPane(machineRemoteDirList);

        JList baseDrawingDirList = new JList(data);
//        baseDrawingDirList.setPreferredSize(new Dimension(260,360));
        JScrollPane baseDrawingDirPanel=new JScrollPane(baseDrawingDirList);

        JList cdDirList = new JList(data);
//        cdDirList.setPreferredSize(new Dimension(260,360));
        JScrollPane cdDirPanel=new JScrollPane(cdDirList);

        mainPanel.add(machineDirPanel);
        mainPanel.add(machineRemoteDirPanel);
        mainPanel.add(baseDrawingDirPanel);
        mainPanel.add(cdDirPanel);

        getContentPane().add(topPanel,BorderLayout.NORTH);
        getContentPane().add(mainPanel,BorderLayout.CENTER);

        pack();
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
