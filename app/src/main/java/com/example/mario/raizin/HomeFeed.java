package com.example.mario.raizin;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.UUID;

public class HomeFeed extends AppCompatActivity {

    //Button generalInformationButton;
    Button timeOutsideButton;

    Handler viewHandler = new Handler();
    //private static final String TAG = "MyActivity";
    //Log.i(TAG, "exec1");
    // nick bluetooth
    //BluetoothAdapter bluetoothAdapter = null;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket bluetoothSocket = null;
    Set<BluetoothDevice> pairedBluetoothDevices;
    String address = null;
    String bluetoothDeviceName = null;
    Button uvButton;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    int measuredUVIndex = -1;
    String bluetoothSerial = null;
    InputStream in;
    private boolean isBtConnected = false;
    BluetoothSocket btSocket = null;
    private ProgressDialog progress;

    private TextView countDownText;
    private TextView timeOutsideText;
    private TextView timeOutsideTimerTextView;
    private TextView timeUntilReapplyTextView;
    public TextView UVDisplayObject;

    private CountDownTimer countdownTimer;
    private long timeLeftInMilliReapply; //SET this variable with max timer time
    private long timeLeftInMilliTimeOutside; //set this variable with time outside
    private boolean timerRunning;

    public int totalTimeOutsideMilli;
    public int totalReapplyTimeMilli;
    String callingActivity;
    FloatingActionButton floatingActionButton;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item2:
                Intent intent = new Intent(getApplicationContext(), GeneralInformationActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    @Override
//    protected void onStart()
//    {
//        super.onStart();
//        viewHandler.post(updateView);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_feed);

        //timeOutsideButton=(Button)findViewById(R.id.timeOutsideButtonID);
        uvButton = (Button) findViewById(R.id.uvButton);
        UVDisplayObject = (TextView) findViewById(R.id.UVDisplay);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButtonID);


        countDownText = findViewById(R.id.countdown_text);
        timeOutsideText = findViewById(R.id.timeOutsideText);
        timeUntilReapplyTextView = findViewById(R.id.timeUntilReapplyTextView);
        timeOutsideTimerTextView = findViewById(R.id.timeOutsideTimerTextView);

        Intent intent = getIntent();

        address = intent.getStringExtra(DeviceList.EXTRA_ADDRESS);

        //SharedPreferences myPrefs = getSharedPreferences("prefID", Context.MODE_PRIVATE);

        //address = myPrefs.getString("device_add", null);

        callingActivity = intent.getStringExtra("FROM_ACTIVITY");
        String callingActivity2 = "" + callingActivity;
        System.out.println("NICK HERES THE CALLING ACTIVITY:");
        System.out.println(callingActivity);
        int timerStart;

        if (callingActivity2.length() == 11) {
            totalTimeOutsideMilli = intent.getIntExtra(Timer.EXTRA_TIME_OUTSIDE, 0);
            totalReapplyTimeMilli = intent.getIntExtra(Timer.EXTRA_REAPPLY_TIME, 0);
            timerStart = intent.getIntExtra(Timer.EXTRA_START_TIMER, 0);
        } else {
            totalTimeOutsideMilli = intent.getIntExtra(CustomTimer.EXTRA_TIME_OUTSIDE, 0);
            totalReapplyTimeMilli = intent.getIntExtra(CustomTimer.EXTRA_REAPPLY_TIME, 0);
            timerStart = intent.getIntExtra(CustomTimer.EXTRA_START_TIMER, 0);
        }


        if (timerStart == 1) {
            TimerSetup("reapply", totalReapplyTimeMilli);
            TimerSetup("timeOutside", totalTimeOutsideMilli);
        }


        uvButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                //getInputData();
                viewHandler.post(updateView);
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Disconnect();
                Intent intent = new Intent(getApplicationContext(), TimeOutsideActivity.class);
                startActivity(intent);
            }
        });
        //new ConnectBT().execute();

        try{
            connectBluetoothDevice();
        }catch(Exception exception){}

        viewHandler.post(updateView);


    }

    //public EmulatorView mEmulatorView;
    Runnable updateView = new Runnable() {
        @Override
        public void run() {
            //mEmulatorView.invalidate();
            viewHandler.postDelayed(updateView, 2000);
            getInputData();

        }
    };

    //Find all bluetooth pairs and get their address and name
    public void connectBluetoothDevice() throws IOException {
        boolean ConnectSuccess = true;
        try{

            if(myBluetooth == null || !isBtConnected) {
                myBluetooth = BluetoothAdapter.getDefaultAdapter();
                BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);
                btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                btSocket.connect();
            }
        }
        catch (IOException e)
        {
            ConnectSuccess = false;//if the try failed, you can check the exception here
        }

    }

//    public class ConnectBT extends AsyncTask<Void, Void, Void> {
//        public boolean ConnectSuccess = true;
//
//        @Override
//        protected void onPreExecute() {
//            progress = ProgressDialog.show(HomeFeed.this, "Connecting...", "Please Wait!!!");
//        }
//
//        @Override
//        protected Void doInBackground(Void... devices) {
//            try {
//                if (btSocket == null || !isBtConnected) {
//                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
//                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);
//                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
//                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
//                    btSocket.connect();
//                    ConnectSuccess = true;
//
//                }
//            } catch (IOException e) {
//                ConnectSuccess = false;
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//
//            if (!ConnectSuccess) {
//                //msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
//                //finish();
//            } else {
//                //msg("Connected");
//                isBtConnected = true;
//            }
//
//            progress.dismiss();
//        }
//    }


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

    void pushNotification(String title, String content) {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "default")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true);
        mNotificationManager.notify(0, mBuilder.build());
    }

    public void startTimer(String timer) {
        if (timer == "reapply") {
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
        } else {
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

    public void updateTimer(String timer) {
        if (timer == "reapply") {
            int minutes = (int) timeLeftInMilliReapply / 60000;
            int seconds = (int) timeLeftInMilliReapply % 60000 / 1000;

            String timeleftText;
            timeleftText = "" + minutes;
            timeleftText += ":";
            if (seconds < 10)
                timeleftText += "0";
            timeleftText += seconds;

            countDownText.setText(timeleftText);
            if (countDownText.getText().equals("0:00")) {
                pushNotification("reapply bitch", "lol");
                if (timeLeftInMilliTimeOutside > totalReapplyTimeMilli)
                    TimerSetup("reapply", totalReapplyTimeMilli);
            }
        } else {
            int minutes = (int) timeLeftInMilliTimeOutside / 60000;
            int seconds = (int) timeLeftInMilliTimeOutside % 60000 / 1000;

            String timeleftText;
            timeleftText = "" + minutes;
            timeleftText += ":";
            if (seconds < 10)
                timeleftText += "0";
            timeleftText += seconds;

            timeOutsideText.setText(timeleftText);
            if (timeOutsideText.getText().equals("0:00")) {
                pushNotification("Finished!", "Your outdoor sesssion is complete.");
            }
        }

    }

    ;

    public void TimerSetup(String timer, int time) {
        if (timer == "reapply") {
            timeLeftInMilliReapply = time;
            startTimer(timer);
            timeUntilReapplyTextView.setText("Time until next Reapply:");
        } else {
            timeLeftInMilliTimeOutside = time;
            startTimer(timer);
            timeOutsideTimerTextView.setText("Remaining time in outdoor session:");
        }
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

}

