package com.example.mario.raizin;


import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GeneralInformationActivity extends AppCompatActivity {

    Button shoppingButton;

    String fitzpatrickType = null;
    int currentScoreTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        shoppingButton  = (Button)findViewById(R.id.shoppingButton);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_information);

        Intent intent = getIntent();
        currentScoreTrack = intent.getIntExtra("SCORE_TRACK", 0);

        if (currentScoreTrack >= 0 && currentScoreTrack < 8){
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

        setContentView(R.layout.activity_general_information);
        TextView textView = (TextView) findViewById(R.id.textViewName);
        textView.setText("Per the fitzpatrick scale you are of the " + fitzpatrickType);
    }

    public void clickMe(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.amazon.ca/s?k=sunscreen&ref=nb_sb_noss"));
        startActivity(intent);
    }
}
