package com.example.kunalparte.mystyleguide.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kunalparte.mystyleguide.Activities.AddNewProductActivity;
import com.example.kunalparte.mystyleguide.Activities.ShowSuggestionsActivity;
import com.example.kunalparte.mystyleguide.Config;
import com.example.kunalparte.mystyleguide.Dialogs.MyCustomizedDilog;
import com.example.kunalparte.mystyleguide.Fragments.AddedProductsListFragment;
import com.example.kunalparte.mystyleguide.Fragments.ProductSuggestionFragment;
import com.example.kunalparte.mystyleguide.Models.CategoryTypeData;
import com.example.kunalparte.mystyleguide.R;
import com.example.kunalparte.mystyleguide.Singleton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by kunalparte on 27/07/17.
 */

public class ProductCategoryListAdapter extends RecyclerView.Adapter<ProductCategoryListAdapter.VHolder>{
    Activity context;
    ArrayList<CategoryTypeData> categoryTypeDatas;
    String identifier;
    public ProductCategoryListAdapter(Activity applicationContext, ArrayList<CategoryTypeData> categoryTypeDataArrayList,String identifier) {
        this.context = applicationContext;
        this.categoryTypeDatas = categoryTypeDataArrayList;
        this.identifier = identifier;
    }

    @Override
    public VHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_select_list,parent,false);
        return new VHolder(view);
    }

    @Override
    public void onBindViewHolder(final VHolder holder, final int position) {
        Picasso.with(context).load(Uri.parse(categoryTypeDatas.get(position).getPicture())).centerCrop().fit().into(holder.productImage);
        holder.productName.setText(categoryTypeDatas.get(position).getCategoryTypename());
        switch (identifier){
            case Config.IS_ADD_IDENTIFIER:
                holder.productCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.productName.getText().toString().equalsIgnoreCase("casual & t-shirts")){
                            ((AddNewProductActivity) context).showCasualOptionDialog();
                        }else {
                            ((AddNewProductActivity) context).showOptionDialog(holder.productName.getText().toString());
                        }

                    }
                });
                break;
            case Config.FORMALS:
                holder.productCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callShowSuggestionDataActivity(context,Config.FORMALS);

                    }
                });
                break;
            case Config.CASUALS:
                holder.productCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callShowSuggestionDataActivity(context,Config.CASUALS);

                    }
                });
                break;
            case Config.GYM_SUiI:
                holder.productCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callShowSuggestionDataActivity(context,Config.GYM_SUiI);

                    }
                });
                break;
            case Config.PARTY_WEAR:
                holder.productCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callShowSuggestionDataActivity(context,Config.PARTY_WEAR);
                    }
                });
                break;
        }

    }
    public void callShowSuggestionDataActivity(Activity activity,String categoryName){
        Intent intent = new Intent(activity, ShowSuggestionsActivity.class);
        intent.putExtra(Config.PREFERRED_TYPE,categoryName);
        activity.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return categoryTypeDatas.size();
    }

    public class VHolder extends RecyclerView.ViewHolder{
        ImageView productImage;
        TextView productName;
        CardView productCard;
        public VHolder(View itemView) {
            super(itemView);
            productImage = (ImageView) itemView.findViewById(R.id.productImage);
            productName = (TextView) itemView.findViewById(R.id.productText);
            productCard = (CardView) itemView.findViewById(R.id.productCard);
        }
    }

}
