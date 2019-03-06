package com.encinauta_gui;


import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortPacketListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class EncinautaGUI {
    public static String receivedData;
    public static String[] receivedValues;

    private JPanel mainPane;

    // Content layouts
    private JPanel header, content, footer;

    // Setable Variables
    private JLabel temperatureLabel;
    private JLabel positionLabel;

    private PacketListener listener;

    SerialPort sp = SerialPort.getCommPort("COM14");


    public EncinautaGUI() {
        System.out.println("Program started!");
        sp.openPort();
        sp.setComPortParameters(9600, 8, 1, 0); // default connection settings for Arduino
        sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0); // block until bytes can be written

        // Packet Listener (Handler Object)
        listener = new PacketListener();
        sp.addDataListener(listener); // Adds event handling to the Serial Port
        // String Variable Initialization

        // Layout Sections Initialization
        header = new JPanel();
        content = new JPanel(new GridLayout(3,3));

        header.add(new JLabel("Encinauta GUI"));

        // Content Initialization

        // Temperature

        content.add(createPanel("Temperature", temperatureLabel));
        content.add(createPanel("Position", positionLabel));


        mainPane.add(header, BorderLayout.NORTH);
        mainPane.add(content, BorderLayout.CENTER);

    }

    /*public void dataReceived(SerialPortEvent event){
        System.out.print("Hi");
    }*/

    private JPanel createPanel(String cLabel, JLabel currentLabel){
        JPanel tempPanel = new JPanel(new GridLayout(2,1));
        tempPanel.add(new JLabel(cLabel));
        currentLabel = new JLabel();
        tempPanel.add(currentLabel);
        currentLabel.setText(cLabel + " goes here!");
        return tempPanel;
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
            //System.out.println("Packet received!");
            receivedData = "";
            byte[] newData = event.getReceivedData();
            //System.out.println("Received data of size: " + newData.length);
            for (int i = 0; i < newData.length; ++i){
                if((char)newData[i] != 'x') {
                    receivedData += (char) newData[i];
                }
            }
            //System.out.println(receivedData);

            // Split data and validate
            receivedValues = receivedData.split(",");
            for(int i = 0; i < receivedValues.length; ++i){
                System.out.println(receivedValues[i]);
            }

            // Update GUI
            temperatureLabel.setText(receivedValues[1] + "°C");
        }
    }

    public static void main(String[] args) {

        JFrame App = new JFrame("Encinauta Ground Station");
        EncinautaGUI obj = new EncinautaGUI();
        App.setContentPane(obj.mainPane);
        App.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        App.pack();
        App.setVisible(true);
    }
}
