package com.example.mario.raizin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Timer extends AppCompatActivity {

    public int timeOutsideInt;
    public String timeOutsideString;
    public int recommendedTime; //Hard code 5 min reapply time for now
    public int reapplyTime;
    public double UVindex;
    SharedPreferences sharedPreferences;
    public int fitzpatrickType;

    public static final String EXTRA_TIME_OUTSIDE = "com.example.mario.raisin.EXTRA_TIME_OUTSIDE";
    public static final String EXTRA_REAPPLY_TIME = "com.example.mario.raisin.EXTRA_REAPPLY_TIME";
    public static final String EXTRA_START_TIMER = "com.example.mario.raisin.EXTRA_START_TIMER";

    TextView recommendedTimeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        Intent intent = getIntent();

        timeOutsideInt = intent.getIntExtra(TimeOutsideActivity.EXTRA_TIME_OUTSIDE, 0);
        System.out.println("NICK HERE IS TIME OUTSIDE PRINTED");
        System.out.println(timeOutsideInt);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        fitzpatrickType = sharedPreferences.getInt("scoreKey",0);



        if (StateSingleton.instance().getUV()!=null ){
            UVindex =  Double.parseDouble(StateSingleton.instance().getUV());
        }

        // SKIN TYPE 1 - 2 - 3 ***
        if(UVindex >= 0 && UVindex <2 && fitzpatrickType >= 0 && fitzpatrickType <25){
            recommendedTime = 120;
        }
        if(UVindex >= 2 && UVindex <15 && fitzpatrickType >= 0 && fitzpatrickType <25){
            recommendedTime = 90;
        }
        // SKIN TYPE 1 - 2 - 3 ***


        // SKIN TYPE 4 ***
        if(UVindex >= 0 && UVindex <4 && fitzpatrickType >= 25 && fitzpatrickType <30){
            recommendedTime = 120;
        }
        if(UVindex >= 4 && UVindex <15 && fitzpatrickType >= 25 && fitzpatrickType <30){
            recommendedTime = 90;
        }


        // SKIN TYPE 5 - 6 ***
        if(UVindex >= 0 && UVindex <6 && fitzpatrickType >= 30){
            recommendedTime = 120;
        }
        if(UVindex >= 6 && UVindex <15 && fitzpatrickType >= 30){
            recommendedTime = 90;
        }
        // SKIN TYPE 5 - 6 ***

        recommendedTimeTextView = findViewById(R.id.recommendedTimeTextView);

        timeOutsideString = Integer.toString(timeOutsideInt);

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
