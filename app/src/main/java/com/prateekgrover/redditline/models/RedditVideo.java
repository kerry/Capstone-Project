package com.prateekgrover.redditline.models;

import com.google.gson.annotations.SerializedName;

public class RedditVideo {
    @SerializedName("fallback_url")
    private String url;
    private int duration;
}
