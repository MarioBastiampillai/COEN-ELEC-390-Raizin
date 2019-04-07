package com.example.mario.raizin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class indexPage extends AppCompatActivity {

    Button setUpProfileBtnObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_page);
        setUpProfileBtnObject = findViewById(R.id.setUpProfileBtn);

        setUpProfileBtnObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), userPersonalization.class);
                intent.putExtra("nameKey", getIntent().getStringExtra("nameKey"));
                startActivity(intent);

            }
        });

    }
}
