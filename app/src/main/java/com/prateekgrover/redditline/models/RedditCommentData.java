package com.prateekgrover.redditline.models;

import com.google.gson.annotations.SerializedName;

public class RedditCommentData {
    private String kind;
    @SerializedName("data")
    private RedditComment redditComment;

    public RedditComment getRedditComment() {
        return redditComment;
    }

    public String getKind() {
        return kind;
    }
}
