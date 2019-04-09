package com.example.mario.raizin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class facebook extends AppCompatActivity {

    private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private CircleImageView circleImageView;

    Button button_share_link, button_share_Photo;
    ShareDialog shareDialog;

    private int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);

        circleImageView = findViewById(R.id.profile_pic);

        info = (TextView) findViewById(R.id.info);

        //login facebook button
        loginButton = findViewById(R.id.login_button);

        //share link button
        button_share_link = (Button)findViewById(R.id.button_share_link);

        //share photo button
        button_share_Photo = findViewById(R.id.button_share_Photo);

        // initializing facebook
        callbackManager = CallbackManager.Factory.create();

        shareDialog = new ShareDialog(this);

        loginButton.setReadPermissions(Arrays.asList("email","public_profile"));

        checkLoginStatus();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loaduserProfile(AccessToken.getCurrentAccessToken());
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
//facebook share

        //for sharing the link
        button_share_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Toast.makeText( facebook.this, "Share successful", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText( facebook.this, "Share cancel", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText( facebook.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setQuote("this is useful link")
                        .setContentUrl(Uri.parse("http://youtube.com"))
                        .build();
                if(ShareDialog.canShow(ShareLinkContent.class)){

                    shareDialog.show(linkContent);
                }

            }
        });

    }

    public void sharePhotos(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {

                Bitmap image = null;
                try {
                    image = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(image)
                        .build();

                SharePhotoContent sharePhotoContent = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();


                if (ShareDialog.canShow(SharePhotoContent.class)) {
                    shareDialog.show(sharePhotoContent);
                }
            }
        }
    }

    private void loaduserProfile(AccessToken newAccessToken) {

        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String first_name = object.getString("first_name");
                    String last_name = object.getString("last_name");
                    String id = object.getString("id");
                    String image_url = "https://graph.facebook.com/" + id + "/picture?type=large";

                    info.setText(first_name + " " + last_name);
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.dontAnimate();

                    Glide.with(facebook.this).load(image_url).into(circleImageView);

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

    private void checkLoginStatus() {
        if(AccessToken.getCurrentAccessToken()!=null)
        {
            loaduserProfile(AccessToken.getCurrentAccessToken());
            button_share_link.setVisibility(View.VISIBLE);
            button_share_Photo.setVisibility(View.VISIBLE);
        }
        else{
            button_share_link.setVisibility(View.INVISIBLE);
            button_share_Photo.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (AccessToken.getCurrentAccessToken() != null) {
            button_share_link.setVisibility(View.VISIBLE);
            button_share_Photo.setVisibility(View.VISIBLE);
        } else {
            button_share_link.setVisibility(View.INVISIBLE);
            button_share_Photo.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AccessToken.getCurrentAccessToken() != null) {
            button_share_link.setVisibility(View.VISIBLE);
            button_share_Photo.setVisibility(View.VISIBLE);
        } else {
            button_share_link.setVisibility(View.INVISIBLE);
            button_share_Photo.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (AccessToken.getCurrentAccessToken() != null) {
            button_share_link.setVisibility(View.VISIBLE);
            button_share_Photo.setVisibility(View.VISIBLE);
        } else {
            button_share_link.setVisibility(View.INVISIBLE);
            button_share_Photo.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (AccessToken.getCurrentAccessToken() != null) {
            button_share_link.setVisibility(View.VISIBLE);
            button_share_Photo.setVisibility(View.VISIBLE);
        } else {
            button_share_link.setVisibility(View.INVISIBLE);
            button_share_Photo.setVisibility(View.INVISIBLE);
        }
    }

}
