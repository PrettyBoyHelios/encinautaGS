package com.encinauta_gui;


import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortPacketListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class EncinautaGUI {
    public static String receivedData;
    public static String[] receivedValues;

    private JLabel mainLabel;
    private JButton btnUpdate;
    private JPanel mainPane;

    private PacketListener listener;

    SerialPort sp = SerialPort.getCommPort("/dev/tty.usbmodem14201");


    public EncinautaGUI() {
        System.out.println("Program started!");
        sp.openPort();
        sp.setComPortParameters(9600, 8, 1, 0); // default connection settings for Arduino
        sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0); // block until bytes can be written

        // Packet Listener (Handler Object)
        listener = new PacketListener();
        sp.addDataListener(listener); // Adds event handling to the Serial Port

        // String Variable Initialization

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

    private final class PacketListener implements SerialPortPacketListener
    {
        @Override
        public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_RECEIVED; }

        @Override
        public int getPacketSize() { return 100; }

        @Override
        public void serialEvent(SerialPortEvent event)
        {
            System.out.println("Packet received!");
            receivedData = "";
            byte[] newData = event.getReceivedData();
            System.out.println("Received data of size: " + newData.length);
            for (int i = 0; i < newData.length; ++i){
                if(receivedValues[i]!="x") {
                    receivedData += (char) newData[i];
                }
                //System.out.print((char)newData[i]);

            }
            System.out.println(receivedData);
            System.out.println("\n");

            // Whole method to update GUI goes here!
            receivedValues = receivedData.split(",");
            for(int i = 0; i < receivedValues.length; i++){

                System.out.println(receivedValues[i]);

            }

            System.out.println("Received " + receivedValues.length + " values");
        }
    }

    public static void main(String[] args) {

        JFrame App = new JFrame("Encinauta Ground Station");
        App.setContentPane(new EncinautaGUI().mainPane);
        App.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        App.pack();
        App.setVisible(true);
    }
}
