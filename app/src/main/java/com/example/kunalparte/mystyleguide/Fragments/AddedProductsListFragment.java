package com.example.kunalparte.mystyleguide.Fragments;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.kunalparte.mystyleguide.Activities.AddNewProductActivity;
import com.example.kunalparte.mystyleguide.Activities.Main2Activity;
import com.example.kunalparte.mystyleguide.Adapters.AllCategoriesAdapter;
import com.example.kunalparte.mystyleguide.Adapters.ProductCategoryListAdapter;
import com.example.kunalparte.mystyleguide.Config;
import com.example.kunalparte.mystyleguide.Models.CategoryTypeData;
import com.example.kunalparte.mystyleguide.Models.ImageMetaData;
import com.example.kunalparte.mystyleguide.Models.ImageModelClass;
import com.example.kunalparte.mystyleguide.MySharePreferences;
import com.example.kunalparte.mystyleguide.R;
import com.example.kunalparte.mystyleguide.Singleton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


public class AddedProductsListFragment extends android.app.Fragment {
    ArrayList<ImageMetaData> allCategoriesArrayList;
    LinearLayoutManager layoutManager;
    AllCategoriesAdapter allCategoriesAdapter;
    RecyclerView allCategoriesList;
    FloatingActionButton floatingActionButton;
    TextView noDataText;
    SwipeRefreshLayout swipeRefreshLayout;

    public AddedProductsListFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_added_products_list, container, false);
        init(view);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddNewProductActivity.class));
            }
        });
        ((Main2Activity)getActivity()).getCategoryList();
        getAllcategoriesData();
        if (allCategoriesArrayList.size() > 0) {
            noDataText.setVisibility(View.GONE);
        } else {
            noDataText.setVisibility(View.VISIBLE);
        }
        onSwipe();
        return view;
    }

    public void onSwipe(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllcategoriesData();
                ((Main2Activity)getActivity()).getCategoryList();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void init(View view) {
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        allCategoriesList = (RecyclerView) view.findViewById(R.id.allcategoriesList);
        noDataText = (TextView) view.findViewById(R.id.noDataText);
        swipeRefreshLayout = (SwipeRefreshLayout)  view.findViewById(R.id.frag_add_swipe_refresh_layout);
        layoutManager = new LinearLayoutManager(getActivity());
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void getAllcategoriesData(){
        if (Config.isNewtworkAvalable(getActivity())) {
            DatabaseReference reference = Singleton.getInstance().getFirebaseReference().getReferenceFromUrl(Config.GET_USERS_URL).child(MySharePreferences.getLoginUserName(getActivity())).child("categories");
            reference.child("allCategories");
            allCategoriesArrayList = new ArrayList<>();
            String url = Config.GET_USERS_URL + "/" + MySharePreferences.getLoginUserName(getActivity()) + "/categories/allCategories.json";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Iterator iterator = response.keys();
                    while (iterator.hasNext()) {
                        String key = iterator.next().toString();
                        try {
                            JSONObject jsonObject = response.getJSONObject(key);
                            ImageMetaData metaData = new ImageMetaData();
                            metaData.setId(jsonObject.getString("id"));
                            metaData.setImageUri(jsonObject.getString("url"));
                            metaData.setBookmarked(jsonObject.getBoolean("isBookmarked"));
                            allCategoriesArrayList.add(metaData);
                        } catch (JSONException e) {
                            System.out.println("" + e);
                        }
                    }
                    if (allCategoriesArrayList.size() > 0) {
                        allCategoriesList.setVisibility(View.VISIBLE);
                        noDataText.setVisibility(View.GONE);
                        createAllCategoriesList();
                    } else {
                        allCategoriesList.setVisibility(View.GONE);
                        noDataText.setVisibility(View.VISIBLE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            Config.getRequestQueue(getActivity(), jsonObjectRequest);
        }else {
            Toast.makeText(getActivity(),"No Internet Connection.Swipe to refresh",Toast.LENGTH_SHORT).show();
        }
    }
    public void createAllCategoriesList(){
        layoutManager = new LinearLayoutManager(getActivity());
        allCategoriesList.setLayoutManager(layoutManager);
        allCategoriesAdapter = new AllCategoriesAdapter(getActivity(),allCategoriesArrayList);
        allCategoriesList.setAdapter(allCategoriesAdapter);
        allCategoriesAdapter.notifyDataSetChanged();
    }
    @Override
    public void onResume(){
        super.onResume();
//        getAllcategoriesData();
        allCategoriesArrayList = new ArrayList<>();
        DatabaseReference reference = Singleton.getInstance().getFirebaseReference().getReferenceFromUrl(Config.GET_USERS_URL).child(MySharePreferences.getLoginUserName(getActivity())).child("categories");
        reference.child("allCategories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        ImageModelClass imageModelClass = snapshot.getValue(ImageModelClass.class);
                        ImageMetaData metaData = new ImageMetaData();
                        metaData.setId(imageModelClass.getId());
                        metaData.setImageUri(imageModelClass.getUrl());
                        metaData.setBookmarked(imageModelClass.isBookmarked());
                        allCategoriesArrayList.add(metaData);
                    }
                    if (allCategoriesArrayList.size()>0)
                    createAllCategoriesList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
