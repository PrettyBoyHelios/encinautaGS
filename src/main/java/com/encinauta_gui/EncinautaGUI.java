package com.encinauta_gui;


import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EncinautaGUI {
    private JLabel mainLabel;
    private JButton btnUpdate;
    private JPanel mainPane;

    SerialPort sp = SerialPort.getCommPort("/dev/ttyACM1");


    public EncinautaGUI() {

        sp.setComPortParameters(9600, 8, 1, 0); // default connection settings for Arduino
        sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0); // block until bytes can be written

        mainLabel.setText("Encinauta GUI");
        btnUpdate.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Hello!");
            }
        });
    }

    public void dataReceived(SerialPortEvent event){
        System.out.print("Hi");
    }

    public static void main(String[] args) {

        JFrame App = new JFrame("Encinauta Ground Station");
        App.setContentPane(new EncinautaGUI().mainPane);
        App.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        App.pack();
        App.setVisible(true);
    }
}
