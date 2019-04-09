package com.example.mario.raizin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CustomTimer extends AppCompatActivity {

    public static final String EXTRA_TIME_OUTSIDE = "com.example.mario.raisin.EXTRA_TIME_OUTSIDE";
    public static final String EXTRA_REAPPLY_TIME = "com.example.mario.raisin.EXTRA_REAPPLY_TIME";
    public static final String EXTRA_START_TIMER = "com.example.mario.raisin.EXTRA_START_TIMER";

    EditText customMinutesEditText;
    Button cancelButton;
    Button setTimerButton;

    int timeOutsideMinutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_timer);
        customMinutesEditText = findViewById(R.id.customMinutesEditText);
        cancelButton = findViewById(R.id.cancelButton);
        setTimerButton = findViewById(R.id.setTimerButton);

        Intent intent = getIntent();
        timeOutsideMinutes = intent.getIntExtra("EXTRA_TIME_OUTSIDE", 0);
        System.out.println("NICK HERES TIME OUTSIDE INT");
        System.out.println(timeOutsideMinutes);
    }

    public void cancelClick(View view){
        Intent intent = new Intent(getApplicationContext(), HomeFeed.class);
        startActivity(intent);
    }

    public void setTimerClick(View view){
        String reapplyTimeMinutesString = customMinutesEditText.getText().toString();

        int mins = Integer.parseInt(reapplyTimeMinutesString);
        if(mins > 1440){
            Toast.makeText(getApplicationContext(),"Maximum time allowed is 1,440 minutes.",Toast.LENGTH_SHORT).show();
        }
        if(reapplyTimeMinutesString.isEmpty() || reapplyTimeMinutesString.length() == 0 || reapplyTimeMinutesString.equals("") || reapplyTimeMinutesString == null){
            Toast.makeText(getApplicationContext(),"Enter a valid time.",Toast.LENGTH_SHORT).show();
        }

        else{
            int reapplyTimeMinutes = Integer.parseInt(reapplyTimeMinutesString);
            if(reapplyTimeMinutes > 0) {
                int reapplyTime = minutesToMilliseconds(reapplyTimeMinutes);
                int timeOutsidemilli = minutesToMilliseconds(timeOutsideMinutes);
                Intent intent = new Intent(getApplicationContext(), HomeFeed.class);
                intent.putExtra(EXTRA_TIME_OUTSIDE, timeOutsidemilli);
                intent.putExtra(EXTRA_REAPPLY_TIME, reapplyTime);
                intent.putExtra(EXTRA_START_TIMER,1);
                intent.putExtra("FROM_ACTIVITY", "recommended");

                startActivity(intent);
            }
            else{
                Toast.makeText(getApplicationContext(),"Enter a valid time.",Toast.LENGTH_SHORT).show();
            }
        }



    }

    public int minutesToMilliseconds(int minutes){
        return minutes * 60000;
    }

}
