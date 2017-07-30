package com.example.kunalparte.mystyleguide.Adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.kunalparte.mystyleguide.Activities.ShowSuggestionsActivity;
import com.example.kunalparte.mystyleguide.Models.ImageMetaData;
import com.example.kunalparte.mystyleguide.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by kunalparte on 29/07/17.
 */

public class ShowSuggestionAdapter extends BaseAdapter{
    ArrayList<ImageMetaData> suggestionArrayList;
    Activity context;

    public ShowSuggestionAdapter(ShowSuggestionsActivity showSuggestionsActivity, ArrayList<ImageMetaData> imageMetaDataArrayList) {
        this.context = showSuggestionsActivity;
        this.suggestionArrayList = imageMetaDataArrayList;
    }

    @Override
    public int getCount() {
        return suggestionArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return suggestionArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.show_suggestion_list_item,parent,false);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.showCategoriesImages);
        imageView.setColorFilter(ContextCompat.getColor(context,R.color.blackTransparent));
        Picasso.with(context).load(Uri.parse(suggestionArrayList.get(position).getImageUri())).fit().into(imageView);
        return convertView;
    }
}
