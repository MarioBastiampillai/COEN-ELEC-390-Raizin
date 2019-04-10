package com.example.mario.raizin;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;


public class HomeFeed extends AppCompatActivity{

    PowerManager.WakeLock wakeLock;

    Button uvButton;
    double measuredUVIndex = 1;
    private ProgressDialog progress;

    private TextView countDownText;
    private TextView timeUntilReapplyTextView;
    public TextView UVDisplayObject;
    public TextView welcomeMessage;

    private CountDownTimer countDownTimer;
    private long timeLeftInMilliReapply; //SET this variable with max timer time
    private boolean timerRunning;
    TextView warningTextView;

    public int totalReapplyTimeMilli;
    String callingActivity;
    FloatingActionButton floatingActionButton;
    public TextView skinTypeDisplayObject;
    private DrawerLayout drawer;
    SharedPreferences sharedPreferences;
    Button stopTimerButton;
    Button lFbButton;
    String skinTypeGet;

    @Override
    public void onBackPressed()
    {
        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }else{

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_feed);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbarId);
        drawer=findViewById(R.id.drawer_layout);
        welcomeMessage=(TextView)findViewById(R.id.textView4);
        skinTypeDisplayObject=findViewById(R.id.skinTypeDisplay);
        uvButton = (Button) findViewById(R.id.uvButton);
        UVDisplayObject = (TextView) findViewById(R.id.UVDisplay);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButtonID);

        /*lFbButton = findViewById(R.id.login_button);
        lFbButton = (Button) findViewById(R.id.login_button);
        lFbButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent(getApplicationContext(), facebook.class);
                startActivity(intent);
            }
        });*/



        warningTextView = findViewById(R.id.warningTextView);
        countDownText = findViewById(R.id.countdown_text);
        timeUntilReapplyTextView = findViewById(R.id.timeUntilReapplyTextView);
        stopTimerButton = findViewById(R.id.stopTimerButton);


        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);


        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences=getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);


        String nameGiven=sharedPreferences.getString("nameKey", "");
        if(nameGiven != "")
        {
            welcomeMessage.setText("Welcome, " + nameGiven);  //was nameGiven
        }
        else
            welcomeMessage.setText("Welcome !");  //was nameGiven


        skinTypeGet = sharedPreferences.getString("skinTypeKey", "N/A");
        skinTypeDisplayObject.setText(skinTypeGet);

        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        int id = menuItem.getItemId();
                        switch (id) {
                            case R.id.nav_information:    //was navigation_item_1
                                //Do some thing here
                                // add navigation drawer item onclick method here
                                Intent intent = new Intent(getApplicationContext(), GeneralInformationActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.nav_shareToFacebook:
                                Intent intentFacebook = new Intent(getApplicationContext(), facebook.class);
                                startActivity(intentFacebook);
                                break;
                            case R.id.nav_logout:
                                if(timerRunning)
                                    Toast.makeText(getApplicationContext(),"You must stop the current timer before you can log out.",Toast.LENGTH_SHORT).show();
                                else{
                                    Intent intentLoginorSignUpActivity = new Intent(getApplicationContext(), LoginorSignUpActivity.class);
                                    startActivity(intentLoginorSignUpActivity);
                                    break;
                                }
                        }
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        drawer.closeDrawers();
                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        return true;
                    }
                });

        createNotificationChannel();

        if(measuredUVIndex >= 8)
            warningTextView.setVisibility(View.VISIBLE);
        else {
            warningTextView.setVisibility(View.INVISIBLE);
        }

        if(!timerRunning){
            stopTimerButton.setVisibility(View.GONE);
        }
        if (StateSingleton.instance().getUV()==null ){
            UVDisplayObject.setText("");
        }
        else {

            UVDisplayObject.setText("UV index: "+StateSingleton.instance().getUV());
        }
        Intent intent = getIntent();

        callingActivity = intent.getStringExtra("FROM_ACTIVITY");

        String callingActivity2 = "" + callingActivity;
        int timerStart;

        if (callingActivity2.length() == 11) {
            totalReapplyTimeMilli = intent.getIntExtra(Timer.EXTRA_REAPPLY_TIME, 0);
            timerStart = intent.getIntExtra(Timer.EXTRA_START_TIMER, 0);
        } else {
            totalReapplyTimeMilli = intent.getIntExtra(CustomTimer.EXTRA_REAPPLY_TIME, 0);
            timerStart = intent.getIntExtra(CustomTimer.EXTRA_START_TIMER, 0);
        }


        if (timerStart == 1) {
            TimerSetup("reapply", totalReapplyTimeMilli);
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
        countDownTimer = new CountDownTimer(timeLeftInMilliReapply, 1000) {
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

        if(((countDownText.getText().equals("0:00")) || (countDownText.getText().equals("00:00")) || (countDownText.getText().equals("000:00")))){
            if(timerRunning)
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
        countDownTimer.cancel();
    }


    @Override
    public void onResume(){
        super.onResume();
        if(StateSingleton.instance().getUV()!=null)
            measuredUVIndex = Double.parseDouble(StateSingleton.instance().getUV());
        if(measuredUVIndex >= 8)
            warningTextView.setVisibility(View.VISIBLE);
        else{
            warningTextView.setVisibility(View.INVISIBLE);
        }
    }

}
