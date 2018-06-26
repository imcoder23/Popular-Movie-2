package com.example.hunter.popularmovies.Model;

public class Trailer {
    private String mName;
    private String mkey;

    public Trailer(String Key , String Name){
            this.mName = Name;
            this.mkey = Key;
    }
    public Trailer(){}

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getMkey() {
        return mkey;
    }

    public void setMkey(String mkey) {
        this.mkey = mkey;
    }
}
