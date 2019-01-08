package com.example.hemant.bottom;

public class PostRetrieval {

    private String posts;
    private String postby;
    private int Likes;
    private String timeofposting;

    public PostRetrieval(){

    }

    public PostRetrieval(String posts, String postby, int likes, String timepst) {
        this.posts = posts;
        this.postby = postby;
        Likes = likes;
        this.timeofposting=timepst;
    }

    public String getPosts() {
        return posts;
    }

    public String getPostby() {
        return postby;
    }

    public int getLikes() {
        return Likes;
    }

    public String getTimeofposting() {
        return timeofposting;
    }
}
