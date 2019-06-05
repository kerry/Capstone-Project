package com.prateekgrover.redditline.models;

import com.google.gson.annotations.SerializedName;

public class RedditComment {
    private transient int level = 0;
    private String body;
    @SerializedName("replies")
    private RedditCommentsResponse replies;
    private String author;
    @SerializedName("created_utc")
    private long created;

    public String getBody() {
        return body;
    }

    public RedditCommentsResponse getReplies() {
        return replies;
    }

    public void setReplies(RedditCommentsResponse replies) {
        this.replies = replies;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public String getAuthor() {
        return author;
    }

    public long getCreated() {
        return created;
    }
}
