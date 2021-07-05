package com.example.infs3605_group_assignment;

import com.google.firebase.database.Exclude;

public class ImageUpload {

    private String mName;
    private String mImageUrl;
    private String mKey;

    public ImageUpload() {
        // empty constructor needed
    }

    public ImageUpload(String name, String imageUrl) {
        // If someone types in no name
        if (name.trim().equals("")) {
            name = "No Name";
        }

        mName = name;
        mImageUrl = imageUrl;
    }

    public String getName() {
        return mName;
    }

    public void setName (String name) {
        mName = name;
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
