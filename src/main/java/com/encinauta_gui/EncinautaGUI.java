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

    private JLabel mainLabel;
    private JButton btnUpdate;
    private JPanel mainPane;
    private JLabel tempLabel;
    private JLabel titleLabel;
    private JLabel humidityLabel;
    private JLabel pressLabel;
    private JPanel contentPane;
    private JLabel accelLabel;
    private JLabel labelTemperature;

    private PacketListener listener;

    SerialPort sp = SerialPort.getCommPort("/dev/tty.usbserial-A97CX2YJ");


    public EncinautaGUI() {
        System.out.println("Program started!");

        titleLabel.setText("UP-E6788");

        sp.openPort();
        sp.setComPortParameters(9600, 8, 1, 0); // default connection settings for Arduino
        sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0); // block until bytes can be written

        // Packet Listener (Handler Object)
        listener = new PacketListener();
        sp.addDataListener(listener); // Adds event handling to the Serial Port

        contentPane.setLayout(new GridLayout(3,3,5,5));

        // String Variable Initialization

        //mainLabel.setText("Encinauta GUI");

    }

    public void dataReceived(SerialPortEvent event){
        System.out.print("Hi");
    }

    private final class PacketListener implements SerialPortPacketListener
    {
        @Override
        public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_RECEIVED; }

        @Override
        public int getPacketSize() { return 150; }

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
                //System.out.print((char)newData[i]);

            }
            System.out.println(receivedData);
            //mainLabel.setText(receivedData);



            // Whole method to update GUI goes here!
            receivedValues = receivedData.split(",");
            /*for(int i = 0; i < receivedValues.length; ++i){
                System.out.println(receivedValues[i]);
            }*/

            tempLabel.setText(receivedValues[1] + "°C, " + receivedValues[4] + "°C");
            humidityLabel.setText(receivedValues[2] + " humval");
            pressLabel.setText(receivedValues[3] + " Hg");
            accelLabel.setText("(" + receivedValues[8] + ","+ receivedValues[9] + "," + receivedValues[10] + ")");

            //System.out.println("Received " + receivedValues.length + " values");
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
