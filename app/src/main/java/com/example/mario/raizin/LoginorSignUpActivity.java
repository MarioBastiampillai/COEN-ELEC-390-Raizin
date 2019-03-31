package com.example.mario.raizin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import static com.example.mario.raizin.HomeFeed.MyPREFERENCES;

public class LoginorSignUpActivity extends AppCompatActivity {

    EditText editTextObject;
    Button buttonObject;
    String[] userArray={};
    ListView listViewObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginor_sign_up);
        editTextObject=(EditText)findViewById(R.id.userName);
        buttonObject=(Button)findViewById(R.id.userNameButton);

        SharedPreferences sharedPreferences=getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String name=sharedPreferences.getString("nameKey", null);
        ArrayList<String>  mStringList= new ArrayList<String>();
        mStringList.add("hello");  //name is not being added
        String[] stringArray = new String[1];
        stringArray = mStringList.toArray(stringArray);
        listViewObject = (ListView) findViewById(R.id.listViewId);
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, R.layout.simple_list, stringArray);
        listViewObject.setAdapter(arrayAdapter);
        listViewObject.setOnItemClickListener(new AdapterView.OnItemClickListener() {   //ass a setOnItemClickListener which will run when one of the items of the listview is clicked
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


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
                Intent intent=new Intent(getApplicationContext(), HomeFeed.class);
                intent.putExtra("userName",userNameInput);
                startActivity(intent);
            }
        });
    }
}
