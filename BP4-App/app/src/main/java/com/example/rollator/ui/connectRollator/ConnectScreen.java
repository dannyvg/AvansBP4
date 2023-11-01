package com.example.rollator.ui.connectRollator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rollator.MainActivity;
import com.example.rollator.R;
import com.example.rollator.blue.BluetoothSend;
import com.example.rollator.functions.FunLoopMotivatie;

import java.io.IOException;

public class ConnectScreen extends AppCompatActivity {
    private EditText txtNaamkamer;
    private ArrayAdapter<String> pairedDevicesArrayAdapter;
    private ListView list;
    private TextView connectionStatus;
    private Button btnShowList, buttonStart, buttonClose;

    private final BluetoothSend bluetoothSend = new BluetoothSend();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_screen);

        Intent intent = getIntent();
        String naam = intent.getStringExtra("naam");
        String kamer = intent.getStringExtra("kamer");

        txtNaamkamer = (EditText) findViewById(R.id.naamKamer);
        btnShowList = (Button) findViewById(R.id.showList);
        connectionStatus = (TextView) findViewById(R.id.connectionStatus);
        buttonClose = (Button) findViewById(R.id.btnStop);
        buttonStart = (Button) findViewById(R.id.buttonStart);
        list = (ListView) findViewById(R.id.listView);

        txtNaamkamer.setText(naam + " - " + kamer);

        pairedDevicesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        //Bluetooth
        btnShowList.setOnClickListener(view -> {
            bluetoothSend.showPairedDevices(list, pairedDevicesArrayAdapter);
        });

        Thread getData = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (bluetoothSend.isConnected()) {
                        bluetoothSend.getBluetoothw();
                    }
                } catch (IOException e) {
                    Log.e("IOException", e.getMessage());
                } catch (NullPointerException e){
                    Log.e("NullPointerException", e.getMessage());
                }
            }
        });

        buttonClose.setOnClickListener(view -> {
            try {
                if (bluetoothSend.isConnected()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int receivedValue = bluetoothSend.startReceivingData();
//                            Log.e("Received value", String.valueOf(receivedValue));
                            FunLoopMotivatie.updateLoopmotivatie(kamer,receivedValue);

                        }
                    }).start();

                    bluetoothSend.send("x");

//                    bluetoothSend.closeConnection();
                    Intent i = new Intent(ConnectScreen.this, MainActivity.class);
                    startActivity(i);
                }
            } catch (IOException e) {
                Log.e("IOException e", e.getMessage());
            } catch (NullPointerException e) {
                Log.e("NullPointerException e", e.getMessage());
            }

        });

        buttonStart.setOnClickListener(view -> {
            try {
                bluetoothSend.send(naam + " - " + kamer);
                buttonStart.setEnabled(false);

                Thread th = new Thread() {
                    @Override
                    public void run() {
                        FunLoopMotivatie.insertLoopMotivatie(kamer);
                    }
                };
                th.start();
            } catch (IOException e) {
                Log.e("IOExeptoin", e.getMessage());
            }
        });


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String send;
                String device = list.getAdapter().getItem(i).toString();
                Log.i("device name", device);

                try {
                    bluetoothSend.createConnection(device);
                } catch (IOException e) {
                    Log.e("IOExeptoin", e.getMessage());
                } finally {
                    try {
                        if (bluetoothSend.isConnected()) {
                            connectionStatus.setText("Er is verbinding.");
                            pairedDevicesArrayAdapter.clear();
                            getData.start();
                        }
                    } catch (IOException e) {
                        Log.e("IOException e", e.getMessage());
                    }
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        return;
    }
}