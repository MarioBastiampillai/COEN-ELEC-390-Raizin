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
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PowerManager;
import android.support.design.widget.FloatingActionButton;
import android.os.Handler;
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
import android.support.v4.app.NotificationManagerCompat;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import static com.example.mario.raizin.DeviceList.EXTRA_ADDRESS;

public class HomeFeed extends AppCompatActivity {

    PowerManager.WakeLock wakeLock;


    //bluetooth
    BluetoothAdapter bluetoothAdapter = null;
    BluetoothAdapter myBluetooth= null;
    BluetoothSocket bluetoothSocket = null;
    Set<BluetoothDevice> pairedBluetoothDevices;
    String bluetoothAddress = null;
    String bluetoothDeviceName = null;
    Button uvButton;
    static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    int measuredUVIndex = -1;
    String bluetoothSerial=null;
    InputStream in;

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
        createNotificationChannel();

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

        try{
            connectBluetoothDevice();
        }catch(Exception exception){}

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "tag:");
        wakeLock.acquire();

        viewHandler.post(updateView);




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
        }catch(Exception e){ }

    }

    private void getInputData(){

        //InputStream in;
        int bytes; //number of bytes read
        byte[] buffer = new byte[4]; //read 4 bytes from bluetooth to store 1 float
        //String bluetoothSerial=null;
        try{
            if(!(bluetoothSocket == null)) {
                in = bluetoothSocket.getInputStream();
                in.read(buffer,0,4);

                bluetoothSerial = new String(buffer,0,4);
            }
            else{
                bluetoothSerial = null;
            }
        }catch(Exception exception){}
        Toast.makeText(getApplicationContext(),bluetoothSerial, Toast.LENGTH_SHORT).show();
    }



    void pushNotification(String title, String content) {
        NotificationManager NotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "1")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)

                .setAutoCancel(true);
        NotificationManager.notify(1, mBuilder.build());
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
            if(countDownText.getText().equals("0:01")){

            }
            if(countDownText.getText().equals("0:00")){
                if(timeLeftInMilliTimeOutside > totalReapplyTimeMilli) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pushNotification("Reapply", "It is time to reapply sunscreen");
                            TimerSetup("reapply", totalReapplyTimeMilli);
                        }
                    }, 1000);
                }
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
            if(timeOutsideText.getText().equals("0:01")){
                pushNotification("Finished!", "Your outdoor sesssion is complete.");
            }
        }

    }


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

    //Android documentation https://developer.android.com/training/notify-user/build-notification
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel_name";
            String description = "description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wakeLock.release();
    }

    Handler viewHandler = new Handler();
    //public EmulatorView mEmulatorView;
        Runnable updateView = new Runnable() {
            @Override
            public void run() {

                //mEmulatorView.invalidate();
                viewHandler.postDelayed(updateView, 2000);
                getInputData();

            }
        };
}
