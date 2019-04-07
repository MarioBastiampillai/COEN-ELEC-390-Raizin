package com.example.mario.raizin;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import android.provider.MediaStore;
import java.io.IOException;

public class facebook extends AppCompatActivity {


    //facebook login
    private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    //facebook login

    //facebook share link, share photo

    Button button_share_link, button_share_Photo;
    ShareDialog shareDialog;
    private Object view;

    private int PICK_IMAGE_REQUEST = 1;

    //facebook share link, share photo

  public Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

            SharePhoto sharePhoto = new SharePhoto.Builder()
                    .setBitmap(bitmap)
                    .build();

            if(ShareDialog.canShow(SharePhotoContent.class))
            {

                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(sharePhoto)
                        .build();
                shareDialog.show(content);
            }
      }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_facebook);


        //login facebook button
        loginButton = findViewById(R.id.login_button);

        //share link button
        button_share_link = (Button)findViewById(R.id.button_share_link);

        //share photo button
        button_share_Photo = findViewById(R.id.button_share_Photo);

        // initializing facebook
        callbackManager = CallbackManager.Factory.create();

        shareDialog = new ShareDialog(this);

        checkLoginStatus();

        info = (TextView) findViewById(R.id.info);


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult)
           {
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

        //for sharing the photo
        button_share_Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Toast.makeText( facebook.this, "Share successful", Toast.LENGTH_SHORT).show();

                Picasso.with(getBaseContext())
                        .load(Uri.parse("http://www.myconfinedspace.com/2015/03/31/vector-batman/vector-batman-jpg/"))
                        .into(target);


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

            }
        });



    }

    public void sharePhotos(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }



//facebook share


    //facebook login
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

                if (ShareDialog.canShow(SharePhotoContent.class)) {
                    SharePhotoContent sharePhotoContent = new SharePhotoContent.Builder()
                            .addPhoto(photo)
                            .build();

                    shareDialog.show(sharePhotoContent);
                }
            }
        }
    }

 //  AccessTokenTracker tokenTracker = new AccessTokenTracker() {
 //      @Override
 //      protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken)
 //      {
 //          if(currentAccessToken==null)
 //          {
 //              txtName.setText("");
 //              txtEmail.setText("");
 //              circleImageView.setImageResource(0);
 //              Toast.makeText(MainActivity.this,"User Logged out",Toast.LENGTH_LONG).show();
 //          }
 //          else
 //              loaduserProfile(currentAccessToken);

 //      }
 //};



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

//facebook login


}
