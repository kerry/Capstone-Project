package com.prateekgrover.redditline.models;

import com.google.gson.annotations.SerializedName;

public class RedditCommentsResponse {

    @SerializedName("data")
    private RedditCommentResponseData redditCommentResponseData;

    public RedditCommentResponseData getRedditCommentResponseData() {
        return redditCommentResponseData;
    }
}
