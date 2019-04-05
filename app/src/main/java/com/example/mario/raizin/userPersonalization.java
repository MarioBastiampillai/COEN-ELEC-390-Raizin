package com.example.mario.raizin;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.Toast;




//public abstract class userPersonalization extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
public class userPersonalization extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    //Genetic disposition questions
    Spinner GDspinner1;
    Spinner GDspinner2;
    Spinner GDspinner3;
    Spinner GDspinner4;

    //Reaction to Sun Exposure
    Spinner REspinner1;
    Spinner REspinner2;
    Spinner REspinner3;
    Spinner REspinner4;

    //Tanning habits
    Spinner THspinner1;
    Spinner THspinner2;

    Button validationBtn;

    int scoreTrack = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_personalization);

        // SPINNER 1 - GDSPINNER1
        GDspinner1 = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.GDone,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
        GDspinner1.setAdapter(adapter);
        GDspinner1.setOnItemSelectedListener(this);

        // SPINNER 2 - GDSPINNER2
        GDspinner2 = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.GDtwo,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
        GDspinner2.setAdapter(adapter2);
        GDspinner2.setOnItemSelectedListener(this);

        // SPINNER 3 - GDSPINNER3
        GDspinner3 = findViewById(R.id.spinner3);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,R.array.GDthree,android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
        GDspinner3.setAdapter(adapter3);
        GDspinner3.setOnItemSelectedListener(this);

        // SPINNER 4 - GDSPINNER4
        GDspinner4 = findViewById(R.id.spinner4);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this,R.array.GDfour,android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
        GDspinner4.setAdapter(adapter4);
        GDspinner4.setOnItemSelectedListener(this);

        // SPINNER 5 - RESPINNER1
        REspinner1 = findViewById(R.id.spinner5);
        ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(this,R.array.REone,android.R.layout.simple_spinner_item);
        adapter5.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
        REspinner1.setAdapter(adapter5);
        REspinner1.setOnItemSelectedListener(this);

        // SPINNER 6 - RESPINNER2
        REspinner2 = findViewById(R.id.spinner6);
        ArrayAdapter<CharSequence> adapter6 = ArrayAdapter.createFromResource(this,R.array.REtwo,android.R.layout.simple_spinner_item);
        adapter6.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
        REspinner2.setAdapter(adapter6);
        REspinner2.setOnItemSelectedListener(this);

        // SPINNER 7 - RESPINNER3
        REspinner3 = findViewById(R.id.spinner7);
        ArrayAdapter<CharSequence> adapter7 = ArrayAdapter.createFromResource(this,R.array.RE3,android.R.layout.simple_spinner_item);
        adapter7.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
        REspinner3.setAdapter(adapter7);
        REspinner3.setOnItemSelectedListener(this);

        // SPINNER 8 - RESPINNER4
        REspinner4 = findViewById(R.id.spinner8);
        ArrayAdapter<CharSequence> adapter8 = ArrayAdapter.createFromResource(this,R.array.RE4,android.R.layout.simple_spinner_item);
        adapter8.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
        REspinner4.setAdapter(adapter8);
        REspinner4.setOnItemSelectedListener(this);

        // SPINNER 9 - THSPINNER1
        THspinner1 = findViewById(R.id.spinner9);
        ArrayAdapter<CharSequence> adapter9 = ArrayAdapter.createFromResource(this,R.array.THone,android.R.layout.simple_spinner_item);
        adapter9.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
        THspinner1.setAdapter(adapter9);
        THspinner1.setOnItemSelectedListener(this);

        // SPINNER 10 - THSPINNER2
        THspinner2 = findViewById(R.id.spinner10);
        ArrayAdapter<CharSequence> adapter10 = ArrayAdapter.createFromResource(this,R.array.THtwo,android.R.layout.simple_spinner_item);
        adapter10.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
        THspinner2.setAdapter(adapter10);
        THspinner2.setOnItemSelectedListener(this);

        validationBtn = findViewById(R.id.button5);
        validationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3=getIntent();
                String passedNameString=intent3.getStringExtra("passedName");
                int sScore1 = (int) GDspinner1.getSelectedItemId();
                int sScore2 = (int) GDspinner2.getSelectedItemId();
                int sScore3 = (int) GDspinner3.getSelectedItemId();
                int sScore4 = (int) GDspinner4.getSelectedItemId();

                int sScore5 = (int) REspinner1.getSelectedItemId();
                int sScore6 = (int) REspinner2.getSelectedItemId();
                int sScore7 = (int) REspinner3.getSelectedItemId();
                int sScore8 = (int) REspinner4.getSelectedItemId();

                int sScore9 = (int) THspinner1.getSelectedItemId();
                int sScore10 = (int) THspinner2.getSelectedItemId();

                if (sScore1 > 0 && sScore2 > 0
                        && sScore3 > 0 && sScore4 > 0
                        && sScore5 > 0 && sScore6 > 0
                        && sScore7 > 0 && sScore8 > 0
                        && sScore9 > 0 && sScore10 > 0){

                    scoreTrack = sScore1 + sScore2
                            + sScore3 + sScore4
                            + sScore5 + sScore6
                            + sScore7 + sScore8
                            + sScore9 + sScore10 -10;

                    Toast.makeText(getApplicationContext(), String.valueOf(scoreTrack), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), HomeFeed.class);
                    intent.putExtra("SCORE_TRACK", scoreTrack);
                    intent.putExtra("passedNameToDeviceList", passedNameString);
                    startActivity(intent);
                    //currentScoreTrack = intent.getIntExtra("SCORE_TRACK", 0); INSIDE THE HOMEFEED PAGE
                }
            }
        });
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){

        String GDone = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), GDone, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent){
    }



}
