package com.example.kunalparte.mystyleguide.Models;

/**
 * Created by kunalparte on 30/07/17.
 */

public class ImageModelClass {

    String id;
    String url;
    public ImageModelClass(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        isBookmarked = bookmarked;
    }

    boolean isBookmarked;

    public ImageModelClass(String id, String url, boolean isBookmarked){
        this.url = url;
        this.id = id;
        this.isBookmarked = isBookmarked;
    }
}
