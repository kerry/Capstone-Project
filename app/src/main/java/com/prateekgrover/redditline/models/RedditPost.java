package com.prateekgrover.redditline.models;

import com.google.gson.annotations.SerializedName;

public class RedditPost {
    @SerializedName("subreddit_id")
    private String subredditId;
    @SerializedName("subreddit")
    private String subredditName;
    @SerializedName("title")
    private String title;
    @SerializedName("selftext")
    private String selfText;
    @SerializedName("created_utc")
    private long createdAt;
    private String url;
    @SerializedName("is_video")
    private boolean isVideo;
    @SerializedName("media")
    private RedditMedia redditMedia;
    @SerializedName("name")
    private String postId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPostId() {
        return postId;
    }
}
