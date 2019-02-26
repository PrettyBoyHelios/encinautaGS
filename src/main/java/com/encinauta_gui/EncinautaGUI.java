package com.encinauta_gui;

import com.fazecast.jSerialComm.SerialPort;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EncinautaGUI {
    private JLabel mainLabel;
    private JButton btnUpdate;
    private JPanel mainPane;

    SerialPort sp = new SerialPort();

    public EncinautaGUI() {
        mainLabel.setText("Encinauta GUI");
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Hello!");
            }
        });
    }

    public static void main(String[] args) {
        JFrame App = new JFrame("Encinauta Ground Station");
        App.setContentPane(new EncinautaGUI().mainPane);
        App.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        App.pack();
        App.setVisible(true);
    }
}
