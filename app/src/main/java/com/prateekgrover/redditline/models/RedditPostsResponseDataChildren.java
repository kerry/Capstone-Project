package com.prateekgrover.redditline.models;

import com.google.gson.annotations.SerializedName;

public class RedditPostsResponseDataChildren {
    @SerializedName("data")
    private RedditPost redditPost;

    public RedditPost getRedditPost() {
        return redditPost;
    }
}
