package com.example.kunalparte.mystyleguide.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.kunalparte.mystyleguide.Activities.ShowSuggestionsActivity;
import com.example.kunalparte.mystyleguide.Adapters.ProductCategoryListAdapter;
import com.example.kunalparte.mystyleguide.Config;
import com.example.kunalparte.mystyleguide.Models.CategoryTypeData;
import com.example.kunalparte.mystyleguide.R;

import java.util.ArrayList;


public class ProductSuggestionFragment extends android.app.Fragment implements View.OnClickListener {
    RecyclerView formalCategoryList;
    RecyclerView casualCategoryList;
    RecyclerView gymSuitCategoryList;
    RecyclerView partyWearlCategoryList;
    LinearLayout formalCard;
    LinearLayout casualCard;
    LinearLayout gymSuitCard;
    LinearLayout partyWearCard;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    ProductCategoryListAdapter productCategoryListAdapter;
    ArrayList<CategoryTypeData>formalCategoryData ;
    ArrayList<CategoryTypeData>casualCategoryData ;
    ArrayList<CategoryTypeData>gymSuitCategoryData ;
    ArrayList<CategoryTypeData>partyWearCategoryData ;
    View.OnClickListener onClickListener;

    public ProductSuggestionFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_suggestion, container, false);
        init(view);
        if (Config.isNewtworkAvalable(getActivity())) {
            getDataForRecyclerViews();
        }else {
            Toast.makeText(getActivity(), "No Internet connection.Swipe to refresh", Toast.LENGTH_SHORT).show();

        }

        formalCard.setOnClickListener(this);
        casualCard.setOnClickListener(this);
        gymSuitCard.setOnClickListener(this);
        partyWearCard.setOnClickListener(this);
        return view;
    }

    private void init(View view) {
        formalCategoryList = (RecyclerView) view.findViewById(R.id.formalCategoryList);
        casualCategoryList = (RecyclerView) view.findViewById(R.id.casualCategoryList);
        gymSuitCategoryList = (RecyclerView) view.findViewById(R.id.gymSuitList);
        partyWearlCategoryList = (RecyclerView) view.findViewById(R.id.partyWearCategoryList);
        formalCard = (LinearLayout) view.findViewById(R.id.formalsWear);
        casualCard = (LinearLayout) view.findViewById(R.id.casualWear);
        gymSuitCard = (LinearLayout) view.findViewById(R.id.gymSuitCard);
        partyWearCard = (LinearLayout) view.findViewById(R.id.partyWear);
    }

    public void getDataForRecyclerViews() {
        formalCategoryData = new ArrayList<>();
        casualCategoryData = new ArrayList<>();
        gymSuitCategoryData = new ArrayList<>();
        partyWearCategoryData = new ArrayList<>();
        if (Config.allCategoryTypeArraylist != null && Config.allCategoryTypeArraylist.size() > 0) {
            for (int i = 0; i < Config.allCategoryTypeArraylist.size(); i++) {
                String categoryName = Config.allCategoryTypeArraylist.get(i).getCategoryTypename();
                if (categoryName.equalsIgnoreCase("formal-shirts") || categoryName.equalsIgnoreCase("formal-shoes") || categoryName.equalsIgnoreCase("trousers")) {
                    putDatainLists(formalCategoryData, Config.allCategoryTypeArraylist.get(i));
                }
                if (categoryName.equalsIgnoreCase("casual & t-shirts") || categoryName.equalsIgnoreCase("jeans & chinos") || categoryName.equalsIgnoreCase("sneakers & vans") || categoryName.equalsIgnoreCase("shorts & boxers") || categoryName.equals("flip-flops")) {
                    putDatainLists(casualCategoryData, Config.allCategoryTypeArraylist.get(i));
                }
                if (categoryName.equalsIgnoreCase("casual & t-shirts") || categoryName.equalsIgnoreCase("shorts & boxers") || categoryName.equalsIgnoreCase("sneakers & vans")) {
                    putDatainLists(gymSuitCategoryData, Config.allCategoryTypeArraylist.get(i));
                }
                if (categoryName.equalsIgnoreCase("jeans & chinos") || categoryName.equalsIgnoreCase("jackets") || categoryName.equalsIgnoreCase("t-shirts") || categoryName.equalsIgnoreCase("sneakers & vans")) {
                    putDatainLists(partyWearCategoryData, Config.allCategoryTypeArraylist.get(i));
                }
            }

            if (formalCategoryData.size() > 0) {
                setViewForData(formalCategoryList, formalCategoryData,Config.FORMALS);
            }
                if (casualCategoryData.size() > 0) {
                    setViewForData(casualCategoryList, casualCategoryData,Config.CASUALS);
                }
                if (gymSuitCategoryData.size() > 0) {
                    setViewForData(gymSuitCategoryList, gymSuitCategoryData,Config.GYM_SUiI);
                }
                if (partyWearCategoryData.size() > 0) {
                    setViewForData(partyWearlCategoryList, partyWearCategoryData,Config.PARTY_WEAR);
                }
            }
        }

    public void callShowSuggestionDataActivity(String categoryName){
        Intent intent = new Intent(getActivity(), ShowSuggestionsActivity.class);
        intent.putExtra(Config.PREFERRED_TYPE,categoryName);
        startActivity(intent);
    }
    public void setViewForData(RecyclerView recyclerView , ArrayList<CategoryTypeData>arrayList,String identifier){
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
         productCategoryListAdapter = new ProductCategoryListAdapter(getActivity(),arrayList,identifier);
        recyclerView.setAdapter(productCategoryListAdapter);
        recyclerView.setClickable(false);
        productCategoryListAdapter.notifyDataSetChanged();
    }
    public void putDatainLists(ArrayList<CategoryTypeData> categoryTypeDataArrayList,CategoryTypeData categoryTypeDatas){
        CategoryTypeData categoryTypeData = new CategoryTypeData();
        categoryTypeData.setPicture(categoryTypeDatas.getPicture());
        categoryTypeData.setCategoryTypename(categoryTypeDatas.getCategoryTypename());
        categoryTypeDataArrayList.add(categoryTypeData);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.formalsWear:
                callShowSuggestionDataActivity(Config.FORMALS);
                break;
            case R.id.casualWear:
                callShowSuggestionDataActivity(Config.CASUALS);
                break;
            case R.id.gymSuitCard:
                callShowSuggestionDataActivity(Config.GYM_SUiI);
                break;
            case R.id.partyWear:
                callShowSuggestionDataActivity(Config.PARTY_WEAR);
                break;
        }
    }
}
