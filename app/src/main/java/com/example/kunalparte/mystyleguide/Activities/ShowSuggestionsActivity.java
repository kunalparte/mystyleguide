package com.example.kunalparte.mystyleguide.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.daprlabs.cardstack.SwipeDeck;
import com.example.kunalparte.mystyleguide.Adapters.ShowSuggestionAdapter;
import com.example.kunalparte.mystyleguide.Adapters.ShowSuggestionRecyclerAdapter;
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

public class ShowSuggestionsActivity extends AppCompatActivity{

    ShowSuggestionAdapter showSuggestionAdapter;
    ArrayList<ImageMetaData> upperWearArrayList;
    ArrayList<ImageMetaData> bottomWearArrayList;
    ArrayList<ImageMetaData> footWearArrayList;
    String categoryName;
    RecyclerView upperWearSuggestionList;
    RecyclerView bottomWearSuggestionList;
    RecyclerView footWearSuggestionList;
    TextView uppertext;
    TextView bottomtext;
    TextView foottext;
    ShowSuggestionRecyclerAdapter showSuggestionRecyclerAdapter;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_suggestions);
        init();
    }

    private void init() {
        setToolbar();
        uppertext = (TextView) findViewById(R.id.upperWeartext);
        uppertext.setBackgroundColor(Color.TRANSPARENT);
        bottomtext = (TextView) findViewById(R.id.bottomWeartext);
        bottomtext.setBackgroundColor(Color.TRANSPARENT);
        foottext = (TextView) findViewById(R.id.footWeartext);
        foottext.setBackgroundColor(Color.TRANSPARENT);
        upperWearSuggestionList = (RecyclerView) findViewById(R.id.upperWearList);
        bottomWearSuggestionList = (RecyclerView) findViewById(R.id.bottomWearList);
        footWearSuggestionList = (RecyclerView) findViewById(R.id.footWearList);
        Intent intent = this.getIntent();
        categoryName = intent.getStringExtra(Config.PREFERRED_TYPE);
        if (Config.isNewtworkAvalable(this)) {
            getDataForAllLists(categoryName);
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
                        getCategoriesKeysAnddata(response.getJSONObject(categories),categories);
                    }catch (JSONException e){

                    }
                    }
                if (upperWearArrayList != null && upperWearArrayList.size()>0){
                    setRecyclerOcnAdapter(upperWearArrayList,upperWearSuggestionList,Config.UPPERWEAR);
                }else {
                }
                if (bottomWearArrayList != null && bottomWearArrayList.size() > 0){
                    setRecyclerOcnAdapter(bottomWearArrayList,bottomWearSuggestionList,Config.BOTTOMWEAR);
                }else {
                }
                if (footWearArrayList != null && footWearArrayList.size() > 0){
                    setRecyclerOcnAdapter(footWearArrayList,footWearSuggestionList,Config.FOOTWEAR);
                }else {
                }
                }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Config.getRequestQueue(this,jsonObjectRequest);
    }

    public void getCategoriesKeysAnddata(JSONObject jsonObject,String categories){
        ArrayList<ImageMetaData> imageMetaDatas = new ArrayList<>();
        Iterator iterator = jsonObject.keys();
        JSONObject json = new JSONObject();
        while (iterator.hasNext()){
            try {
                String key = iterator.next().toString();
                json = jsonObject.getJSONObject(key);
                imageMetaDatas.add(putDatainLists(json));
            }catch (JSONException e){

            }
        }
        switch(categories){
            case Config.UPPERWEAR:
                upperWearArrayList.addAll(imageMetaDatas);
                break;
            case Config.BOTTOMWEAR:
                bottomWearArrayList.addAll(imageMetaDatas);
                break;
            case Config.FOOTWEAR:
                footWearArrayList.addAll(imageMetaDatas);
                break;
        }
    }

    public void setRecyclerOcnAdapter(ArrayList<ImageMetaData> arrayList, RecyclerView recyclerView, String category){
        RecyclerView.LayoutManager rlayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(rlayoutManager);
        showSuggestionRecyclerAdapter = new ShowSuggestionRecyclerAdapter(this,arrayList,category,categoryName);
        recyclerView.setAdapter(showSuggestionRecyclerAdapter);

    }

    public ImageMetaData putDatainLists(JSONObject jsonObject){
        ImageMetaData categoryTypeData = new ImageMetaData();
        try {
            categoryTypeData.setImageUri(jsonObject.getString("url"));
            categoryTypeData.setId(jsonObject.getString("id"));
            categoryTypeData.setBookmarked(jsonObject.getBoolean("isBookmarked"));
        }catch (JSONException e){
            System.out.print(""+e);
        }
        return categoryTypeData;
    }
    @Override
    public void onBackPressed(){
        finish();
    }



}
