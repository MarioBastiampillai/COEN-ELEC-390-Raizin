package com.example.mario.raizin;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class HomeFeed extends AppCompatActivity {

    PowerManager.WakeLock wakeLock;
    BluetoothSocket bluetoothSocket = null;
    Set<BluetoothDevice> pairedBluetoothDevices;
    String bluetoothDeviceName = null;
    Button uvButton;
    int measuredUVIndex = -1;
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
    TextView warningTextView;

    public int totalTimeOutsideMilli;
    public int totalReapplyTimeMilli;
    String callingActivity;
    //String fitzpatrickType = null;
    int currentScoreTrack;
    FloatingActionButton floatingActionButton;
    Button stopTimerButton;
    Button lFbButton;


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
                Intent intent1 = getIntent();
                currentScoreTrack = intent1.getIntExtra("SCORE_TRACK", 0);
                Intent intent = new Intent(getApplicationContext(), GeneralInformationActivity.class);
                intent.putExtra("SCORE_TRACK", currentScoreTrack);
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
        uvButton = (Button) findViewById(R.id.uvButton);
        UVDisplayObject = (TextView) findViewById(R.id.UVDisplay);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButtonID);

        lFbButton = findViewById(R.id.login_button);
        lFbButton = (Button) findViewById(R.id.login_button);
        lFbButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent(getApplicationContext(), facebook.class);
                startActivity(intent);
            }
        });



        warningTextView = findViewById(R.id.warningTextView);
        if (measuredUVIndex >= 8)
            warningTextView.setVisibility(View.VISIBLE);
        else {
            warningTextView.setVisibility(View.INVISIBLE);
        }

        countDownText = findViewById(R.id.countdown_text);
        timeUntilReapplyTextView = findViewById(R.id.timeUntilReapplyTextView);
        stopTimerButton = findViewById(R.id.stopTimerButton);
        if (!timerRunning) {
            stopTimerButton.setVisibility(View.GONE);
        }
        if (StateSingleton.instance().getUV()==null ){
            UVDisplayObject.setText("");
        }
        else {

            UVDisplayObject.setText(StateSingleton.instance().getUV());
        }
        Intent intent = getIntent();
        currentScoreTrack = intent.getIntExtra("SCORE_TRACK", 0);
        //address = intent.getStringExtra(DeviceList.EXTRA_ADDRESS);



        //SharedPreferences myPrefs = getSharedPreferences("prefID", Context.MODE_PRIVATE);

        //address = myPrefs.getString("device_add", null);

        callingActivity = intent.getStringExtra("FROM_ACTIVITY");
        //currentScoreTrack = intent.getIntExtra("SCORE_TRACK", 0);

        /*if (currentScoreTrack >= 0 && currentScoreTrack < 8){
            fitzpatrickType = "Type 1";
        }
        if (currentScoreTrack >= 8 && currentScoreTrack < 17){
            fitzpatrickType = "Type 2";
        }
        if (currentScoreTrack >= 17 && currentScoreTrack < 25){
            fitzpatrickType = "Type 3";
        }
        if (currentScoreTrack >= 25 && currentScoreTrack < 30){
            fitzpatrickType = "Type 4";
        }
        if (currentScoreTrack >= 30){
            fitzpatrickType = "Type V and VI";
        }
        setContentView(R.layout.activity_home_feed);
        TextView textView = (TextView) findViewById(R.id.textViewName);
        textView.setText("Per the fitzpatrick scale you are of the " + fitzpatrickType);
*/

        String callingActivity2 = "" + callingActivity;
        int timerStart;

        if (callingActivity2.length() == 11) {
            //totalTimeOutsideMilli = intent.getIntExtra(Timer.EXTRA_TIME_OUTSIDE, 0);
            totalReapplyTimeMilli = intent.getIntExtra(Timer.EXTRA_REAPPLY_TIME, 0);
            timerStart = intent.getIntExtra(Timer.EXTRA_START_TIMER, 0);
        } else {
            //totalTimeOutsideMilli = intent.getIntExtra(CustomTimer.EXTRA_TIME_OUTSIDE, 0);
            totalReapplyTimeMilli = intent.getIntExtra(CustomTimer.EXTRA_REAPPLY_TIME, 0);
            timerStart = intent.getIntExtra(CustomTimer.EXTRA_START_TIMER, 0);
        }


        if (timerStart == 1) {
            TimerSetup("reapply", totalReapplyTimeMilli);
            //TimerSetup("timeOutside", totalTimeOutsideMilli);
        }


        uvButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                startActivity(intent);
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timerRunning)
                    Toast.makeText(getApplicationContext(),"You must stop the current timer before you can set another one.",Toast.LENGTH_SHORT).show();
                else{
                    if(measuredUVIndex == -1){
                        Toast.makeText(getApplicationContext(),"You must measure the UV index before setting a Timer", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        //Disconnect();
                        Intent intent = new Intent(getApplicationContext(), Timer.class);
                        startActivity(intent);
                    }
                }
            }
        });

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "tag:");
        wakeLock.acquire();

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

    public void startTimer(String timer) {
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
        stopTimerButton.setVisibility(View.VISIBLE);
        timerRunning = true;
    }

    public void updateTimer(String timer) {
        int minutes = (int) timeLeftInMilliReapply / 60000;
        int seconds = (int) timeLeftInMilliReapply % 60000 / 1000;

        String timeleftText;
        timeleftText = "" + minutes;
        timeleftText += ":";
        if (seconds < 10)
            timeleftText += "0";
        timeleftText += seconds;

        countDownText.setText(timeleftText);
        if(countDownText.getText().equals("0:01")){

        }
        if(((countDownText.getText().equals("0:00")) || (countDownText.getText().equals("00:00")) || (countDownText.getText().equals("000:00"))) && timerRunning){
            pushNotification("Reapply", "It is time to reapply sunscreen");
            TimerSetup("reapply", totalReapplyTimeMilli + 1000);
        }
    }


    public void TimerSetup(String timer, int time){
        timeLeftInMilliReapply = time;
        startTimer(timer);
        timeUntilReapplyTextView.setText("Time until next Reapply:");
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

    public void stopTimer(View view){
        timerRunning = !timerRunning;
        stopTimerButton.setVisibility(View.INVISIBLE);
        countDownText.setVisibility(View.INVISIBLE);
        timeUntilReapplyTextView.setVisibility(View.INVISIBLE);
        timeLeftInMilliReapply = 0;
    }


    @Override
    public void onBackPressed() {
        //do nothing
    }
}

