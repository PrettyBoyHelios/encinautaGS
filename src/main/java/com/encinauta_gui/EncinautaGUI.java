package com.encinauta_gui;


import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortPacketListener;

import javax.swing.*;
import java.awt.*;


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
    private JLabel temp2Label;
    private JLabel magnetLabel;
    private JLabel gyroLabel;
    private JLabel latLabel;
    private JLabel longLabel;
    private JLabel altitudeLabel;
    private JLabel velLabel;
    private JLabel signalLabel;
    private JPanel dhtPanel;
    private JPanel bmpPanel;
    private JPanel magnetPanel;
    private JPanel accelPanel;
    private JFormattedTextField formattedTextField1;
    private JPanel gyroPanel;
    private JLabel labelTemperature;

    private PacketListener listener;

    SerialPort sp = SerialPort.getCommPort("/dev/tty.usbmodem14201");


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

            if(receivedValues.length == 19){
                // DHT
                tempLabel.setText("Temperature: " + receivedValues[1] + "°C");
                humidityLabel.setText("Humidity: " + receivedValues[2] + " humval");

                // BMP
                pressLabel.setText("Pressure: " + receivedValues[3] + " Hg");
                temp2Label.setText("Temperature: " + receivedValues[4] + " °C");

                // Magnetometer
                magnetLabel.setText("(" + receivedValues[5] + ","+ receivedValues[6] + "," + receivedValues[7] + ")");

                // Accelerometer
                accelLabel.setText("(" + receivedValues[8] + ","+ receivedValues[9] + "," + receivedValues[10] + ")");

                // Gyroscope
                gyroLabel.setText("(" + receivedValues[11] + ","+ receivedValues[12] + "," + receivedValues[13] + ")");

                // Lat & Long
                latLabel.setText("Latitude: " + receivedValues[14] + "");
                longLabel.setText("Longitude: " + receivedValues[15] + "");
                altitudeLabel.setText("Altitude: " + receivedValues[16] + " m");
                velLabel.setText("Velocity: " + receivedValues[17] + " m/s");
                signalLabel.setText("Signal Strenght: " + receivedValues[18] + " dB");
            }else{
                System.out.println(receivedValues.length);
            }



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
