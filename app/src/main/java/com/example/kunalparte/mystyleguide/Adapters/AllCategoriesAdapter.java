package com.example.kunalparte.mystyleguide.Adapters;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.kunalparte.mystyleguide.Models.ImageMetaData;
import com.example.kunalparte.mystyleguide.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by kunalparte on 28/07/17.
 */

public class AllCategoriesAdapter extends RecyclerView.Adapter<AllCategoriesAdapter.VHolder> {
    ArrayList<ImageMetaData>allCategoriesArrayList = new ArrayList<>();
    Activity activity;
    public AllCategoriesAdapter(Activity activity, ArrayList<ImageMetaData> allCategoriesArrayList) {
        this.activity = activity;
        this.allCategoriesArrayList = allCategoriesArrayList;
    }

    @Override
    public VHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.all_categories_list_item,parent,false);
        return new VHolder(view);
    }

    @Override
    public void onBindViewHolder(VHolder holder, int position) {
        holder.allCategoriesImage.setColorFilter(ContextCompat.getColor(activity,R.color.blackTransparent));
        Picasso.with(activity).load(allCategoriesArrayList.get(position).getImageUri()).fit().centerCrop().into(holder.allCategoriesImage);
    }

    @Override
    public int getItemCount() {
        return allCategoriesArrayList.size();
    }

    public class VHolder extends RecyclerView.ViewHolder{
        ImageView allCategoriesImage;
        public VHolder(View itemView) {
            super(itemView);
            allCategoriesImage = (ImageView) itemView.findViewById(R.id.allCategoriesImages);
        }
    }
}
