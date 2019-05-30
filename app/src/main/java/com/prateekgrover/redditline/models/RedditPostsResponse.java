package com.prateekgrover.redditline.models;

import com.google.gson.annotations.SerializedName;

public class RedditPostsResponse {
    @SerializedName("data")
    private RedditPostsResponseData redditPostsResponseData;

    public RedditPostsResponseData getRedditPostsResponseData() {
        return redditPostsResponseData;
    }
}
