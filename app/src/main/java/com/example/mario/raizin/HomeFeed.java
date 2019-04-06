package com.example.mario.raizin;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Handler;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
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
import java.util.Set;
import java.util.UUID;



public class HomeFeed extends AppCompatActivity{

    PowerManager.WakeLock wakeLock;

//    Handler viewHandler = new Handler();
    //private static final String TAG = "MyActivity";
    //Log.i(TAG, "exec1");
    // nick bluetooth
    //BluetoothAdapter bluetoothAdapter = null;
//    BluetoothAdapter myBluetooth = null;
    BluetoothSocket bluetoothSocket = null;
    Set<BluetoothDevice> pairedBluetoothDevices;
//    String address = null;
    String bluetoothDeviceName = null;
    Button uvButton;
//    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    double measuredUVIndex = -1;
//    String bluetoothSerial = null;
//    InputStream in;
//    private boolean isBtConnected = false;
//    BluetoothSocket btSocket = null;
    private ProgressDialog progress;

    private TextView countDownText;
    private TextView timeOutsideText;
    private TextView timeOutsideTimerTextView;
    private TextView timeUntilReapplyTextView;
    public TextView UVDisplayObject;
    public TextView welcomeMessage;

    private CountDownTimer countDownTimer;
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
    public TextView skinTypeDisplayObject;
    private DrawerLayout drawer;
    SharedPreferences sharedPreferences;                //creation of a SharedPreference object to be used to input data
    public static final String MyPREFERENCES="MyPrefs";
    public static final String Name="nameKey";
    public static final String selectedName="selectedNameKey";
    public static final String SkinType="skinTypeKey";
    Button stopTimerButton;
    String skinTypeGet;


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }*/


    @Override
    public void onBackPressed()
    {
        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

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
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbarId);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        drawer=findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        welcomeMessage=(TextView)findViewById(R.id.textView4);
        skinTypeDisplayObject=findViewById(R.id.skinTypeDisplay);

        //sharedPreferences=getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //String nameGiven=sharedPreferences.getString("goToHomeFeedName", null);
        //String nameGivenFromLogin=sharedPreferences.getString("goToHomeFeed", null);
        //String skinTypeGivenFromLogin=sharedPreferences.getString("skinTypeDisplay", null);

        Intent getName=getIntent();
        String nameGiven=getName.getStringExtra("passedNameToDeviceList");
        Intent getNameFromLogin=getIntent();
        String nameGivenFromLogin=getNameFromLogin.getStringExtra("goToHomeFeed");
        Intent getSkinTypeFromLogin=getIntent();
        String skinTypeGivenFromLogin=getSkinTypeFromLogin.getStringExtra("skinTypeDisplay");
        if(!TextUtils.isEmpty(nameGivenFromLogin))
        {
            welcomeMessage.setText("Welcome, "+nameGivenFromLogin);
        }
        else{
            welcomeMessage.setText("Welcome, "+nameGiven);  //was nameGiven
        }
        if(!TextUtils.isEmpty(skinTypeGivenFromLogin))
        {
            skinTypeDisplayObject.setText(skinTypeGivenFromLogin);
        }
        Intent skinTypeIntent=getIntent();
        currentScoreTrack=skinTypeIntent.getIntExtra("SCORE_TRACK",0);
        if (currentScoreTrack >= 0 && currentScoreTrack < 8){
            skinTypeGet = "Skin Type 1";
        }
        if (currentScoreTrack >= 8 && currentScoreTrack < 17){
            skinTypeGet = "Skin Type 2";
        }
        if (currentScoreTrack >= 17 && currentScoreTrack < 25){
            skinTypeGet = "Skin Type 3";
        }
        if (currentScoreTrack >= 25 && currentScoreTrack < 30){
            skinTypeGet = "Skin Type 4";
        }
        if (currentScoreTrack >= 30){
            skinTypeGet = "Skin Type V and VI";
        }
        skinTypeDisplayObject.setText(skinTypeGet);


        sharedPreferences=getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(selectedName, nameGivenFromLogin);
        editor.putString(Name, nameGiven);  //was nameGiven
        editor.putString(SkinType, skinTypeGet);
        editor.commit();

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
                                Intent intent1 = getIntent();
                                currentScoreTrack = intent1.getIntExtra("SCORE_TRACK", 0);
                                Intent intent = new Intent(getApplicationContext(), GeneralInformationActivity.class);
                                intent.putExtra("SCORE_TRACK", currentScoreTrack);
                                startActivity(intent);
                                break;
                            case R.id.nav_logout:
                                Intent intentLoginorSignUpActivity = new Intent(getApplicationContext(), LoginorSignUpActivity.class);
                                startActivity(intentLoginorSignUpActivity);
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

        //timeOutsideButton=(Button)findViewById(R.id.timeOutsideButtonID);
        uvButton = (Button) findViewById(R.id.uvButton);
        UVDisplayObject = (TextView) findViewById(R.id.UVDisplay);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButtonID);
        warningTextView = findViewById(R.id.warningTextView);
        if(measuredUVIndex >= 8)
            warningTextView.setVisibility(View.VISIBLE);
        else{
            warningTextView.setVisibility(View.INVISIBLE);
        }

        countDownText = findViewById(R.id.countdown_text);
        timeUntilReapplyTextView = findViewById(R.id.timeUntilReapplyTextView);
        stopTimerButton = findViewById(R.id.stopTimerButton);
        if(!timerRunning){
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
                //getInputData();
                //viewHandler.post(updateView);
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
        //new ConnectBT().execute();

//        try{
//            connectBluetoothDevice();
//        }catch(Exception exception){}

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "tag:");
        wakeLock.acquire();

