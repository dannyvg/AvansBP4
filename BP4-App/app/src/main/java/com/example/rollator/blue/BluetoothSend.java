package com.example.rollator.blue;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class BluetoothSend {
    private BluetoothSocket socket;
    private BluetoothAdapter bluetoothAdapter;

    private BufferedReader bufferedReader;
    private static final String TAG = "BluetoothExample";

    public BluetoothSend() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @SuppressLint("MissingPermission")
    public void send(String send) throws IOException {
        Log.i("send", send);
        OutputStream outputStream = socket.getOutputStream();
        // Send the plain text
        outputStream.write(send.getBytes(StandardCharsets.UTF_8));

    }

    public int startReceivingData() {
        int receivedInt = 0;
        try {
            InputStream inputStream = socket.getInputStream();

            int highByte = inputStream.read();
            int lowByte = inputStream.read();

            if (highByte != -1 && lowByte != -1) {
                Log.d(TAG, "Received Bytes: [" + highByte + ", " + lowByte + "]");

                receivedInt = (highByte << 8) | lowByte;
                Log.d(TAG, "Reconstructed Integer: " + receivedInt);
            }

            socket.close();

        } catch (IOException e) {
            Log.e(TAG, "Failed to receive data", e);
        }

        return receivedInt;
    }


    @SuppressLint("MissingPermission")
    public void createConnection(String deviceName) throws IOException {
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
        } else if (!bluetoothAdapter.isEnabled()) {
            // Bluetooth is not enabled, request to turn it on
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                // Loop through the paired devices and find the desired device
                for (BluetoothDevice device : pairedDevices) {
                    if (device.getName().equals(deviceName)) {
                        // Connect to the desired device
                        socket = device.createRfcommSocketToServiceRecord(device.getUuids()[0].getUuid());
                        socket.connect();
                    }
                }
            }
        }
    }

    public void closeConnection(){
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getBluetoothw() throws IOException {
        while(socket.isConnected()){
            InputStream inputStream = socket.getInputStream();
            String receivedText = "";
            byte[] buffer = new byte[1024];
            while (inputStream.available() > 0) {
                int numBytes = inputStream.read(buffer);
                receivedText = new String(buffer, 0, numBytes);
                Log.e("receivedText", receivedText);
                if(!receivedText.isEmpty()){
//                    Toast.makeText(, receivedText, Toast.LENGTH_SHORT).show();
                    receivedText = "";
                }
            }
            return receivedText;
        }
        return "0";
    }

//    public String getBluetooth() throws IOException{
//        InputStream inputStream = socket.getInputStream();
//
//        byte[] buffer = new byte[1024];
//        int numBytes = inputStream.read(buffer);
//        String receivedText = new String(buffer, 0, numBytes);
//        Log.e("receivedText", receivedText);
//
//        return receivedText;
//    }

    public boolean isConnected() throws IOException{
        return socket.isConnected();
    }

    @SuppressLint("MissingPermission")
    public void showPairedDevices(ListView pairedDevicesListView, ArrayAdapter<String> pairedDevicesArrayAdapter) {

        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
        } else if (!bluetoothAdapter.isEnabled()) {
            // Bluetooth is not enabled, request to turn it on
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            // Get the default Bluetooth adapter
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter == null) {
                // Device does not support Bluetooth
                return;
            }
            // Get the list of paired devices
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            // Create an array adapter for the list view
            pairedDevicesListView.setAdapter(pairedDevicesArrayAdapter);

            if (pairedDevices.size() > 0) {
                pairedDevicesArrayAdapter.clear();
                // Loop through the paired devices and add them to the list view
                for (BluetoothDevice device : pairedDevices) {
                    pairedDevicesArrayAdapter.add(device.getName());
                }
            } else {
                pairedDevicesArrayAdapter.add("No paired devices found.");
            }
        }
    }
}
