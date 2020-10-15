package com.example.ideaorganizer;

import com.google.firebase.database.Exclude;

public class Upload {
    private String mNote;
    private String mKey;
    private String mImageUrl;

    public Upload() {
        //empty constructor needed
    }
    public Upload(String name) {
        if (name.trim().equals("")) {
            name = "No Name";
        }
        mNote = name;

    }
    public String getName() {
        return mNote;
    }
    public void setName(String name) {
        mNote = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }
    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    @Exclude
    public String getKey() {
        return mKey;
    }

    @Exclude
    public void setKey(String key) {
        mKey = key;
    }
}
