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
import android.widget.Toast;

public class GeneralInformationActivity extends AppCompatActivity {
    Button shoppingButton;
    String buttonTxt = null;
    String fitzpatrickType = null;
    String gii1 = null;
    String gii2 = null;
    String gii3 = null;
    String link = null;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_information);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        fitzpatrickType = sharedPreferences.getString("skinTypeKey", "N/A");
        gii1 = sharedPreferences.getString("gii1Key", "N/A");
        gii2 = sharedPreferences.getString("gii2Key", "N/A");
        gii3 = sharedPreferences.getString("gii3Key", "N/A");
        link = sharedPreferences.getString("linkKey", "N/A");
        buttonTxt = sharedPreferences.getString("buttonTxtKey", "N/A");


        TextView textView = findViewById(R.id.fitzDisplay);
        textView.setText("Per the fitzpatrick scale you are of the " + fitzpatrickType);

        TextView textView1 = findViewById(R.id.gi1);
        textView1.setText(gii1);

        TextView textView2 = findViewById(R.id.gi2);
        textView2.setText(gii2);

        TextView textView3 = findViewById(R.id.gi3);
        textView3.setText(gii3);

        shoppingButton  = findViewById(R.id.shoppingButton);
        shoppingButton.setText(buttonTxt);
        Toast.makeText(getApplicationContext(),buttonTxt, Toast.LENGTH_SHORT).show();

        shoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(intent);
            }
        });

    }
}