//        viewHandler.post(updateView);


    }
//comment
//    //private EmulatorView mEmulatorView;
//    Runnable updateView = new Runnable() {
//        @Override
//        public void run() {
//            //mEmulatorView.invalidate();
//            viewHandler.postDelayed(updateView, 2000);
//            getInputData();
//
//        }
//    };

    //Find all bluetooth pairs and get their address and name
//    public void connectBluetoothDevice() throws IOException {
//        boolean ConnectSuccess = true;
//        try{
//
//            if(myBluetooth == null || !isBtConnected) {
//                myBluetooth = BluetoothAdapter.getDefaultAdapter();
//                BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);
//                btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
//                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
//                btSocket.connect();
//            }
//        }
//        catch (IOException e)
//        {
//            ConnectSuccess = false;//if the try failed, you can check the exception here
//        }
//
//    }

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


//    private void getInputData() {
//        //InputStream in;
//        int bytes; //number of bytes read
//        byte[] buffer = new byte[4]; //read 4 bytes from bluetooth to store 1 float
//        //String bluetoothSerial=null;
//        try {
//            if (!(btSocket == null)) {
//                in = btSocket.getInputStream();
//                in.read(buffer, 0, 4);
//
//                bluetoothSerial = new String(buffer, 0, 4);
//            } else {
//                bluetoothSerial = null;
//            }
//        } catch (Exception exception) {
//        }
//        //Toast.makeText(getApplicationContext(),bluetoothSerial, Toast.LENGTH_SHORT).show();
//        UVDisplayObject.setText(bluetoothSerial);
//    }

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

    //Handler viewHandler = new Handler();
    //public EmulatorView mEmulatorView;
        //Runnable updateView = new Runnable() {
            //@Override
            //public void run() {
//    private void Disconnect() {
//        if (btSocket != null) //If the btSocket is busy
//        {
//            try {
//                btSocket.close(); //close connection
//            } catch (IOException e) { //msg("Error");}
//            }
//            finish(); //return to the first layout
//
//        }
//    }

    /*@Override
    public void onBackPressed() {
        //do nothing
    }*/

    @Override
    public void onResume(){
        super.onResume();
        if(StateSingleton.instance().getUV()!=null)
            //measuredUVIndex = Integer.parseInt(StateSingleton.instance().getUV());
            measuredUVIndex = Double.parseDouble(StateSingleton.instance().getUV());
        if(measuredUVIndex >= 1)
            warningTextView.setVisibility(View.VISIBLE);
        else{
            warningTextView.setVisibility(View.INVISIBLE);
        }
    }

}

