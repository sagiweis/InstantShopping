package com.dys.instantshopping;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


public class StartupActivity extends AppCompatActivity {

    CallbackManager callbackManager;

    /*private String getCurrentDevicePhoneNumber(){
        TelephonyManager tMgr = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        return tMgr.getLine1Number();
    }

    private void registerUser(){
        String facebookId = Profile.getCurrentProfile().getId();
        String phoneNumber = getCurrentDevicePhoneNumber();
        User user = new User(facebookId,phoneNumber);
    }*/

    private void moveToMyGroups(){
        Intent myIntent = new Intent(StartupActivity.this, MyGroups.class);
        startActivity(myIntent);
    }

    private void handleFacebookLogin(){
        if(AccessToken.getCurrentAccessToken() != null){
            moveToMyGroups();
        } else {
            initFacebookLogin();
        }
    }

    private void initFacebookLogin(){
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //registerUser();
                moveToMyGroups();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }

        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_startup);
        handleFacebookLogin();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}