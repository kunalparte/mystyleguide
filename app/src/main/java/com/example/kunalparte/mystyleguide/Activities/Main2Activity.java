package com.example.kunalparte.mystyleguide.Activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.kunalparte.mystyleguide.Config;
import com.example.kunalparte.mystyleguide.Dialogs.MyCustomizedDilog;
import com.example.kunalparte.mystyleguide.Fragments.AddedProductsListFragment;
import com.example.kunalparte.mystyleguide.Fragments.BookmarkFragment;
import com.example.kunalparte.mystyleguide.Fragments.ProductSuggestionFragment;
import com.example.kunalparte.mystyleguide.Fragments.ProfileFragment;
import com.example.kunalparte.mystyleguide.Models.CategoryTypeData;
import com.example.kunalparte.mystyleguide.R;
import com.example.kunalparte.mystyleguide.Singleton;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

public class Main2Activity extends AppCompatActivity {
    FrameLayout frameLayout;
    Context context;
    View view;
    RelativeLayout mainListIncludedLay;
    BottomBar bottomBar;
    int previousSelectedTab = 0;
    int tabPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        init();
        onTabSelected();
//        getCategoryList();
          bottomBar.setActiveTabColor(getApplicationContext().getResources().getColor(R.color.white));
    }

    private void init() {
        setToolbar();
        frameLayout = (FrameLayout) findViewById(R.id.fragAddLayout);
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
    }
    public void onTabSelected(){
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId){
                    case R.id.productList:
                        tabPosition = 0;
                        getSupportActionBar().setTitle("My WardRobe");
                        break;
                    case R.id.suggestions:
                        tabPosition = 1;
                        getSupportActionBar().setTitle("My Suggestions");
                        break;
                    case R.id.bookmarks:
                        tabPosition = 2;
                        getSupportActionBar().setTitle("My Bookmarks");
                        break;
                    default:
                        break;
                }
                switchTab(tabPosition);

            }

        });
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

    public void getPreviousFragment(Stack<Fragment> fragments, Stack<String>fragmentTitle){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.remove(fragments.lastElement());
        fragments.pop();
        fragmentTitle.pop();
        fragmentTransaction.show(fragments.lastElement()).commit();
    }
    public void switchTab(int position){
        FragmentTransaction fragmentTransaction =getFragmentManager().beginTransaction();
        Fragment fragment = getFragment(previousSelectedTab);
        if (fragment != null){
            fragmentTransaction.hide(fragment);
        }
        switch (position){
            case 0:
                if (Singleton.getInstance().getProductListFragmentStack().size() > 0) {
                    for (int i = Singleton.getInstance().getProductListFragmentStack().size() - 1; i > 0; i--) {
                        Singleton.getInstance().getProductListFragmentStack().remove(i);
                        Singleton.getInstance().getProductListFragTitleStack().remove(i);
                    }
                }
                break;
            case 1:
                if (Singleton.getInstance().getSuggestionFragmentStack().size() > 0) {
                    for (int i = Singleton.getInstance().getSuggestionFragmentStack().size() - 1; i > 0; i--) {
                        Singleton.getInstance().getSuggestionFragmentStack().remove(i);
                        Singleton.getInstance().getSuggestionFragTitleStack().remove(i);
                    }
                }
                break;
            case 2:
                if (Singleton.getInstance().getBookmarksFragmentStack().size() > 0) {
                    for (int i = Singleton.getInstance().getBookmarksFragmentStack().size() - 1; i > 0; i--) {
                        Singleton.getInstance().getBookmarksFragmentStack().remove(i);
                        Singleton.getInstance().getBookmarksFragTitleStack().remove(i);
                    }
                }
                break;

        }
        Fragment newFragment = getFragment(position);
        if (newFragment != null){
            fragmentTransaction.show(newFragment);
            newFragment.onHiddenChanged(true);
        }
        fragmentTransaction.commit();
        if (newFragment == null ){
            switch (position){
                case 0:
                    Singleton.getInstance().getProductListFragmentStack().push(new AddedProductsListFragment());
                    Singleton.getInstance().getProductListFragTitleStack().push("AddProductListFragment");
                    fragmentTransaction.add(R.id.fragAddLayout,
                            Singleton.getInstance().getProductListFragmentStack().lastElement(),
                            Singleton.getInstance().getProductListFragTitleStack().lastElement());
                    break;
                case 1:
                    Singleton.getInstance().getSuggestionFragmentStack().push(new ProductSuggestionFragment());
                    Singleton.getInstance().getSuggestionFragTitleStack().push("Suggestionfragment");
                    fragmentTransaction.add(R.id.fragAddLayout,
                            Singleton.getInstance().getSuggestionFragmentStack().lastElement(),
                            Singleton.getInstance().getSuggestionFragTitleStack().lastElement());
                    break;
                case 2:
                    Singleton.getInstance().getBookmarksFragmentStack().push(new BookmarkFragment());
                    Singleton.getInstance().getBookmarksFragTitleStack().push("BookmarkFragment");
                    fragmentTransaction.add(R.id.fragAddLayout,
                            Singleton.getInstance().getBookmarksFragmentStack().lastElement(),
                            Singleton.getInstance().getBookmarksFragTitleStack().lastElement());
                    break;

            }
        }
        previousSelectedTab = position;
    }
    public Fragment getFragment(int position){
        switch(position){
            case 0:
                if (Singleton.getInstance().getProductListFragmentStack().size() > 0)
                return Singleton.getInstance().getProductListFragmentStack().lastElement();
            case 1:
                if (Singleton.getInstance().getSuggestionFragmentStack().size() > 0)
                return Singleton.getInstance().getSuggestionFragmentStack().lastElement();
            case 2:
                if (Singleton.getInstance().getBookmarksFragmentStack().size() > 0)
                return Singleton.getInstance().getBookmarksFragmentStack().lastElement();

        }
        return null;
    }

    @Override
    public void onBackPressed(){
        switch(tabPosition){
            case 0:
                if (Singleton.getInstance().getProductListFragmentStack().size() > 1){
                    getPreviousFragment(Singleton.getInstance().getProductListFragmentStack(),Singleton.getInstance().getProductListFragTitleStack());
                }else {
                    exitDialog();
                }
                break;
            case 1:
                if (Singleton.getInstance().getSuggestionFragmentStack().size() > 1){
                    getPreviousFragment(Singleton.getInstance().getSuggestionFragmentStack(),Singleton.getInstance().getSuggestionFragTitleStack());
                }else {
                    exitDialog();
                }
                break;
            case 2:
                if (Singleton.getInstance().getBookmarksFragmentStack().size() > 1){
                    getPreviousFragment(Singleton.getInstance().getBookmarksFragmentStack(),Singleton.getInstance().getBookmarksFragTitleStack());
                }else {
                    exitDialog();
                }
                break;
        }
    }
    public void getCategoryList(){
        Config.lifestyleArraylist = new ArrayList<>();
        Config.footwearArraylist = new ArrayList<>();
        Config.allCategoryTypeArraylist = new ArrayList<>();
        Config.showNHideProgressDialog("Wait for a moment",this,true);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Config.GET_CATEGORIES + ".json", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Iterator iterator = response.keys();
                while (iterator.hasNext()){
                    String category = iterator.next().toString();
                    try {
                        JSONObject jsonObject = response.getJSONObject(category);
                        Iterator categoryIterator= jsonObject.keys();
                        while (categoryIterator.hasNext()){
                            String categoryType = categoryIterator.next().toString();
                            JSONObject categoryTypeJson = jsonObject.getJSONObject(categoryType);
                            CategoryTypeData categoryTypeData = new CategoryTypeData();
                            categoryTypeData.setCategoryTypename(categoryTypeJson.getString("text"));
                            categoryTypeData.setPicture(categoryTypeJson.getString("picture"));
                            if (category.equals("lifestyle")){
                                Config.lifestyleArraylist.add(categoryTypeData);
                            }else {
                                Config.footwearArraylist.add(categoryTypeData);
                            }
                            Config.allCategoryTypeArraylist.add(categoryTypeData);
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                Config.showNHideProgressDialog("Wait for a moment",Main2Activity.this,false);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.print(""+error);
            }
        });

        Config.getRequestQueue(this,jsonObjectRequest);
    }
    public void exitDialog(){
        final MyCustomizedDilog myCustomizedDilog = new MyCustomizedDilog(this);
        myCustomizedDilog.setDialogMessage("Do You want to exit?");
        myCustomizedDilog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        myCustomizedDilog.setPositivBtnText("Yes");
        myCustomizedDilog.setNegativBtnText("No");
        myCustomizedDilog.setPositivBtnOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Singleton.getInstance().dismissAllDialogs();
                Singleton.getInstance().clearFragmentStacks();
                finish();
            }
        });
        myCustomizedDilog.setNegativBtnOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Singleton.getInstance().dismissAllDialogs();
            }
        });
        myCustomizedDilog.show();
        Singleton.getInstance().initializeVectorForDialog().add(myCustomizedDilog);
    }

}
