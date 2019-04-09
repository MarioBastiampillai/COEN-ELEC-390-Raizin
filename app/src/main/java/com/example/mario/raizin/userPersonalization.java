package com.example.mario.raizin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

    SharedPreferences sharedPreferences;

    public void skinTypeDetermination (int currentScoreTrack){
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        String fitzpatrickType = "N/A";
        String gii1 = "N/A";
        String gii2 = "N/A";
        String gii3 = "N/A";
        String link = "N/A";
        String buttonTxt = "N/A";
        if (currentScoreTrack >= 0 && currentScoreTrack < 8){
            fitzpatrickType = "Type 1";
            link = "https://www.amazon.ca/Banana-Boat-Performance-Ultra-Lightweight-Sunscreen/dp/B01BM5UP4C/ref=cm_cr_arp_d_product_top?ie=UTF8";

            gii1 = "This skin type is believed to be highly susceptible to premature ageing and skin cancers " +
                    "and one is therefore advised to take extreme care, " +
                    "use sunscreen and protect yourself from harmful UV rays as extreme sun exposure can result in serious damage";

            gii2 = "Individuals with this skin type should stay out of the sun during the peak hours of 10am - 4pm," +
                    "wear a high SPF 50 sunscreen every day and re apply it at lunchtime";

            gii3 = "Wear sunglasses that provide UVA protection as UVA exposure can cause cataracts. " +
                    "Tanning beds and booth are prohibited and contraindicated at all times as they have a high intensity of UV output ";
            buttonTxt = "Shop for SPF 30!";
        }
        if (currentScoreTrack >= 8 && currentScoreTrack < 17){
            fitzpatrickType = "Type 2";
            link = "https://www.amazon.ca/Banana-Boat-Performance-Ultra-Lightweight-Sunscreen/dp/B01BM5UP4C/ref=cm_cr_arp_d_product_top?ie=UTF8";

            gii1 = "This skin type manage to get a light tan with repeated exposure to sun." +
                    " These skin types are therefore advised to take extreme care, " +
                    "use sunscreen and protect themselves from harmful UV rays as extreme sun exposure can result in serious damage";

            gii2 = "Individuals with this skin type should stay out of the sun during the peak hours of 10am - 4pm, " +
                    "wear a high SPF 50 sunscreen every day and re apply it at lunchtime";

            gii3 = "Wear sunglasses that provide UVA protection as UVA exposure can cause cataracts. " +
                    "Avoid tanning beds and booth at all times as they have a high intensity of UV output";
            buttonTxt = "Shop for SPF 30!";

        }
        if (currentScoreTrack >= 17 && currentScoreTrack < 25){
            fitzpatrickType = "Type 3";
            link = "https://www.amazon.ca/Banana-Boat-Performance-Ultra-Lightweight-Sunscreen/dp/B01BM5UKQU/" +
                    "ref=sr_1_6?keywords=spf+30&qid=1554603584&refinements=p_72%3A11192170011&rnid=11192166011&s=gateway&sr=8-6";

            gii1 = "This skin sometimes burns and may tan to a light bronze." +
                    "These skin types are therefore advised to take care, " +
                    "use sunscreen and protect themselves from harmful UV rays as extreme sun exposure can result in serious damage";

            gii2 = "Individuals with this skin type should stay out of the sun during the peak hours of 10am - 4pm, " +
                    "wear a SPF 30 sunscreen every day and re apply it at lunchtime";

            gii3 = "Wear sunglasses that provide UVA protection as UVA exposure can cause cataracts. " +
                    "Avoid tanning beds and booth at all times as they have a high intensity of UV output";
            buttonTxt = "Shop for SPF 30!";

        }
        if (currentScoreTrack >= 25 && currentScoreTrack < 30){
            fitzpatrickType = "Type 4";
            link = "https://www.amazon.ca/Hawaiian-Tropic-Sheer-Sunscreen-Lotion/dp/B00BP2H6SG/ref=sr_1_5?keywords=spf+15&qid=1554603930&s=gateway&sr=8-5";

            gii1 = "This skin type typically tan with ease and seldom get burned." +
                    "They should still take care, use sunscreen and protect themselves from harmful UV rays as extreme sun exposure can result in serious damage, " +
                    "uneven skin tone, premature ageing and possible skin cancers.";

            gii2 = "Individuals with this skin type should stay out of the sun during the peak hours of 10am - 4pm, and wear a SPF 15 sunscreen every day.";

            gii3 = "Wear sunglasses that provide UVA protection as UVA exposure can cause cataracts.";
            buttonTxt = "Shop for SPF 15!";

        }
        if (currentScoreTrack >= 30){
            fitzpatrickType = "Type V and VI";
            link = "https://www.amazon.ca/Hawaiian-Tropic-Sheer-Sunscreen-Lotion/dp/B00BP2H6SG/ref=sr_1_5?keywords=spf+15&qid=1554603930&s=gateway&sr=8-5";

            gii1 = "This skin type tan easily and very rarely burn. " +
                    "Individuals with this skin type should protect themselves from the sun as chronic sun exposure leads to uneven skin tone and pigmentation.";
            gii2 = " Individuals with this skin type should stay out of the sun during the peak hours of 10am - 4pm, " +
                    "and wear a SPF 15 to 30 sunscreen every day";

            gii3 = "Wear sunglasses that provide UVA protection as UVA exposure can cause cataracts.";
            buttonTxt = "Shop for SPF 15!";

        }

        sharedPreferences.edit().putInt("scoreKey", currentScoreTrack).apply();
        sharedPreferences.edit().putString("skinTypeKey", fitzpatrickType).apply();

        sharedPreferences.edit().putString("gii1Key", gii1).apply();
        sharedPreferences.edit().putString("gii2Key", gii2).apply();
        sharedPreferences.edit().putString("gii3Key", gii3).apply();
        sharedPreferences.edit().putString("linkKey", link).apply();
        sharedPreferences.edit().putString("buttonTxtKey", buttonTxt).apply();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_personalization);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

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

                    skinTypeDetermination(scoreTrack);

                    sharedPreferences.edit().putString("nameKey", getIntent().getStringExtra("nameKey")).apply();

                    Intent intent = new Intent(getApplicationContext(), HomeFeed.class);
                    startActivity(intent);
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
