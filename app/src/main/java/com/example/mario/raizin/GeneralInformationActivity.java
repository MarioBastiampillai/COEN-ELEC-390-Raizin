package com.example.mario.raizin;


import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GeneralInformationActivity extends AppCompatActivity {

    Button shoppingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        shoppingButton  = (Button)findViewById(R.id.shoppingButton);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_information);
    }

    public void clickMe(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.amazon.ca/s?k=sunscreen&ref=nb_sb_noss"));
        startActivity(intent);
    }
}
