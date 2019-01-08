package com.example.hemant.bottom;

public class Posts {

    public String posts;
    public String postby;
    public int Likes;
    public String timeofposting;

    public Posts(){}

    public
    Posts(int c,String pst,String pstby,String postingtime){
        Likes=c;
        posts=pst;
        postby=pstby;
        timeofposting=postingtime;
    }
}
