package com.example.mario.raizin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Timer extends AppCompatActivity {

    public int timeOutsideInt;
    public String timeOutsideString;
    public int recommendedTime = 5; //Hard code 5 min reapply time for now
    public int reapplyTime;

    public static final String EXTRA_TIME_OUTSIDE = "com.example.mario.raisin.EXTRA_TIME_OUTSIDE";
    public static final String EXTRA_REAPPLY_TIME = "com.example.mario.raisin.EXTRA_REAPPLY_TIME";
    public static final String EXTRA_START_TIMER = "com.example.mario.raisin.EXTRA_START_TIMER";

    TextView testIntent;
    TextView recommendedTimeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        Intent intent = getIntent();

        timeOutsideInt = intent.getIntExtra(TimeOutsideActivity.EXTRA_TIME_OUTSIDE, 0);
        System.out.println("NICK HERE IS TIME OUTSIDE PRINTED");
        System.out.println(timeOutsideInt);
        testIntent = findViewById(R.id.testIntent);
        recommendedTimeTextView = findViewById(R.id.recommendedTimeTextView);

        timeOutsideString = Integer.toString(timeOutsideInt);
        testIntent.setText(timeOutsideString);
        recommendedTimeTextView.setText(Integer.toString(recommendedTime));

    }

    public void recommendedClick(View view){
        reapplyTime = minutesToMilliseconds(recommendedTime);
        int timeOutsidemilli = minutesToMilliseconds(timeOutsideInt);
        Intent intent = new Intent(getApplicationContext(),HomeFeed.class);
        intent.putExtra(EXTRA_TIME_OUTSIDE, timeOutsidemilli);
        intent.putExtra(EXTRA_REAPPLY_TIME, reapplyTime);
        intent.putExtra(EXTRA_START_TIMER,1);
        intent.putExtra("FROM_ACTIVITY", "recommended");
        startActivity(intent);
    }

    public void customClick(View view){
        Intent intent = new Intent(getApplicationContext(),CustomTimer.class);
        intent.putExtra("EXTRA_TIME_OUTSIDE", timeOutsideInt);
        startActivity(intent);
    }

    public int minutesToMilliseconds(int minutes){
        return minutes * 60000;
    }

}
