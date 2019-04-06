package com.example.mario.raizin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

public class facebook extends AppCompatActivity {


    //facebook
    private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    //facebook


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_facebook);


        //login facebook button
        loginButton = findViewById(R.id.login_button);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        info = (TextView) findViewById(R.id.info);
        loginButton = (LoginButton)findViewById(R.id.login_button);

//        checkLoginStatus();
        // loginButton.setReadPermissions(Arrays.asList("info"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                info.setText(
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n" +
                                "Auth Token: "
                                + loginResult.getAccessToken().getToken()
                );

            }

            @Override
            public void onCancel() {
                info.setText("Login attempt canceled.");

            }

            @Override
            public void onError(FacebookException error) {
                info.setText("Login attempt failed.");

            }
        });
// facebook

    }


    //facebook
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode,  resultCode, data);
//        super.onActivityResult(requestCode, resultCode, data);
    }

//    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
//        @Override
//        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken)
//        {
//            if(currentAccessToken==null)
//            {
////                txtName.setText("");
////                txtEmail.setText("");
////                circleImageView.setImageResource(0);
////                Toast.makeText(MainActivity.this,"User Logged out",Toast.LENGTH_LONG).show();
//            }
//            else
//                loaduserProfile(currentAccessToken);
//
//        }
//    };

    private void loaduserProfile(AccessToken newAccessToken) {

        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String first_name = object.getString("first_name");
                    String last_name = object.getString("last_name");
                    String email = object.getString("email");
                    String id = object.getString("id");
                    String image_url = "https://graph.facebook.com/"+id+ "/picture?type=normal";


//                    txtEmail.setText(email);
                    info.setText(first_name + " " +last_name);

//                    Glide.with(MainActivity.this).load(image_url).into(circleImageView);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        Bundle parameters = new Bundle();
        parameters.putString("fields","first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }

    private void checkLoginStatus()
    {
        if(AccessToken.getCurrentAccessToken()!=null)
        {
            loaduserProfile(AccessToken.getCurrentAccessToken());
        }
    }

//facebook
//facebook share




}
