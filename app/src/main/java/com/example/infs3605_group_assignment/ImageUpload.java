package com.example.infs3605_group_assignment;

import com.google.firebase.database.Exclude;

public class ImageUpload {

    private String mTitle;
    private String mLocation;
    private String mNotes;
    private String mDate;
    private String mImageUrl;
//    private String mKey;

    public ImageUpload() {
        // empty constructor needed
    }

    public ImageUpload(String title, String location, String notes, String date, String imageUrl) {
        if (title.trim().equals("")) {
            title = "No Name";
        }

        mTitle = title;
        mLocation = location;
        mNotes = notes;
        mDate = date;
        mImageUrl = imageUrl;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmLocation() {
        return mLocation;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public String getmNotes() {
        return mNotes;
    }

    public void setmNotes(String mNotes) {
        this.mNotes = mNotes;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

//    @Exclude
//    public String getKey() {
//        return mKey;
//    }
//
//    @Exclude
//    public void setKey(String key) {
//        mKey = key;
//    }
}
