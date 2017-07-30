package com.example.kunalparte.mystyleguide;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kunalparte.mystyleguide.Models.CategoryTypeData;
import com.example.kunalparte.mystyleguide.Models.ImageMetaData;
import com.example.kunalparte.mystyleguide.Models.UserLoginDetais;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

/**
 * Created by kunalparte on 26/07/17.
 */

public class Config {
    public static final String FIREBASE_URL = "https://fir-mystyleguide.firebaseio.com/";
    public static final String FIREBASE_STORAGE_URL = "gs://firebase-mystyleguide.appspot.com";
    public static final String GET_CATEGORIES_IMAGES = "gs://firebase-mystyleguide.appspot.com/categories";
    public static final String GET_USERS_URL = "https://fir-mystyleguide.firebaseio.com/users";
    public static final String GET_CATEGORIES = "https://fir-mystyleguide.firebaseio.com/categories";
    public static final String IS_ADD_IDENTIFIER = "isAddIdentifier";
    public static final String IS_SHOW_IDENTIFIER = "isShowIdentifier";
    public static final String PREFERRED_TYPE = "preferredType";
    public static final String USERS = "users";
    public static final String FORMALS = "formals";
    public static final String CASUALS = "casuals";
    public static final String GYM_SUiI = "gymSuite";
    public static final String PARTY_WEAR = "partyWear";
    public static final String CATEGORY_TYPE = "categoryType";
    public static final String UPPERWEAR = "upperWear";
    public static final String BOTTOMWEAR = "bottomWear";
    public static final String FOOTWEAR = "footWear";
    public static String CATEGORY ="";

    public static ArrayList<CategoryTypeData> allCategoryTypeArraylist;
    public static ArrayList<CategoryTypeData> lifestyleArraylist ;
    public static ArrayList<CategoryTypeData> footwearArraylist ;


    public static RequestQueue requestQueue;
    public static ProgressDialog progressDialog;
    public static final int CAMERA = 1;
    public static final int GALLERY = 2;
    public static void setFirebaseUser(UserLoginDetais user, DatabaseReference databaseReference){
        DatabaseReference reference = databaseReference.child(user.getUserName());
        reference.child("userEmail").setValue(user.getUserEmail());
        reference.child("profilePic").setValue(user.getProfilePic());
    }

    public static boolean isNewtworkAvalable(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null)
            return  true;
        else
            return false;
    }

    public static RequestQueue getRequestQueue(Context context, JsonObjectRequest jsonObjectRequest){
        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
        return requestQueue;
    }

    public static void showNHideProgressDialog(String message,Context context,boolean show){
        if (show) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(message);
            progressDialog.show();
        }else {
            progressDialog.dismiss();
        }
    }

    public static String random() {
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = "img"+sb.toString();
        return output;
    }
}
