package com.example.mario.raizin;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class UVDisplay extends AppCompatActivity {

    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    String address = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    Handler viewHandler = new Handler();

    String bluetoothSerial = null;

    public TextView UVDisplayObject;
    public TextView instruct;
    InputStream in;

    Button Disconnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uvdisplay);

        Intent intent = getIntent();
        address = intent.getStringExtra(DeviceList.EXTRA_ADDRESS);

        UVDisplayObject = (TextView) findViewById(R.id.RTUVdisplay);
        Disconnect = findViewById(R.id.button3);
        instruct = findViewById(R.id.textView22);

        //Connection to bluetooth
        try{
            connectBluetoothDevice();
        }catch(Exception exception){}

        // disconnect bluetooth when clicked
        Disconnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                Disconnect();
                Intent i = new Intent(getApplicationContext(), HomeFeed.class);
                startActivity(i);
            }
        });

        viewHandler.post(updateView);


    }


    //Find all bluetooth pairs and get their address and name
    public void connectBluetoothDevice() throws IOException {
        boolean ConnectSuccess = true;
        try {

            if (myBluetooth == null || !isBtConnected) {
                myBluetooth = BluetoothAdapter.getDefaultAdapter();
                BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);
                btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                btSocket.connect();
            }
        } catch (IOException e) {
            ConnectSuccess = false;//if the try failed, you can check the exception here
        }

    }
    private void getInputData() {
        //InputStream in;
        int bytes; //number of bytes read
        byte[] buffer = new byte[4]; //read 4 bytes from bluetooth to store 1 float
        //String bluetoothSerial=null;
        try {
            if (!(btSocket == null)) {
                in = btSocket.getInputStream();
                in.read(buffer, 0, 4);

                bluetoothSerial = new String(buffer, 0, 4);
            } else {
                bluetoothSerial = null;
            }
        } catch (Exception exception) {
        }
        //Toast.makeText(getApplicationContext(),bluetoothSerial, Toast.LENGTH_SHORT).show();
        UVDisplayObject.setText(bluetoothSerial);
    }


    private void Disconnect() {
        if (btSocket != null) //If the btSocket is busy
        {
            try {
                btSocket.close(); //close connection
            } catch (IOException e) { //msg("Error");}
            }
            finish(); //return to the first layout

        }
    }

    //private EmulatorView mEmulatorView;
    Runnable updateView = new Runnable() {
        @Override
        public void run() {
            //mEmulatorView.invalidate();
            viewHandler.postDelayed(updateView, 2000);
            getInputData();

        }
    };

}