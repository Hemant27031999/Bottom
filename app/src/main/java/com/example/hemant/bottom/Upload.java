package com.example.hemant.bottom;

public class Upload {
    String mName;
    String mImageUrl;

    public Upload(){

    }

    public Upload(String name, String imageUrl){

        if(name.trim().equals("")){
            name="No Name";
        }

        this.mName=name;
        this.mImageUrl=imageUrl;
    }

    public String getvName(){
        return mName;
    }

    public String getImageUrl(){
        return mImageUrl;
    }
}
