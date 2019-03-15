package com.example.mario.raizin;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class HomeFeed extends AppCompatActivity {

    Button generalInformationButton;
    Button timeOutsideButton;

    // nick bluetooth
    BluetoothAdapter bluetoothAdapter = null;
    BluetoothSocket bluetoothSocket = null;
    Set<BluetoothDevice> pairedBluetoothDevices;
    String bluetoothAddress = null;
    String bluetoothDeviceName = null;
    Button uvButton;
    static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    int measuredUVIndex = -1;

    public void onBackPressed() {
        //super.onBackPressed();
        // dont call **super**, if u want disable back button in current screen.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_feed);
        generalInformationButton=(Button)findViewById(R.id.generalInfoButtonID);
        timeOutsideButton=(Button)findViewById(R.id.timeOutsideButtonID);
        uvButton = (Button)findViewById(R.id.uvButton);

        uvButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0) {
                getInputData();
            }
        });

        generalInformationButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent intent = new Intent(getApplicationContext(), GeneralInformationActivity.class);
                //Intent inSkinType=new Intent(getApplicationContext(), GeneralInformationActivity.class);

                startActivity(intent);
            }
        });
        timeOutsideButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent intent = new Intent(getApplicationContext(), TimeOutsideActivity.class);
                startActivity(intent);
            }
        });
        try{
            connectBluetoothDevice();
        }catch(Exception exception){}

    }
    //Find all bluetooth pairs and get their address and name
    private void connectBluetoothDevice() throws IOException {
        try{
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            bluetoothAddress = bluetoothAdapter.getAddress();
            pairedBluetoothDevices = bluetoothAdapter.getBondedDevices();
            if(pairedBluetoothDevices.size() > 0){
                for(BluetoothDevice bluetoothDev : pairedBluetoothDevices){
                    bluetoothAddress = bluetoothDev.getAddress();
                    bluetoothDeviceName = bluetoothAdapter.getName();
                    //Toast.makeText(getApplicationContext(),bluetoothDeviceName, Toast.LENGTH_SHORT).show();
                }
            }
        }catch(Exception e){ }
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(bluetoothAddress);
        bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);
        bluetoothSocket.connect();
        try{
            //Toast.makeText(getApplicationContext(),bluetoothDeviceName, Toast.LENGTH_SHORT).show();
        }catch(Exception exception){}
    }

    private void getInputData(){

        InputStream in;
        int bytes; //number of bytes read
        byte[] buffer = new byte[4]; //read 4 bytes from bluetooth to store 1 float
        String bluetoothSerial = "";
        try{
            if(!(bluetoothSocket == null)) {
                in = bluetoothSocket.getInputStream();
                bytes = in.read(buffer);
                bluetoothSerial = new String(buffer, 0, bytes);
            }
        }catch(Exception exception){}
        Toast.makeText(getApplicationContext(),bluetoothSerial, Toast.LENGTH_SHORT).show();
    }

    void pushNotification(String title, String content) {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "default")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true);
        mNotificationManager.notify(0, mBuilder.build());
    }

}
