package com.example.infs3605_group_assignment.Text;

public class TextUpload {

    private String mTitle;
    private String mLocation;
    private String mNotes;
    private String mDate;

    public TextUpload() {
    }

    public TextUpload(String title, String location, String notes, String date) {

        mTitle = title;
        mLocation = location;
        mNotes = notes;
        mDate = date;
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
}
