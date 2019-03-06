package com.example.mario.raizin;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.view.View;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;


public class SkinTypeDeterminationActivity extends AppCompatActivity {

    String[] skinColor={"       Pale white skin", "      White skin", "      Light brown or olive skin", "       Brown skin", "      Dark brown or black skin"};
    ListView listViewObject;
    Button nextButton;
    Button cancelButton;
    String selectedSkinTone="";
    String recommendedSPFLevel=new String();

    int timeBeforeBurnWithoutSunscreen;
    int recommendedSPFLevelNumber;
    int estimatedTimeOfSunscreenDuration;
    String skinToneSelection;
    String dialogFragmentSkinType=new String();
    TextView skinTypeChosen;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin_type_determination);
        skinTypeChosen=(TextView)findViewById(R.id.skinTypeSelection);
        nextButton=(Button)findViewById(R.id.nextButtonID);
        cancelButton=(Button)findViewById(R.id.cancelButtonID);
        listViewObject = (ListView) findViewById(R.id.listViewSkinColor);   //creation of a listViewObject which is then used to set up the listView
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, R.layout.simple_list, skinColor); //puts whatever is in the totalCourseArray into the listView
        listViewObject.setAdapter(arrayAdapter);
        skinTypeChosen.setVisibility(View.INVISIBLE);
        listViewObject.setOnItemClickListener(new OnItemClickListener() {   //ass a setOnItemClickListener which will run when one of the items of the listview is clicked
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {          //once an item is clicked it will open the assignmentActivity
            //openDialogFragment();
                Toast.makeText(SkinTypeDeterminationActivity.this, skinColor[position], Toast.LENGTH_SHORT).show();
                //need to store the selected skin type
                selectedSkinTone=skinColor[position];
                //determined from nivea sunscreen guide for all skin types
                if(selectedSkinTone=="       Pale white skin")
                {
                    recommendedSPFLevel="30 or 50+";
                    recommendedSPFLevelNumber=30;
                    timeBeforeBurnWithoutSunscreen=10;
                    estimatedTimeOfSunscreenDuration=((recommendedSPFLevelNumber*timeBeforeBurnWithoutSunscreen));
                    skinToneSelection="Pale white skin";
                }
                else if(selectedSkinTone=="      White skin")
                {
                    recommendedSPFLevel="30 or 50+";
                    recommendedSPFLevelNumber=30;
                    timeBeforeBurnWithoutSunscreen=15;
                    estimatedTimeOfSunscreenDuration=((recommendedSPFLevelNumber*timeBeforeBurnWithoutSunscreen));
                    skinToneSelection="White skin";

                    //estimatedTimeOfSunscreenDuration in minutes
                    //pass the value to TimeOutsideActivity

                }
                else if(selectedSkinTone=="      Light brown or olive skin")
                {
                    recommendedSPFLevel="atleast 30";
                    recommendedSPFLevelNumber=30;
                    timeBeforeBurnWithoutSunscreen=18;
                    estimatedTimeOfSunscreenDuration=((recommendedSPFLevelNumber*timeBeforeBurnWithoutSunscreen));
                    skinToneSelection="Light brown or olive skin";
                    //estimatedTimeOfSunscreenDuration in minutes
                    //need to check the duration of how long the person will be outside
                    //if the user will be outside for a long time then the recommendedSPFLevel will be 50+
                }
                else if(selectedSkinTone=="       Brown skin")
                {
                    recommendedSPFLevel="atleast 15";
                    recommendedSPFLevelNumber=15;
                    timeBeforeBurnWithoutSunscreen=20;
                    estimatedTimeOfSunscreenDuration=((recommendedSPFLevelNumber*timeBeforeBurnWithoutSunscreen));
                    skinToneSelection="Brown skin";

                    //estimatedTimeOfSunscreenDuration in minutes
                    //if the user will be outside for a long time then the recommendedSPFLevel will be 50+
                }
                else if(selectedSkinTone=="      Dark brown or black skin")
                {
                    recommendedSPFLevel="atleast 15";
                    recommendedSPFLevelNumber=15;
                    timeBeforeBurnWithoutSunscreen=25;
                    estimatedTimeOfSunscreenDuration=((recommendedSPFLevelNumber*timeBeforeBurnWithoutSunscreen));
                    skinToneSelection="Dark brown or black skin";
                    //estimatedTimeOfSunscreenDuration in minutes
                }
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //need to prevent if skin type from dialog fragment isn't chosen and aslo if item from listview isn't selected
                if(skinToneSelection=="Pale white skin")
                {
                    Intent in=new Intent(getApplicationContext(), SkinTypeDeterminationActivityContinuation.class);  //was HomeFeed.class
                    in.putExtra("spfFactor","30");
                    in.putExtra("estimatedTime", (estimatedTimeOfSunscreenDuration/60));   //was divide by 60
                    startActivity(in);
                }
                else if(skinToneSelection=="White skin")
                {
                    Intent in = new Intent(getApplicationContext(), SkinTypeDeterminationActivityContinuation.class);
                    in.putExtra("spfFactor", "30");
                    in.putExtra("estimatedTime", (estimatedTimeOfSunscreenDuration/60));
                    startActivity(in);
                }
                else if(skinToneSelection=="Light brown or olive skin")
                {
                    Intent in=new Intent(getApplicationContext(), SkinTypeDeterminationActivityContinuation.class);
                    in.putExtra("spfFactor","30");
                    in.putExtra("estimatedTime", (estimatedTimeOfSunscreenDuration/60));
                    startActivity(in);
                }
                else if(skinToneSelection=="Brown skin")
                {
                        Intent in = new Intent(getApplicationContext(), SkinTypeDeterminationActivityContinuation.class);
                        in.putExtra("spfFactor", "15");
                        in.putExtra("estimatedTime", (estimatedTimeOfSunscreenDuration/60));
                        startActivity(in);
                }
                else if(skinToneSelection=="Dark brown or black skin")
                {
                    Intent in=new Intent(getApplicationContext(), SkinTypeDeterminationActivityContinuation.class);
                    in.putExtra("spfFactor","15");
                    in.putExtra("estimatedTime", (estimatedTimeOfSunscreenDuration/60));
                    startActivity(in);
                }

            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent intent = new Intent(getApplicationContext(), userInfo.class);
                startActivity(intent);
            }
        });
    }
}

