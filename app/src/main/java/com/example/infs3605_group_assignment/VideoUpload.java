package com.example.infs3605_group_assignment;

public class VideoUpload {

    private String mName;
    private String mVideoUri;

    public VideoUpload() {

    }

    public VideoUpload(String name, String videoUri) {
        if (name.trim().equals("")) {
            name = "No Name";
        }

        mName = name;
        mVideoUri = videoUri;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmVideoUri() {
        return mVideoUri;
    }

    public void setmVideoUri(String mVideoUri) {
        this.mVideoUri = mVideoUri;
    }
}
