package com.example.infs3605_group_assignment.Video;

import com.google.firebase.database.Exclude;

public class VideoUpload {

    private String mTitle;
    private String mLocation;
    private String mNotes;
    private String mDate;
    private String mVideoUrl;
    private String mKey;

    public VideoUpload() {
        // empty constructor needed
    }

    public VideoUpload(String title, String location, String notes, String date, String videoUrl) {

        mTitle = title;
        mLocation = location;
        mNotes = notes;
        mDate = date;
        mVideoUrl = videoUrl;
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

    public String getmVideoUrl() {
        return mVideoUrl;
    }

    public void setmVideoUrl(String mVideoUrl) {
        this.mVideoUrl = mVideoUrl;
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
