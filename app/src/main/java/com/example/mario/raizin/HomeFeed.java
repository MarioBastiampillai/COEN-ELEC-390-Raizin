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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import static com.example.mario.raizin.DeviceList.EXTRA_ADDRESS;

public class HomeFeed extends AppCompatActivity {

    //Button generalInformationButton;
    Button timeOutsideButton;


    // nick bluetooth
    BluetoothAdapter bluetoothAdapter = null;
    BluetoothAdapter myBluetooth= null;
    BluetoothSocket bluetoothSocket = null;
    Set<BluetoothDevice> pairedBluetoothDevices;
    String bluetoothAddress = null;
    String bluetoothDeviceName = null;
    Button uvButton;
    static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    int measuredUVIndex = -1;

    private TextView countDownText;
    private TextView timeOutsideText;
    private TextView timeOutsideTimerTextView;
    private TextView timeUntilReapplyTextView;
    TextView UVDisplayObject;

    private CountDownTimer countdownTimer;
    private long timeLeftInMilliReapply; //SET this variable with max timer time
    private long timeLeftInMilliTimeOutside; //set this variable with time outside
    private boolean timerRunning;

    public int totalTimeOutsideMilli;
    public int totalReapplyTimeMilli;
    String callingActivity;
    FloatingActionButton floatingActionButton;

