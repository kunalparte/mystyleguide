package com.example.kunalparte.mystyleguide;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

/**
 * Created by kunalparte on 27/06/17.
 */

public class MySharePreferences {
    public static String MY_APP_PREF = "my_app_Pref";
    public static String IS_LOGIN = "is_login";
    public static String USERNAME = "userName";
    public static String USEREMAIL = "userEmail";
    public static String USERPROFILEIMAGE = "userProfileImage";
    public static SharedPreferences sharePreferences;
    public static SharedPreferences.Editor editor;

    public static void initializePreferences(Context context) {
        if (sharePreferences == null) {
            sharePreferences = context.getSharedPreferences(MY_APP_PREF, Context.MODE_PRIVATE);
            editor = sharePreferences.edit();
        }
    }

    public static void isLoggedIn(Context context, boolean isLogin) {
        initializePreferences(context);
        editor.putBoolean(IS_LOGIN, isLogin).commit();
    }

    public static void saveUserNameLogin(Context context, String userName) {
        initializePreferences(context);
        editor.putString(USERNAME, userName).commit();
    }

    public static void saveUserEmailLogin(Context context, String userEmail) {
        initializePreferences(context);
        editor.putString(USEREMAIL, userEmail).commit();
    }

    public static void saveUserProfileImageUri(Context context,Uri uri){
        initializePreferences(context);
        String profileImageStr = uri.toString();
        editor.putString(USERPROFILEIMAGE,profileImageStr).commit();
    }
    public static boolean getLoginStatus(Context context) {
        initializePreferences(context);
        return sharePreferences.getBoolean(IS_LOGIN, false);
    }

    public static String getLoginUserEmail(Context context) {
        initializePreferences(context);
        return sharePreferences.getString(USEREMAIL, " ");
    }

    public static String getLoginUserName(Context context) {
        initializePreferences(context);
        return sharePreferences.getString(USERNAME, " ");
    }

    public static String getUserProfileImageUrl(Context context){
        initializePreferences(context);
        if (sharePreferences.getString(USERPROFILEIMAGE, " ")!= null && !sharePreferences.getString(USERPROFILEIMAGE, " ").isEmpty())
            return sharePreferences.getString(USERPROFILEIMAGE, " ");
        else
            return null;
    }
}