package com.example.hemant.bottom;

public class Statistics {
    private String subscriberCount;

    private String videoCount;

    private String hiddenSubscriberCount;

    private String commentCount;

    private String viewCount;

    public String getSubscriberCount() {
        return subscriberCount;
    }

    public void setSubscriberCount(String subscriberCount) {
        this.subscriberCount = subscriberCount;
    }

    public String getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(String videoCount) {
        this.videoCount = videoCount;
    }

    public String getHiddenSubscriberCount() {
        return hiddenSubscriberCount;
    }

    public void setHiddenSubscriberCount(String hiddenSubscriberCount) {
        this.hiddenSubscriberCount = hiddenSubscriberCount;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    @Override
    public String toString() {
        return "ClassPojo [subscriberCount = " + subscriberCount + ", videoCount = " + videoCount + ", hiddenSubscriberCount = " + hiddenSubscriberCount + ", commentCount = " + commentCount + ", viewCount = " + viewCount + "]";
    }
}