    public void onBackPressed() {
        //super.onBackPressed();
        // dont call **super**, if u want disable back button in current screen.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.item2:
                Intent intent = new Intent(getApplicationContext(), GeneralInformationActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_feed);

        //timeOutsideButton=(Button)findViewById(R.id.timeOutsideButtonID);
        uvButton = (Button)findViewById(R.id.uvButton);
        UVDisplayObject=(TextView)findViewById(R.id.UVDisplay);
        floatingActionButton=(FloatingActionButton)findViewById(R.id.floatingActionButtonID);

        countDownText = findViewById(R.id.countdown_text);
        timeOutsideText = findViewById(R.id.timeOutsideText);
        timeUntilReapplyTextView = findViewById(R.id.timeUntilReapplyTextView);
        timeOutsideTimerTextView = findViewById(R.id.timeOutsideTimerTextView);

        Intent intent = getIntent();

        bluetoothAddress = intent.getStringExtra(DeviceList.EXTRA_ADDRESS);
        callingActivity = intent.getStringExtra("FROM_ACTIVITY");
        String callingActivity2 = "" + callingActivity;
        System.out.println("NICK HERES THE CALLING ACTIVITY:");
        System.out.println(callingActivity);
        int timerStart;

        if(callingActivity2.length() == 11){
            totalTimeOutsideMilli= intent.getIntExtra(Timer.EXTRA_TIME_OUTSIDE, 0);
            totalReapplyTimeMilli = intent.getIntExtra(Timer.EXTRA_REAPPLY_TIME, 0);
            timerStart = intent.getIntExtra(Timer.EXTRA_START_TIMER, 0);
        }
        else{
            totalTimeOutsideMilli= intent.getIntExtra(CustomTimer.EXTRA_TIME_OUTSIDE, 0);
            totalReapplyTimeMilli = intent.getIntExtra(CustomTimer.EXTRA_REAPPLY_TIME, 0);
            timerStart = intent.getIntExtra(CustomTimer.EXTRA_START_TIMER, 0);
        }


        if(timerStart == 1){
            TimerSetup("reapply", totalReapplyTimeMilli);
            TimerSetup("timeOutside", totalTimeOutsideMilli);
        }


        uvButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0) {
               getInputData();
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TimeOutsideActivity.class);
                startActivity(intent);
            }
        });
        /*generalInformationButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent intent = new Intent(getApplicationContext(), GeneralInformationActivity.class);
                //Intent inSkinType=new Intent(getApplicationContext(), GeneralInformationActivity.class);

                startActivity(intent);
            }
        });*/
        /*timeOutsideButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent intent = new Intent(getApplicationContext(), TimeOutsideActivity.class);
                startActivity(intent);
            }
        });*/
        try{
            connectBluetoothDevice();
        }catch(Exception exception){}

    }
    //Find all bluetooth pairs and get their address and name
    private void connectBluetoothDevice() throws IOException {
        try{

            if(bluetoothAdapter == null) {
                myBluetooth = BluetoothAdapter.getDefaultAdapter();
                BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(bluetoothAddress);
                bluetoothSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(uuid);
                bluetoothSocket.connect();
            }
//            myBluetooth = BluetoothAdapter.getDefaultAdapter();
//            bluetoothAddress = myBluetooth.getAddress();
//            pairedBluetoothDevices = myBluetooth.getBondedDevices();
//            if(pairedBluetoothDevices.size() > 0){
//                for(BluetoothDevice bluetoothDev : pairedBluetoothDevices){
//                    bluetoothAddress = bluetoothDev.getAddress();
//                    bluetoothDeviceName = myBluetooth.getName();
                    //Toast.makeText(getApplicationContext(),bluetoothDeviceName, Toast.LENGTH_SHORT).show();
//                }
//            }
        }catch(Exception e){ }
//        myBluetooth = BluetoothAdapter.getDefaultAdapter();
//        BluetoothDevice bluetoothDevice = myBluetooth.getRemoteDevice(bluetoothAddress);
//        bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);
//        bluetoothSocket.connect();
//        try{
//            //Toast.makeText(getApplicationContext(),bluetoothDeviceName, Toast.LENGTH_SHORT).show();
//        }catch(Exception exception){}
    }

    private void getInputData(){

        InputStream in;
        int bytes; //number of bytes read
        byte[] buffer = new byte[4]; //read 4 bytes from bluetooth to store 1 float
        String bluetoothSerial = "";
        String blueToothSerialArray[]=new String[10];
        int index=0;
        try{
            if(!(bluetoothSocket == null)) {
                in = bluetoothSocket.getInputStream();
                bytes = in.read(buffer);
                bluetoothSerial = new String(buffer, 0, bytes);
                UVDisplayObject.setText(bluetoothSerial);
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



    public void startTimer(String timer){
        if(timer == "reapply"){
            countdownTimer = new CountDownTimer(timeLeftInMilliReapply, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeftInMilliReapply = millisUntilFinished;
                    updateTimer("reapply");
                }

                @Override
                public void onFinish() {

                }
            }.start();
        }
        else{
            countdownTimer = new CountDownTimer(timeLeftInMilliTimeOutside, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeftInMilliTimeOutside = millisUntilFinished;
                    updateTimer("timeOutside");
                }

                @Override
                public void onFinish() {

                }
            }.start();
        }


    }





    public void updateTimer(String timer){
        if(timer == "reapply"){
            int minutes = (int) timeLeftInMilliReapply/60000;
            int seconds = (int) timeLeftInMilliReapply % 60000 / 1000;

            String timeleftText;
            timeleftText = "" + minutes;
            timeleftText += ":";
            if(seconds < 10)
                timeleftText += "0";
            timeleftText += seconds;

            countDownText.setText(timeleftText);
            if(countDownText.getText().equals("0:00")){
                pushNotification("reapply bitch", "lol");
                if(timeLeftInMilliTimeOutside > totalReapplyTimeMilli)
                    TimerSetup("reapply", totalReapplyTimeMilli);
            }
        }
        else{
            int minutes = (int) timeLeftInMilliTimeOutside/60000;
            int seconds = (int) timeLeftInMilliTimeOutside % 60000 / 1000;

            String timeleftText;
            timeleftText = "" + minutes;
            timeleftText += ":";
            if(seconds < 10)
                timeleftText += "0";
            timeleftText += seconds;

            timeOutsideText.setText(timeleftText);
            if(timeOutsideText.getText().equals("0:00")){
                pushNotification("Finished!", "Your outdoor sesssion is complete.");
            }
        }

    };

    public void TimerSetup(String timer, int time){
        if(timer == "reapply"){
            timeLeftInMilliReapply = time;
            startTimer(timer);
            timeUntilReapplyTextView.setText("Time until next Reapply:");
        }
        else{
            timeLeftInMilliTimeOutside = time;
            startTimer(timer);
            timeOutsideTimerTextView.setText("Remaining time in outdoor session:");
        }
    }
}
