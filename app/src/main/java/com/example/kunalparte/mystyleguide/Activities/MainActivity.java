package com.example.kunalparte.mystyleguide.Activities;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.kunalparte.mystyleguide.Config;
import com.example.kunalparte.mystyleguide.Models.UserLoginDetais;
import com.example.kunalparte.mystyleguide.MySharePreferences;
import com.example.kunalparte.mystyleguide.R;
import com.example.kunalparte.mystyleguide.Singleton;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    GoogleSignInOptions googleSignInOptions;
    GoogleApiClient googleApiClient;
//    SignInButton googleSignInButton;
    View.OnClickListener onClickListener;
    LoginButton fbLoGinButton;
    CallbackManager callbackManager;
    Button googleSignInbutton;
    Button fbSignInbutton;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();
        init();
        onClick();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, 1);
    }
    public void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.maroon));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getWindow().setStatusBarColor(getResources().getColor(R.color.dark_maroon));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void onClick() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
/*                    case R.id.sign_in_button:
                        signIn();
                        break;*/
                    case R.id.fbSignInBtn:
                        if (Config.isNewtworkAvalable(getApplicationContext()))
                        fbLoGinButton.performClick();
                        else
                            Toast.makeText(getApplicationContext(), "No internet Connection", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.login_button:
                        onFBLoginButtonClicked();
                        break;
                    case R.id.googleSignInBtn:
                        if (Config.isNewtworkAvalable(getApplicationContext()))
                            signIn();
                        else
                            Toast.makeText(getApplicationContext(), "No internet Connection", Toast.LENGTH_SHORT).show();
                        break;


                }
            }
        };
//        googleSignInButton.setOnClickListener(onClickListener);
        fbLoGinButton.setOnClickListener(onClickListener);
        googleSignInbutton.setOnClickListener(onClickListener);
        fbSignInbutton.setOnClickListener(onClickListener);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("MainActivity", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            UserLoginDetais userLoginDetais = new UserLoginDetais();
            userLoginDetais.setUserName(acct.getDisplayName());
            if (acct.getPhotoUrl() != null) {
                userLoginDetais.setProfilePic(acct.getPhotoUrl().toString());
            }
            userLoginDetais.setUserEmail(acct.getEmail());
            MySharePreferences.saveUserNameLogin(this,acct.getDisplayName());
            MySharePreferences.saveUserEmailLogin(this,acct.getEmail());
            MySharePreferences.isLoggedIn(MainActivity.this,true);
            DatabaseReference reference = Singleton.getInstance().getFirebaseReference().getReferenceFromUrl(Config.GET_USERS_URL);
            Config.setFirebaseUser(userLoginDetais,reference);
            Toast.makeText(getApplicationContext(),"User Authorized",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this,Main2Activity.class));
            finish();
        } else {
            Toast.makeText(getApplicationContext(),"User not Authorized",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 1){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }else {
            callbackManager.onActivityResult(requestCode,resultCode,data);
        }
    }
    private void init() {
        googleSignInbutton = (Button) findViewById(R.id.googleSignInBtn);
//          googleSignInButton = (SignInButton) findViewById(R.id.sign_in_button);

        callbackManager = CallbackManager.Factory.create();
        fbSignInbutton = (Button) findViewById(R.id.fbSignInBtn);
        fbLoGinButton = (LoginButton) findViewById(R.id.login_button);
        fbLoGinButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
    }


    public void onFBLoginButtonClicked(){
        fbLoGinButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        UserLoginDetais userLoginDetais = new UserLoginDetais();
                        userLoginDetais.setUserName(object.optString("name"));
                        if (object.optString("name") != null) {
                            userLoginDetais.setProfilePic(object.optString("picture"));
                        }
                        userLoginDetais.setUserEmail(object.optString("email"));
                        MySharePreferences.saveUserNameLogin(getApplicationContext(),object.optString("name"));
                        MySharePreferences.saveUserEmailLogin(getApplicationContext(),object.optString("email"));
                        MySharePreferences.isLoggedIn(MainActivity.this,true);
                        DatabaseReference reference = Singleton.getInstance().getFirebaseReference().getReferenceFromUrl(Config.GET_USERS_URL);
                        Config.setFirebaseUser(userLoginDetais, reference);
                        startActivity(new Intent(MainActivity.this,Main2Activity.class));
                        finish();
                    }
                });
                Bundle parameteres = new Bundle();
                parameteres.putString("fields","id,name,email,picture,birthday,gender");
                graphRequest.setParameters(parameteres);
                graphRequest.executeAsync();
                Toast.makeText(getApplicationContext(),"User Authorized",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),"User not Authorized",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                System.out.println(""+error.toString());
            }
        });
    }
}
