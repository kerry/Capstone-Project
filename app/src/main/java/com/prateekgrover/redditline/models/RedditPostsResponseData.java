package com.prateekgrover.redditline.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RedditPostsResponseData {
    @SerializedName("children")
    private ArrayList<RedditPostsResponseDataChildren> children;

    public ArrayList<RedditPostsResponseDataChildren> getChildren() {
        return children;
    }
}
