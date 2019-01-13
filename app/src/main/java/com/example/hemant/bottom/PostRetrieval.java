package com.example.hemant.bottom;

public class PostRetrieval {

    private String posts;
    private String postby;
    private String timeofposting;

    public PostRetrieval(){

    }

    public PostRetrieval(String posts, String postby,  String timepst) {
        this.posts = posts;
        this.postby = postby;
        this.timeofposting=timepst;
    }

    public String getPosts() {
        return posts;
    }

    public String getPostby() {
        return postby;
    }

    public String getTimeofposting() {
        return timeofposting;
    }
}
