package com.example.mario.raizin;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TimeOutsideActivity extends AppCompatActivity {
    Button thirtyMinButtonObject;
    Button fourtyFiveMinButtonObject;
    Button oneHourButtonObject;
    Button twoHourButtonObject;
    Button startTimerObject;
    String timeOutside="";
    int timeOutsideNumber;

    public static final String EXTRA_TIME_OUTSIDE = "com.example.mario.raizin.EXTRA_TIME_OUTSIDE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_outside);
        thirtyMinButtonObject=(Button)findViewById(R.id.thirtyMinButton);
        fourtyFiveMinButtonObject=(Button)findViewById(R.id.fourtyFiveMinButton);
        oneHourButtonObject=(Button)findViewById(R.id.oneHourButton);
        twoHourButtonObject=(Button)findViewById(R.id.twoHourButton);
        startTimerObject=(Button)findViewById(R.id.startTimer);
        thirtyMinButtonObject.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                timeOutside="30 minutes";
                timeOutsideNumber=30;


            }
        });
        fourtyFiveMinButtonObject.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                timeOutside="45 minutes";
                timeOutsideNumber=45;

            }
        });
        oneHourButtonObject.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                timeOutside="one hours";
                timeOutsideNumber=60;

            }
        });
        twoHourButtonObject.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                timeOutside="two hours";
                timeOutsideNumber=120;

            }
        });
        startTimerObject.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
/*
                //estimatedTimeOfSunscreenDuration in minutes
                //30 second timer
                //need to pass the time outside variable as milliseconds in here
                //need to pass the estimated time variable as milliseconds in here
                //convert the estimatedTimeOfSunscreenDuration in milliseconds, multiply minutes by 60000
                Intent inTimer=getIntent();
                int estimatedTime=inTimer.getIntExtra("estimatedTime",0);
                //convert estimatedTime from hours to milliseconds
                int convertedTime=estimatedTime*(3600000);
                new CountDownTimer(convertedTime, 1000) {

                    public void onTick(long millisUntilFinished) {
                        //millisUntilFinished / 1000;
                        //send a notification once the time has passed

                    }

                    public void onFinish() {
                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                        // Vibrate for 4000 milliseconds
                        v.vibrate(4000);
                    }
                }.start();
*/

                Intent intent = new Intent(getApplicationContext(), Timer.class);
                intent.putExtra(EXTRA_TIME_OUTSIDE,timeOutsideNumber);
                startActivity(intent);

            }
        });

    }
}
