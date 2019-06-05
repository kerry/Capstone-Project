package com.prateekgrover.redditline.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;
import com.prateekgrover.redditline.repository.database.DataConverter;

@Entity(tableName = "reddit_post")
public class RedditPost implements Parcelable {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private String postId;

    @ColumnInfo(name = "subreddit_id")
    @SerializedName("subreddit_id")
    private String subredditId;

    @ColumnInfo(name = "subreddit_name_prefixed")
    @SerializedName("subreddit_name_prefixed")
    private String subredditName;

    @ColumnInfo(name = "title")
    @SerializedName("title")
    private String title;

    @ColumnInfo(name = "selftext")
    @SerializedName("selftext")
    private String selfText;

    @ColumnInfo(name = "created_utc")
    @SerializedName("created_utc")
    private long createdAt;

    @ColumnInfo(name = "url")
    private String url;

    @ColumnInfo(name = "is_video")
    @SerializedName("is_video")
    private boolean isVideo;

    @ColumnInfo(name = "media")
    @SerializedName("media")
    @TypeConverters(DataConverter.class)
    private RedditMedia redditMedia;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    private String postName;

    @ColumnInfo(name = "preview")
    @SerializedName("preview")
    @TypeConverters(DataConverter.class)
    private RedditPostPreview preview;

    @ColumnInfo(name = "secure_media")
    @SerializedName("secure_media")
    @TypeConverters(DataConverter.class)
    private RedditSecureMedia secureMedia;

    @ColumnInfo(name = "permalink")
    private String permalink;

    @ColumnInfo(name = "likes")
    private Boolean likes;

    public RedditPost() {
    }

    protected RedditPost(Parcel in) {
        subredditId = in.readString();
        subredditName = in.readString();
        title = in.readString();
        selfText = in.readString();
        createdAt = in.readLong();
        url = in.readString();
        isVideo = in.readByte() != 0;
        redditMedia = in.readParcelable(RedditMedia.class.getClassLoader());
        postName = in.readString();
        preview = in.readParcelable(RedditPostPreview.class.getClassLoader());
        secureMedia = in.readParcelable(RedditSecureMedia.class.getClassLoader());
        permalink = in.readString();
        postId = in.readString();
        likes = (Boolean) in.readSerializable();
    }

    public static final Creator<RedditPost> CREATOR = new Creator<RedditPost>() {
        @Override
        public RedditPost createFromParcel(Parcel in) {
            return new RedditPost(in);
        }

        @Override
        public RedditPost[] newArray(int size) {
            return new RedditPost[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubredditName() {
        return subredditName;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public String getUrl() {
        return url;
    }

    public String getRedditVideoUrl() {
        if (redditMedia == null) {
            return "";
        } else {
            return redditMedia.getRedditVideo().getUrl();
        }
    }

    public String getPreviewUrl() {
        return preview == null ? null : preview.getImages().get(0).getSource().getUrl();
    }

    public String getSelfText() {
        return selfText;
    }

    public String getPostName() {
        return postName;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public String getEmbedType() {
        return secureMedia != null ? secureMedia.getRedditEmbed().getType() : "";
    }

    public String getPermalink() {
        return permalink;
    }

    public String getPostId() {
        return postId;
    }

    public Boolean isLikes() {
        return likes;
    }

    public void setLikes(Boolean likes) {
        this.likes = likes;
    }

    public RedditMedia getRedditMedia() {
        return redditMedia;
    }

    public RedditPostPreview getPreview() {
        return preview;
    }

    public RedditSecureMedia getSecureMedia() {
        return secureMedia;
    }

    public String getSubredditId() {
        return subredditId;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public void setPreview(RedditPostPreview preview) {
        this.preview = preview;
    }

    public void setRedditMedia(RedditMedia redditMedia) {
        this.redditMedia = redditMedia;
    }

    public void setSecureMedia(RedditSecureMedia secureMedia) {
        this.secureMedia = secureMedia;
    }

    public void setSelfText(String selfText) {
        this.selfText = selfText;
    }

    public void setSubredditId(String subredditId) {
        this.subredditId = subredditId;
    }

    public void setSubredditName(String subredditName) {
        this.subredditName = subredditName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(subredditId);
        dest.writeString(subredditName);
        dest.writeString(title);
        dest.writeString(selfText);
        dest.writeLong(createdAt);
        dest.writeString(url);
        dest.writeByte((byte) (isVideo ? 1 : 0));
        dest.writeParcelable(redditMedia, flags);
        dest.writeString(postName);
        dest.writeParcelable(preview, flags);
        dest.writeParcelable(secureMedia, flags);
        dest.writeString(permalink);
        dest.writeString(postId);
        dest.writeSerializable(likes);
    }
}
