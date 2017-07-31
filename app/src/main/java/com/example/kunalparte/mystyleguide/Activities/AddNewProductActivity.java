package com.example.kunalparte.mystyleguide.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.kunalparte.mystyleguide.Adapters.ProductCategoryListAdapter;
import com.example.kunalparte.mystyleguide.Config;
import com.example.kunalparte.mystyleguide.Dialogs.MyCustomizedDilog;
import com.example.kunalparte.mystyleguide.Models.CategoryTypeData;
import com.example.kunalparte.mystyleguide.MySharePreferences;
import com.example.kunalparte.mystyleguide.R;
import com.example.kunalparte.mystyleguide.Singleton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static android.R.attr.permission;
import static java.security.AccessController.getContext;

public class AddNewProductActivity extends AppCompatActivity{
    RecyclerView addNewProductList;
    RecyclerView addNewFootwearList;
    RecyclerView.LayoutManager layoutManager;
    ProductCategoryListAdapter productCategoryListAdapter;
    ArrayList<CategoryTypeData> lifestyleArraylist;
    ArrayList<CategoryTypeData> footwearArraylist;
    MyCustomizedDilog myCustomizedDilog;
    ProgressDialog progressDialog;
    boolean permisssionGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);
        init();
        if (Config.isNewtworkAvalable(this))
        getCategoryList();
        else
            Toast.makeText(this, "No Internet connection.Swipe to refresh", Toast.LENGTH_SHORT).show();
    }

    private void init() {
        setToolbar();
        addNewProductList = (RecyclerView) findViewById(R.id.chooseLifestyleCategoryList);
        addNewFootwearList = (RecyclerView) findViewById(R.id.chooseFootwearCategoryList);
    }

    public void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.addActivityTolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.maroon));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getWindow().setStatusBarColor(getResources().getColor(R.color.dark_maroon));
        getSupportActionBar().setTitle("Choose To Upload");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    public void getCategoryList(){
        if (Config.lifestyleArraylist.size() >0){
            setViewForData(addNewProductList,Config.lifestyleArraylist,2);
        }
        if (Config.footwearArraylist.size() > 0){
            setViewForData(addNewFootwearList,Config.footwearArraylist,1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (Config.isNewtworkAvalable(this)) {
                switch (requestCode) {
                    case Config.CAMERA:
                        if (resultCode == RESULT_OK) {
                            if (data.toString() != null && !data.toString().isEmpty()) {
                                Bitmap photo = (Bitmap) data.getExtras().get("data");
                                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                    photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                                    String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), photo, "Title", null);
                                    createCategoryFirebaseData(Uri.parse(path));
                            }
                        }
                        break;
                    case Config.GALLERY:
                        if (resultCode == RESULT_OK) {
                            if (data != null)
                                createCategoryFirebaseData(data.getData());
                        }
                        break;
                }
            }else {
                Toast.makeText(this, "No Internet connection.Please try againShowSugges", Toast.LENGTH_SHORT).show();
            }
    }

    public void createCategoryFirebaseData(Uri data){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Wait for a moment");
        progressDialog.show();
        Uri uri = data;
        UploadTask uploadTask = Singleton.getInstance().getFirebaseStorageReference().getReferenceFromUrl(Config.FIREBASE_STORAGE_URL)
                .child(MySharePreferences.getLoginUserName(this)).child("categoryPictures").child(Config.random()).putFile(uri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(""+e);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri uri = taskSnapshot.getDownloadUrl();
            switch (Config.CATEGORY){
                case "Formal-Shirts":
                    putDataInFirebaseDB(true,false,false,Config.FORMALS,Config.UPPERWEAR,returnUpdatedImageMetaData(Config.random(),uri.toString(),false));
                    putDataForAllCategories(returnUpdatedImageMetaData(Config.random(),uri.toString(),false));
                    break;
                case "Casual-Shirts":
                    putDataInFirebaseDB(true,false,false,Config.CASUALS,Config.UPPERWEAR,returnUpdatedImageMetaData(Config.random(),uri.toString(),false));
                    putDataForAllCategories(returnUpdatedImageMetaData(Config.random(),uri.toString(),false));
                    break;
                case "T-Shirts":
                    putDataInFirebaseDB(true,false,false,Config.CASUALS,Config.UPPERWEAR,returnUpdatedImageMetaData(Config.random(),uri.toString(),false));
                    putDataInFirebaseDB(true,false,false,Config.PARTY_WEAR,Config.UPPERWEAR,returnUpdatedImageMetaData(Config.random(),uri.toString(),false));
                    putDataForAllCategories(returnUpdatedImageMetaData(Config.random(),uri.toString(),false));
                    break;
                case "Jackets":
                    putDataInFirebaseDB(true,false,false,Config.PARTY_WEAR,Config.UPPERWEAR,returnUpdatedImageMetaData(Config.random(),uri.toString(),false));
                    putDataForAllCategories(returnUpdatedImageMetaData(Config.random(),uri.toString(),false));
                    break;
                case "Shorts & Boxers":
                    putDataInFirebaseDB(false,true,false,Config.CASUALS,Config.BOTTOMWEAR,returnUpdatedImageMetaData(Config.random(),uri.toString(),false));
                    putDataInFirebaseDB(false,true,false,Config.GYM_SUiI,Config.BOTTOMWEAR,returnUpdatedImageMetaData(Config.random(),uri.toString(),false));
                    putDataForAllCategories(returnUpdatedImageMetaData(Config.random(),uri.toString(),false));
                    break;
                case "Jeans & Chinos":
                    putDataInFirebaseDB(false,true,false,Config.CASUALS,Config.BOTTOMWEAR,returnUpdatedImageMetaData(Config.random(),uri.toString(),false));
                    putDataInFirebaseDB(false,true,false,Config.PARTY_WEAR,Config.BOTTOMWEAR,returnUpdatedImageMetaData(Config.random(),uri.toString(),false));
                    putDataForAllCategories(returnUpdatedImageMetaData(Config.random(),uri.toString(),false));
                    break;
                case "Trousers":
                    putDataInFirebaseDB(false,true,false,Config.FORMALS,Config.BOTTOMWEAR,returnUpdatedImageMetaData(Config.random(),uri.toString(),false));
                    putDataForAllCategories(returnUpdatedImageMetaData(Config.random(),uri.toString(),false));
                    break;
                case "Formal-Shoes":
                    putDataInFirebaseDB(false,false,true,Config.FORMALS,Config.FOOTWEAR,returnUpdatedImageMetaData(Config.random(),uri.toString(),false));
                    putDataForAllCategories(returnUpdatedImageMetaData(Config.random(),uri.toString(),false));
                    break;
                case "Flip-flops":
                    putDataInFirebaseDB(false,false,true,Config.CASUALS,Config.FOOTWEAR,returnUpdatedImageMetaData(Config.random(),uri.toString(),false));
                    putDataForAllCategories(returnUpdatedImageMetaData(Config.random(),uri.toString(),false));
                    break;
                case "Sneakers & Vans":
                    putDataInFirebaseDB(false,false,true,Config.CASUALS,Config.FOOTWEAR,returnUpdatedImageMetaData(Config.random(),uri.toString(),false));
                    putDataInFirebaseDB(false,false,true,Config.PARTY_WEAR,Config.FOOTWEAR,returnUpdatedImageMetaData(Config.random(),uri.toString(),false));
                    putDataForAllCategories(returnUpdatedImageMetaData(Config.random(),uri.toString(),false));
                    break;
            }
            }
        });

    }



    public void putDataInFirebaseDB(boolean upperWear,boolean bottomWear,boolean footwear,String childRef,String childRef1,HashMap map){
        DatabaseReference reference = Singleton.getInstance().getFirebaseReference().getReferenceFromUrl(Config.GET_USERS_URL).child(MySharePreferences.getLoginUserName(getApplicationContext())).child("categories");
        if (upperWear){
            reference.child(childRef).child(childRef1).push().setValue(map);
        }else if (bottomWear){
            reference.child(childRef).child(childRef1).push().setValue(map);
        }else if (footwear){
            reference.child(childRef).child(childRef1).push().setValue(map);
        }
    }
    public void putDataForAllCategories(HashMap map){
        DatabaseReference reference = Singleton.getInstance().getFirebaseReference().getReferenceFromUrl(Config.GET_USERS_URL).child(MySharePreferences.getLoginUserName(getApplicationContext())).child("categories");
        reference.child("allCategories").push().setValue(map);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        progressDialog.dismiss();
//        startActivity(new Intent(AddNewProductActivity.this,Main2Activity.class));
        finish();
    }

    public HashMap returnUpdatedImageMetaData(String id,String url,boolean isBookmarked){
        HashMap map = new HashMap();
        map.put("id",id);
        map.put("url",url);
        map.put("isBookmarked",isBookmarked);
        return map;
    }
    public void setViewForData(RecyclerView recyclerView , ArrayList<CategoryTypeData>arrayList,int rows){
        layoutManager = new StaggeredGridLayoutManager(rows,StaggeredGridLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        productCategoryListAdapter = new ProductCategoryListAdapter(this,arrayList,Config.IS_ADD_IDENTIFIER);
        recyclerView.setAdapter(productCategoryListAdapter);
    }
    public void showOptionDialog(String categoryType){
        Config.CATEGORY = categoryType;
        myCustomizedDilog = new MyCustomizedDilog(this);
        myCustomizedDilog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        myCustomizedDilog.setDialogMessage("Choose from the following options");
        myCustomizedDilog.setPositivBtnText("Camera");
        myCustomizedDilog.setNegativBtnText("Gallery");
        myCustomizedDilog.setPositivBtnOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    String[] multi = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};
                    if (Build.VERSION.SDK_INT >= 23) {
                        requestPermissions(multi, 6);
                        int res = checkCallingOrSelfPermission(Manifest.permission.CAMERA);

                    }
                }else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, Config.CAMERA);
                    Singleton.getInstance().dismissAllDialogs();
                }
                }

        });
        myCustomizedDilog.setNegativBtnOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,
                            "Select Picture"), Config.GALLERY);
                    Singleton.getInstance().dismissAllDialogs();
            }
        });
        myCustomizedDilog.show();
        Singleton.getInstance().initializeVectorForDialog().add(myCustomizedDilog);
    }
    public void showCasualOptionDialog(){
        myCustomizedDilog = new MyCustomizedDilog(this);
        myCustomizedDilog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        myCustomizedDilog.setDialogMessage("Choose from the following options");
        myCustomizedDilog.setPositivBtnText("Shirt");
        myCustomizedDilog.setNegativBtnText("T-Shirt");
        myCustomizedDilog.setPositivBtnOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                               showOptionDialog("Casual-Shirts");
            }

        });
        myCustomizedDilog.setNegativBtnOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        showOptionDialog("T-Shirts");


            }
        });
        myCustomizedDilog.show();
        Singleton.getInstance().initializeVectorForDialog().add(myCustomizedDilog);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 6:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, Config.CAMERA);
                break;
        }
    }

    @Override
    public void onBackPressed(){
        if (myCustomizedDilog != null && myCustomizedDilog.isShowing()){
            Singleton.getInstance().dismissAllDialogs();
        }else {
//            startActivity(new Intent(AddNewProductActivity.this,Main2Activity.class));
            finish();
        }
    }
}

