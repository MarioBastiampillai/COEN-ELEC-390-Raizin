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
    int currentScoreTrack;
    String fitzpatrickType = null;
    String gii1 = null;
    String gii2 = null;
    String gii3 = null;
    String link = null;
    SharedPreferences sharedPreferences;

    String fitzpatrickType = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_information);

        Intent intent = getIntent();
        currentScoreTrack = intent.getIntExtra("SCORE_TRACK", 0);

        if (currentScoreTrack >= 0 && currentScoreTrack < 8){

            fitzpatrickType = "Type 1";
            link = "https://www.amazon.ca/Banana-Boat-Performance-Ultra-Lightweight-Sunscreen/dp/B01BM5UKQU/" +
                    "ref=sr_1_6?keywords=spf+30&qid=1554603584&refinements=p_72%3A11192170011&rnid=11192166011&s=gateway&sr=8-6";

            gii1 = "This skin type is believed to be highly susceptible to premature ageing and skin cancers " +
                    "and one is therefore advised to take extreme care, " +
                    "use sunscreen and protect yourself from harmful UV rays as extreme sun exposure can result in serious damage";

            gii2 = "Individuals with this skin type should stay out of the sun during the peak hours of 10am - 4pm," +
                    "wear a high SPF 30 sunscreen every day and re apply it at lunchtime";

            gii3 = "Wear sunglasses that provide UVA protection as UVA exposure can cause cataracts. " +
                    "Tanning beds and booth are prohibited and contraindicated at all times as they have a high intensity of UV output ";




        }
        if (currentScoreTrack >= 8 && currentScoreTrack < 17){

            fitzpatrickType = "Type 2";
            link = "https://www.amazon.ca/Banana-Boat-Performance-Ultra-Lightweight-Sunscreen/dp/B01BM5UKQU/" +
                    "ref=sr_1_6?keywords=spf+30&qid=1554603584&refinements=p_72%3A11192170011&rnid=11192166011&s=gateway&sr=8-6";

            gii1 = "This skin type manage to get a light tan with repeated exposure to sun." +
                    " These skin types are therefore advised to take extreme care, " +
                    "use sunscreen and protect themselves from harmful UV rays as extreme sun exposure can result in serious damage";

            gii2 = "Individuals with this skin type should stay out of the sun during the peak hours of 10am - 4pm, " +
                    "wear a high SPF 30 sunscreen every day and re apply it at lunchtime";

            gii3 = "Wear sunglasses that provide UVA protection as UVA exposure can cause cataracts. " +
                    "Avoid tanning beds and booth at all times as they have a high intensity of UV output";
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


        }
        if (currentScoreTrack >= 25 && currentScoreTrack < 30){

            fitzpatrickType = "Type 4";
            link = "https://www.amazon.ca/Hawaiian-Tropic-Sheer-Sunscreen-Lotion/dp/B00BP2H6SG/ref=sr_1_5?keywords=spf+15&qid=1554603930&s=gateway&sr=8-5";

            gii1 = "This skin type typically tan with ease and seldom get burned." +
                    "They should still take care, use sunscreen and protect themselves from harmful UV rays as extreme sun exposure can result in serious damage, " +
                    "uneven skin tone, premature ageing and possible skin cancers.";

            gii2 = "Individuals with this skin type should stay out of the sun during the peak hours of 10am - 4pm, and wear a SPF 15 sunscreen every day.";

            gii3 = "Wear sunglasses that provide UVA protection as UVA exposure can cause cataracts.";
        }
        if (currentScoreTrack >= 30){

            fitzpatrickType = "Type V and VI";
            link = "https://www.amazon.ca/Hawaiian-Tropic-Sheer-Sunscreen-Lotion/dp/B00BP2H6SG/ref=sr_1_5?keywords=spf+15&qid=1554603930&s=gateway&sr=8-5";

            gii1 = "This skin type tan easily and very rarely burn. " +
                    "Individuals with this skin type should protect themselves from the sun as chronic sun exposure leads to uneven skin tone and pigmentation.";
            gii2 = " Individuals with this skin type should stay out of the sun during the peak hours of 10am - 4pm, " +
                    "and wear a SPF 15 to 30 sunscreen every day";

            gii3 = "Wear sunglasses that provide UVA protection as UVA exposure can cause cataracts.";
        }

        setContentView(R.layout.activity_general_information);
        TextView textView = (TextView) findViewById(R.id.textViewName);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        fitzpatrickType = sharedPreferences.getString("skinTypeKey", "N/A");

        TextView textView = findViewById(R.id.fitzDisplay);
        textView.setText("Per the fitzpatrick scale you are of the " + fitzpatrickType);

        TextView textView1 = findViewById(R.id.gi1);
        textView1.setText(gii1);

        TextView textView2 = findViewById(R.id.gi2);
        textView2.setText(gii2);

        TextView textView3 = findViewById(R.id.gi3);
        textView3.setText(gii3);

        shoppingButton  = (Button)findViewById(R.id.shoppingButton);
        shoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(intent);

            }
        });

    }

   /* public void clickMe(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.amazon.ca/s?k=sunscreen&ref=nb_sb_noss"));
        startActivity(intent);
    }
    */

}
