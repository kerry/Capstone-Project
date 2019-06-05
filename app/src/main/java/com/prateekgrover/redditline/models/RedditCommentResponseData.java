package com.prateekgrover.redditline.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RedditCommentResponseData {
    @SerializedName("children")
    private List<RedditCommentData> redditCommentDataList;

    public List<RedditCommentData> getRedditCommentDataList() {
        return redditCommentDataList;
    }
}
