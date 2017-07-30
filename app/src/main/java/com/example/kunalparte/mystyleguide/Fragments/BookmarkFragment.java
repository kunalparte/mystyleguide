package com.example.kunalparte.mystyleguide.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.kunalparte.mystyleguide.Adapters.AllCategoriesAdapter;
import com.example.kunalparte.mystyleguide.Config;
import com.example.kunalparte.mystyleguide.Models.ImageMetaData;
import com.example.kunalparte.mystyleguide.MySharePreferences;
import com.example.kunalparte.mystyleguide.R;
import com.example.kunalparte.mystyleguide.Singleton;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class BookmarkFragment extends android.app.Fragment {

    RecyclerView bookMaksList;
    AllCategoriesAdapter allCategoriesAdapter;
    ArrayList<ImageMetaData> bookMarkArrayList;
    LinearLayoutManager layoutManager;
    TextView noBookmarkText;
    SwipeRefreshLayout swipeRefreshLayout;

    public BookmarkFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);
        init(view);
        if (Config.isNewtworkAvalable(getActivity())){
            getBookmarksdata();
        }else {
            Toast.makeText(getActivity(), "No Internet connection.Swipe to refresh", Toast.LENGTH_SHORT).show();
        }

        onSwipe();
        if (bookMarkArrayList.size()>0){
            noBookmarkText.setVisibility(View.GONE);
        }else {
            bookMaksList.setVisibility(View.GONE);
            noBookmarkText.setVisibility(View.VISIBLE);
        }
        return view;
    }

    private void init(View view) {
        bookMaksList = (RecyclerView) view.findViewById(R.id.bookMarksList);
        noBookmarkText = (TextView) view.findViewById(R.id.noBookmarkText);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.frag_bookmark_swipe_refresh_layout);
        bookMarkArrayList = new ArrayList<>();
    }

    public void onSwipe(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Config.isNewtworkAvalable(getActivity())){
                    getBookmarksdata();
                }else {
                    Toast.makeText(getActivity(), "No Internet connection.Swipe to refresh", Toast.LENGTH_SHORT).show();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void getBookmarksdata(){
        DatabaseReference databaseReference = Singleton.getInstance().getFirebaseReference().getReferenceFromUrl(Config.GET_USERS_URL+"/"+MySharePreferences.getLoginUserName(getActivity()));
        databaseReference.child("bookMarks");
        String url = Config.GET_USERS_URL+"/"+ MySharePreferences.getLoginUserName(getActivity())+"/bookMarks.json";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Iterator iterator = response.keys();
                while (iterator.hasNext()){
                    String key = iterator.next().toString();
                    try {
                        JSONObject jsonObject = response.getJSONObject(key);
                        ImageMetaData imageMetaData = new ImageMetaData();
                        imageMetaData.setId(jsonObject.getString("id"));
                        imageMetaData.setImageUri(jsonObject.getString("url"));
                        imageMetaData.setBookmarked(jsonObject.getBoolean("isBookmarked"));
                        bookMarkArrayList.add(imageMetaData);
                    }catch (JSONException e){

                    }
                }
                if (bookMarkArrayList.size()>0){
                    bookMaksList.setVisibility(View.VISIBLE);
                    noBookmarkText.setVisibility(View.GONE);
                    allCategoriesAdapter = new AllCategoriesAdapter(getActivity(),bookMarkArrayList);
                    layoutManager = new LinearLayoutManager(getActivity());
                    bookMaksList.setLayoutManager(layoutManager);
                    bookMaksList.setAdapter(allCategoriesAdapter);
                    allCategoriesAdapter.notifyDataSetChanged();

                }else {
                    bookMaksList.setVisibility(View.GONE);
                    noBookmarkText.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(""+error
                );
            }
        });
        Config.getRequestQueue(getActivity(),jsonObjectRequest);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        }

        @Override
        public void onResume(){
            super.onResume();
            getBookmarksdata();
        }
}
