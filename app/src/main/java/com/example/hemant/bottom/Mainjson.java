package com.example.hemant.bottom;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Mainjson {
    @SerializedName("items")
    private ArrayList<Items> items;

    @SerializedName("kind")
    private String kind;

    @SerializedName("etag")
    private String etag;

    @SerializedName("pageInfo")
    private PageInfo pageInfo;

    public ArrayList<Items> getItems() {
        return items;
    }

    public void setItems(ArrayList<Items> items) {
        this.items = items;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }
}
