package com.example.hunter.popularmovies.Model;

public class Reviews {
    private String mAuthor;
    private String mContent;

    public String getmAuthor() {
        return mAuthor;
    }

    public void setmAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public Reviews(String mAuthor, String mContent) {
        this.mAuthor = mAuthor;

        this.mContent = mContent;
    }
}
