package com.example.kunalparte.mystyleguide.Models;

/**
 * Created by kunalparte on 28/07/17.
 */

public class ImageMetaData {
    String id;
    String imageUri;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        isBookmarked = bookmarked;
    }

    boolean isBookmarked;
}
