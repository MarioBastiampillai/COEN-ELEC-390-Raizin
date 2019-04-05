package com.example.mario.raizin;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TimeOutsideActivity extends AppCompatActivity {
    Button startTimerObject;
    String timeOutside="";
    int timeOutsideNumber = 0;
    EditText timeOutsideEditText;

    public static final String EXTRA_TIME_OUTSIDE = "com.example.mario.raizin.EXTRA_TIME_OUTSIDE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_outside);
        startTimerObject=(Button)findViewById(R.id.startTimer);
        timeOutsideEditText = findViewById(R.id.timeOutsideEditText);

        startTimerObject.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                timeOutside = timeOutsideEditText.getText().toString();
                if(timeOutside.isEmpty() || timeOutside.length() == 0 || timeOutside.equals("") || timeOutside == null){
                    Toast.makeText(getApplicationContext(),"Enter a valid time.",Toast.LENGTH_SHORT).show();
                }
                else{
                    timeOutsideNumber = Integer.parseInt(timeOutside);
                    if(timeOutsideNumber > 0) {
                        Intent intent = new Intent(getApplicationContext(), Timer.class);
                        intent.putExtra(EXTRA_TIME_OUTSIDE, timeOutsideNumber);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Enter a valid time.",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}
