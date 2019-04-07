package com.example.mario.raizin;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GeneralInformationActivity extends AppCompatActivity {

    Button shoppingButton;
    SharedPreferences sharedPreferences;

    String fitzpatrickType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        shoppingButton  = (Button)findViewById(R.id.shoppingButton);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_information);

        setContentView(R.layout.activity_general_information);
        TextView textView = (TextView) findViewById(R.id.textViewName);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        fitzpatrickType = sharedPreferences.getString("skinTypeKey", "N/A");
        textView.setText("Per the fitzpatrick scale you are of the " + fitzpatrickType);
    }

    public void clickMe(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.amazon.ca/s?k=sunscreen&ref=nb_sb_noss"));
        startActivity(intent);
    }
}
