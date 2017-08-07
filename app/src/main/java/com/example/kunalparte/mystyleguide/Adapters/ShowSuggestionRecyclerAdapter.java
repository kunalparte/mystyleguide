package com.example.kunalparte.mystyleguide.Adapters;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.example.kunalparte.mystyleguide.Activities.ShowSuggestionsActivity;
import com.example.kunalparte.mystyleguide.Config;
import com.example.kunalparte.mystyleguide.Models.ImageMetaData;
import com.example.kunalparte.mystyleguide.Models.ImageModelClass;
import com.example.kunalparte.mystyleguide.MySharePreferences;
import com.example.kunalparte.mystyleguide.R;
import com.example.kunalparte.mystyleguide.Singleton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by kunalparte on 04/08/17.
 */

public class ShowSuggestionRecyclerAdapter extends RecyclerView.Adapter<ShowSuggestionRecyclerAdapter.VHolder> {
    Context context;
    ArrayList<ImageMetaData> imageMetaDataArrayList;
    String category;
    String categoryName;

    public ShowSuggestionRecyclerAdapter(ShowSuggestionsActivity showSuggestionsActivity, ArrayList<ImageMetaData> arrayList, String category, String categoryName) {
        this.context = showSuggestionsActivity;
        this.imageMetaDataArrayList = arrayList;
        this.category = category;
        this.categoryName = categoryName;
    }

    @Override
    public VHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.show_suggestions_layout, parent, false);
        return new VHolder(view);
    }


    @Override
    public void onBindViewHolder(final VHolder holder, final int position) {
        int totalItems = getItemCount();
        /*View view = holder.suggestionCard;
        if (position == totalItems-1 -position){
            view.setScaleX(0.8f);
            view.setScaleY(0.8f);
        }else {
            view.setScaleX(1f);
            view.setScaleY(1f);
        }*/

        Picasso.with(context).load(Uri.parse(imageMetaDataArrayList.get(position).getImageUri())).fit().into(holder.imageView);
        if (imageMetaDataArrayList.get(position).isBookmarked()) {
            holder.bookMarkButton.setImageDrawable(context.getResources().getDrawable(R.drawable.bookmarked_icon));
        } else {
            holder.bookMarkButton.setImageDrawable(context.getResources().getDrawable(R.drawable.bookmarks_icon));
        }
        holder.bookMarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageMetaDataArrayList.get(position).isBookmarked()) {
                    holder.bookMarkButton.setImageDrawable(context.getResources().getDrawable(R.drawable.bookmarks_icon));
                    DatabaseReference databaseReference = Singleton.getInstance().getFirebaseReference().getReferenceFromUrl(Config.GET_USERS_URL + "/" + MySharePreferences.getLoginUserName(context));
                    switch (category) {
                        case Config.UPPERWEAR:
                            updateValueOfChild(databaseReference.child("categories").child(categoryName).child(Config.UPPERWEAR), imageMetaDataArrayList.get(position).getId());
                            break;
                        case Config.BOTTOMWEAR:
                            updateValueOfChild(databaseReference.child("categories").child(categoryName).child(Config.BOTTOMWEAR), imageMetaDataArrayList.get(position).getId());
                            break;
                        case Config.FOOTWEAR:
                            updateValueOfChild(databaseReference.child("categories").child(categoryName).child(Config.FOOTWEAR), imageMetaDataArrayList.get(position).getId());
                            break;
                    }
                    Query query = databaseReference.child("bookMarks");
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                ImageModelClass imageModelClass = snapshot.getValue(ImageModelClass.class);
                                if (imageMetaDataArrayList.get(position).getId().equals(imageModelClass.getId())) {
                                    snapshot.getRef().removeValue();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    holder.bookMarkButton.setImageDrawable(context.getResources().getDrawable(R.drawable.bookmarked_icon));
                    DatabaseReference databaseReference = Singleton.getInstance().getFirebaseReference().getReferenceFromUrl(Config.GET_USERS_URL + "/" + MySharePreferences.getLoginUserName(context));
                    databaseReference.child("bookMarks").push().setValue(Config.getHashMap(imageMetaDataArrayList.get(position).getId(), imageMetaDataArrayList.get(position).getImageUri(), imageMetaDataArrayList.get(position).isBookmarked()));
                    switch (category) {
                        case Config.UPPERWEAR:
                            updateValueOfChild(databaseReference.child("categories").child(categoryName).child(Config.UPPERWEAR), imageMetaDataArrayList.get(position).getId());
                            break;
                        case Config.BOTTOMWEAR:
                            updateValueOfChild(databaseReference.child("categories").child(categoryName).child(Config.BOTTOMWEAR), imageMetaDataArrayList.get(position).getId());
                            break;
                        case Config.FOOTWEAR:
                            updateValueOfChild(databaseReference.child("categories").child(categoryName).child(Config.FOOTWEAR), imageMetaDataArrayList.get(position).getId());
                            break;
                    }
                }
            }
        });
    }

    public void updateValueOfChild(DatabaseReference reference, final String id) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ImageModelClass imageModelClass = snapshot.getValue(ImageModelClass.class);
                    if (id.equals(imageModelClass.getId())) {
                        if (imageModelClass.isBookmarked())
                            snapshot.getRef().child("isBookmarked").setValue(false);
                        else
                            snapshot.getRef().child("isBookmarked").setValue(true);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return imageMetaDataArrayList.size();
    }

    public class VHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView bookMarkButton;
        CardView suggestionCard;
        View root;
        public VHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.suggestionCardImageView);
            bookMarkButton = (ImageView) itemView.findViewById(R.id.bookmarkButton);
            suggestionCard = (CardView) itemView.findViewById(R.id.suggestionListCard);
        }
    }
}
