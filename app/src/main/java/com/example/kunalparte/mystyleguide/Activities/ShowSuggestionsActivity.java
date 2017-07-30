package com.example.kunalparte.mystyleguide.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Adapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.daprlabs.cardstack.SwipeDeck;
import com.example.kunalparte.mystyleguide.Adapters.ShowSuggestionAdapter;
import com.example.kunalparte.mystyleguide.Config;
import com.example.kunalparte.mystyleguide.Models.CategoryTypeData;
import com.example.kunalparte.mystyleguide.Models.ImageMetaData;
import com.example.kunalparte.mystyleguide.MySharePreferences;
import com.example.kunalparte.mystyleguide.R;
import com.example.kunalparte.mystyleguide.Singleton;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ShowSuggestionsActivity extends AppCompatActivity {

    ShowSuggestionAdapter showSuggestionAdapter;
    ArrayList<ImageMetaData> upperWearArrayList;
    ArrayList<ImageMetaData> bottomWearArrayList;
    ArrayList<ImageMetaData> footWearArrayList;
    SwipeDeck upperSwipeDeck;
    SwipeDeck bottomSwipeDeck;
    SwipeDeck footSwipeDeck;
    CardView noUpperWearData;
    CardView noBottomWearData;
    CardView noFootWearData;
    String categoryName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_suggestions);
        init();
    }

    private void init() {
        setToolbar();
        upperSwipeDeck = (SwipeDeck) findViewById(R.id.upperWearSwipeDeck);
        bottomSwipeDeck = (SwipeDeck) findViewById(R.id.bottomWearSwipeDeck);
        footSwipeDeck = (SwipeDeck) findViewById(R.id.footWearSwipeDeck);
        upperSwipeDeck.setHardwareAccelerationEnabled(true);
        bottomSwipeDeck.setHardwareAccelerationEnabled(true);
        footSwipeDeck.setHardwareAccelerationEnabled(true);
        noUpperWearData = (CardView) findViewById(R.id.noDataUpperWear);
        noBottomWearData = (CardView) findViewById(R.id.noDatabottomWear);
        noFootWearData = (CardView) findViewById(R.id.noDataFootWear);
        Intent intent = this.getIntent();
        categoryName = intent.getStringExtra(Config.PREFERRED_TYPE);
        if (Config.isNewtworkAvalable(this)) {
            getDataForAllLists(categoryName);
            swipeCardDeck(upperSwipeDeck, upperWearArrayList);
            swipeCardDeck(bottomSwipeDeck, bottomWearArrayList);
            swipeCardDeck(footSwipeDeck, footWearArrayList);
        }else {
            Toast.makeText(this,"No Inernet Connection.Swipe to refresh",Toast.LENGTH_SHORT).show();
        }
    }

    public void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.showSuggestionToolBar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.maroon));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("My Suggestions Pair");
        getWindow().setStatusBarColor(getResources().getColor(R.color.dark_maroon));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void getDataForAllLists(String categoryType){
        upperWearArrayList = new ArrayList<>();
        bottomWearArrayList = new ArrayList<>();
        footWearArrayList = new ArrayList<>();
        String url = Config.GET_USERS_URL+"/"+ MySharePreferences.getLoginUserName(this)+"/categories/"+categoryType+".json";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Iterator iterator = response.keys();
                while (iterator.hasNext()){
                    String categories = iterator.next().toString();
                    try {
                        if (categories != null && categories.equals(Config.UPPERWEAR)){
                            JSONObject jsonObject = getCategoriesKeysAnddata(response.getJSONObject(categories));
                            putDatainLists(upperWearArrayList,jsonObject);
                        }
                        if (categories != null && categories.equals(Config.BOTTOMWEAR)){
                            JSONObject jsonObject = getCategoriesKeysAnddata(response.getJSONObject(categories));
                            putDatainLists(bottomWearArrayList,jsonObject);
                        }
                        if (categories != null && categories.equals(Config.FOOTWEAR)){
                            JSONObject jsonObject = getCategoriesKeysAnddata(response.getJSONObject(categories));
                            putDatainLists(footWearArrayList,jsonObject);
                        }
                    }catch (JSONException e){

                    }
                    }
                if (upperWearArrayList != null && upperWearArrayList.size()>0){
                    upperSwipeDeck.setVisibility(View.VISIBLE);
                    noUpperWearData.setVisibility(View.GONE);
                    setListOnAdapter(upperWearArrayList,upperSwipeDeck);
                }else {
                    upperSwipeDeck.setVisibility(View.GONE);
                    noUpperWearData.setVisibility(View.VISIBLE);
                }
                if (bottomWearArrayList != null && bottomWearArrayList.size() > 0){
                    bottomSwipeDeck.setVisibility(View.VISIBLE);
                    noBottomWearData.setVisibility(View.GONE);
                    setListOnAdapter(bottomWearArrayList,bottomSwipeDeck);
                }else {
                    bottomSwipeDeck.setVisibility(View.GONE);
                    noBottomWearData.setVisibility(View.VISIBLE);
                }
                if (footWearArrayList != null && footWearArrayList.size() > 0){
                    footSwipeDeck.setVisibility(View.VISIBLE);
                    noFootWearData.setVisibility(View.GONE);
                    setListOnAdapter(footWearArrayList,footSwipeDeck);
                }else {
                    footSwipeDeck.setVisibility(View.GONE);
                    noFootWearData.setVisibility(View.VISIBLE);
                }
                }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Config.getRequestQueue(this,jsonObjectRequest);
    }

    public JSONObject getCategoriesKeysAnddata(JSONObject jsonObject){
        Iterator iterator = jsonObject.keys();
        JSONObject json = new JSONObject();
        while (iterator.hasNext()){
            String key = iterator.next().toString();
            try {
                json = jsonObject.getJSONObject(key);
                return json;
            }catch (JSONException e){

            }
        }
        return json;
    }
    public void setListOnAdapter(ArrayList<ImageMetaData> imageMetaDataArrayList,SwipeDeck swipeDeck){
        showSuggestionAdapter = new ShowSuggestionAdapter(ShowSuggestionsActivity.this,imageMetaDataArrayList);
        swipeDeck.setAdapter(showSuggestionAdapter);
    }
    public void swipeCardDeck(SwipeDeck swipeDeck, final ArrayList<ImageMetaData> imageMetaDatas){
        swipeDeck.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {;

            }

            @Override
            public void cardSwipedRight(int position) {
                DatabaseReference databaseReference = Singleton.getInstance().getFirebaseReference().getReferenceFromUrl(Config.GET_USERS_URL+"/"+MySharePreferences.getLoginUserName(getApplicationContext()));
                databaseReference.child("bookMarks").push().setValue(returnUpdatedImageMetaData(imageMetaDatas.get(position)));
            }

            @Override
            public void cardsDepleted() {

            }

            @Override
            public void cardActionDown() {

            }

            @Override
            public void cardActionUp() {

            }
        });
    }
    public HashMap returnUpdatedImageMetaData(ImageMetaData imageMetaData){
        HashMap map = new HashMap();
        map.put("id",imageMetaData.getId());
        map.put("url",imageMetaData.getImageUri());
        map.put("isBookmarked",true);
        return map;
    }
    public void putDatainLists(ArrayList<ImageMetaData> categoryTypeDataArrayList, JSONObject jsonObject){
        try {
            ImageMetaData categoryTypeData = new ImageMetaData();
            categoryTypeData.setImageUri(jsonObject.getString("url"));
            categoryTypeData.setId(jsonObject.getString("id"));
            categoryTypeData.setBookmarked(jsonObject.getBoolean("isBookmarked"));
            categoryTypeDataArrayList.add(categoryTypeData);
        }catch (JSONException e){
            System.out.print(""+e);
        }
    }
    @Override
    public void onBackPressed(){
        finish();
    }
}
