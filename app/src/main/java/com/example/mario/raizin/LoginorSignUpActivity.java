package com.example.mario.raizin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.mario.raizin.HomeFeed.MyPREFERENCES;

public class LoginorSignUpActivity extends AppCompatActivity {

    EditText editTextObject;
    Button buttonObject;
    ListView listViewObject;
    String userNameSelected;
    String storedSkinType;
    TextView startInstruction;
    TextView nextInstruction;
    public static final String MyPREFERENCES="MyPrefs";
    public static final String Name="nameKey";
    public static final String selectedName="selectedNameKey";
    SharedPreferences sharedPreferences;


    ArrayList<String>  mStringList= new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginor_sign_up);
        editTextObject=(EditText)findViewById(R.id.userName);
        buttonObject=(Button)findViewById(R.id.userNameButton);
        startInstruction=(TextView)findViewById(R.id.firstInstructionDisplay);
        nextInstruction=(TextView)findViewById(R.id.secondInstructionDisplay);

        sharedPreferences=getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String name=sharedPreferences.getString("nameKey", null);
        String otherName=sharedPreferences.getString("selectedNameKey", null);
        storedSkinType=sharedPreferences.getString("skinTypeKey", null);

        if(!TextUtils.isEmpty(name)&&TextUtils.isEmpty(otherName))
        {
            mStringList.add(name);
        }
        else if(TextUtils.isEmpty(name)&&!TextUtils.isEmpty(otherName))
        {
            mStringList.add(otherName);
        }


        String[] stringArray = new String[mStringList.size()];
        stringArray = mStringList.toArray(stringArray);
        if((stringArray.length)>0)
        {
            nextInstruction.setText("Click on a profile below to login");
        }
        else{
            nextInstruction.setText("");
        }
        listViewObject = (ListView) findViewById(R.id.listViewId);
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, R.layout.simple_list, stringArray);
        listViewObject.setAdapter(arrayAdapter);

        listViewObject.setOnItemClickListener(new AdapterView.OnItemClickListener() {   //ass a setOnItemClickListener which will run when one of the items of the listview is clicked
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] stringArray = new String[mStringList.size()];
                stringArray = mStringList.toArray(stringArray);
                userNameSelected=stringArray[position];
                //SharedPreferences sharedPreferences=getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                //SharedPreferences.Editor editor = sharedPreferences.edit();
                //editor.putString("goToHomeFeed", userNameSelected);
                //editor.putString("skinTypeDisplay", storedSkinType);
                //editor.commit();

                Intent intentHomeFeed = new Intent(getApplicationContext(), HomeFeed.class);
                intentHomeFeed.putExtra("goToHomeFeed", userNameSelected);
                intentHomeFeed.putExtra("skinTypeDisplay", storedSkinType);
                startActivity(intentHomeFeed);

            }
        });

        buttonObject.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0)
            {
                String userNameInput=editTextObject.getText().toString();
                if(userNameInput.length()==0)
                {
                    Toast.makeText(getApplicationContext(), "Please enter a proper name", Toast.LENGTH_SHORT).show();
                }
                else{
                    //SharedPreferences sharedPreferences=getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    //SharedPreferences.Editor editor = sharedPreferences.edit();
                    //editor.putString("goToHomeFeedName", userNameInput);
                    //editor.commit();
                    Intent intent=new Intent(getApplicationContext(), indexPage.class);
                    intent.putExtra("userName",userNameInput);
                    startActivity(intent);
                }
                /*SharedPreferences sharedPreferences=getSharedPreferences(originalPreferences, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(originalName, userNameInput);
                editor.commit();*/

                //intent.putExtra("userName",userNameInput);
                //String[] stringArray = new String[mStringList.size()];
                //stringArray = mStringList.toArray(stringArray);
                /*SharedPreferences sharedPreferences=getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(originalName, stringArray[0]);
                editor.commit();*/

                //put into sharedPreference in indexPage
                //put the userNameInput into mStringList, sharedPreference
                //check to see whether the string obtained from sharedPreference is empty or not
                //if it is not empty, add it to mStringList
                //check to see if the listview is empty or not
                //if it is not empty take the econtents and add it into mStringList

            }
        });
    }
}
