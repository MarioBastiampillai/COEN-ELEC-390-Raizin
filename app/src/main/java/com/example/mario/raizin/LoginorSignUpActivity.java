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
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.mario.raizin.HomeFeed.MyPREFERENCES;

public class LoginorSignUpActivity extends AppCompatActivity {

    EditText editTextObject;
    Button buttonObject;
    String[] userArray={};
    ListView listViewObject;
    String userNameSelected;
    String storedSkinType;
    /*SharedPreferences sharedPreferencesObject;
    public static final String MyPREFERENCESLogin="MyPrefsLogin";
    public static final String NameLogin="nameLoginKey";*/
    ArrayList<String>  mStringList= new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginor_sign_up);
        editTextObject=(EditText)findViewById(R.id.userName);
        buttonObject=(Button)findViewById(R.id.userNameButton);

        SharedPreferences sharedPreferences=getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String name=sharedPreferences.getString("nameKey", null);
        String otherName=sharedPreferences.getString("selectedNameKey", null);
        storedSkinType=sharedPreferences.getString("SkinType", null);

        //ArrayList<String>  mStringList= new ArrayList<String>();
        if(!TextUtils.isEmpty(name)&&TextUtils.isEmpty(otherName))
        {
            mStringList.add(name);
        }
        else if(TextUtils.isEmpty(name)&&!TextUtils.isEmpty(otherName))
        {
            mStringList.add(otherName);
        }
        else{
            Toast.makeText(getApplicationContext(), "Please enter a proper name", Toast.LENGTH_SHORT).show();
        }
         //name is not being added
        String[] stringArray = new String[mStringList.size()];   //need to figure out what the size of mStringList is, mStringList.size(), was 1
        stringArray = mStringList.toArray(stringArray);          //need to attach previous stuff in array as well
        listViewObject = (ListView) findViewById(R.id.listViewId);
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, R.layout.simple_list, stringArray);
        listViewObject.setAdapter(arrayAdapter);

        listViewObject.setOnItemClickListener(new AdapterView.OnItemClickListener() {   //ass a setOnItemClickListener which will run when one of the items of the listview is clicked
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] stringArray = new String[mStringList.size()];
                stringArray = mStringList.toArray(stringArray);
                userNameSelected=stringArray[position];
                Intent intentHomeFeed = new Intent(getApplicationContext(), HomeFeed.class);
                intentHomeFeed.putExtra("goToHomeFeed", userNameSelected);//take all the information related to userNameSelected and pass it to the HomeFeed and open it directly
                intentHomeFeed.putExtra("skinTypeDisplay", storedSkinType);
                startActivity(intentHomeFeed);

            }
        });
        /*listViewObject.setOnItemClickListener(new AdapterView.OnItemClickListener() {   //ass a setOnItemClickListener which will run when one of the items of the listview is clicked
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
        });*/
        buttonObject.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0)
            {
                String userNameInput=editTextObject.getText().toString();
                Intent intent=new Intent(getApplicationContext(), indexPage.class);
                intent.putExtra("userName",userNameInput);
                startActivity(intent);
            }
        });
    }
}
