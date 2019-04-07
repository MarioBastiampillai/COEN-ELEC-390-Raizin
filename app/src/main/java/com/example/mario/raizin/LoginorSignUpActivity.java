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

public class LoginorSignUpActivity extends AppCompatActivity {

    EditText editTextObject;
    Button buttonObject;
    ListView listViewObject;
    // String userNameSelected;
    TextView startInstruction;
    TextView nextInstruction;
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

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("nameKey", "");

        if(!TextUtils.isEmpty(name)) {
            mStringList.add(name);
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
                // userNameSelected=stringArray[position];

                Intent intentHomeFeed = new Intent(getApplicationContext(), HomeFeed.class);
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
                    Intent intent=new Intent(getApplicationContext(), indexPage.class);
                    sharedPreferences.edit().putString("nameKey", userNameInput).apply();
                    startActivity(intent);
                }

            }
        });
    }
}
