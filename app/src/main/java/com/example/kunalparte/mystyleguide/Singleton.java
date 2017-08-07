package com.example.kunalparte.mystyleguide;

import android.app.Fragment;

import com.example.kunalparte.mystyleguide.Dialogs.MyCustomizedDilog;
import com.example.kunalparte.mystyleguide.Models.UserLoginDetais;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Stack;
import java.util.Vector;

/**
 * Created by kunalparte on 25/07/17.
 */

public class Singleton {
    private static Singleton singleton;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    //fragment stack of productList
    private Stack<Fragment> productListFragmentStack;
    private Stack<String> productListFragmentTitleStack;

    //fragment stack of suggestionsFragment
    private Stack<Fragment> suggestionFragmentStack;
    private Stack<String> suggestionFragmentTitleStack;

    //fragment stack of BookmarksFragment
    private Stack<Fragment> bookmarksFragmentStack;
    private Stack<String> bookmarksFragmentTitleStack;

    //fragment stack of prodfileFragment;
    private Stack<Fragment> profileFragmentStack;
    private Stack<String> profileFragmentTitleStack;

    //dialog Vector
    private Vector<MyCustomizedDilog> dialog;

    public static Singleton getInstance(){
        if (singleton== null){
            singleton = new Singleton();
        }
        return singleton;
    }

    //Firebase Database reference
    public FirebaseDatabase getFirebaseReference(){
        if (firebaseDatabase == null){
            firebaseDatabase = FirebaseDatabase.getInstance();
        }
        return firebaseDatabase;
    }

    //firebase Storeage reference
    public FirebaseStorage getFirebaseStorageReference(){
        if (firebaseStorage == null){
            firebaseStorage = FirebaseStorage.getInstance();
        }
        return firebaseStorage;
    }

    //profuctListFragment stack instance
    public Stack<Fragment> getProductListFragmentStack(){
        if (productListFragmentStack == null){
            productListFragmentStack = new Stack<>();
        }
        return productListFragmentStack;
    }

    //profuctListFragment title stack instance
    public Stack<String> getProductListFragTitleStack(){
        if (productListFragmentTitleStack == null){
            productListFragmentTitleStack = new Stack<>();
        }
        return productListFragmentTitleStack;
    }

    //suggestionFragment stack instance
    public Stack<Fragment> getSuggestionFragmentStack(){
        if (suggestionFragmentStack == null){
            suggestionFragmentStack = new Stack<>();
        }
        return suggestionFragmentStack;
    }

    //suggestionFragment title stack instance
    public Stack<String> getSuggestionFragTitleStack(){
        if (suggestionFragmentTitleStack == null){
            suggestionFragmentTitleStack = new Stack<>();
        }
        return suggestionFragmentTitleStack;
    }

    //bookmarksFragment stack instance
    public Stack<Fragment> getBookmarksFragmentStack(){
        if (bookmarksFragmentStack == null){
            bookmarksFragmentStack = new Stack<>();
        }
        return bookmarksFragmentStack;
    }

    //bookmarkFragment title stack instance
    public Stack<String> getBookmarksFragTitleStack(){
        if (bookmarksFragmentTitleStack == null){
            bookmarksFragmentTitleStack = new Stack<>();
        }
        return bookmarksFragmentTitleStack;
    }

    public Vector initializeVectorForDialog(){
        if (dialog == null){
            dialog = new Vector<>();
        }
        return dialog;
    }

    public void dismissAllDialogs(){
        if (dialog != null && dialog.size()>0){
            for (MyCustomizedDilog myCustomDialog : dialog){
                if (myCustomDialog.isShowing()){
                    myCustomDialog.dismiss();
                }
            }
        }
    }

    public void clearFragmentStacks(){
        if (productListFragmentStack.size() > 0){
            productListFragmentStack.clear();
            productListFragmentTitleStack.clear();
        }
        if (suggestionFragmentStack.size() > 0){
            suggestionFragmentStack.clear();
            suggestionFragmentTitleStack.clear();
        }
        if (bookmarksFragmentStack.size() > 0){
            bookmarksFragmentStack.clear();
            bookmarksFragmentTitleStack.clear();
        }
    }
}
