package com.prateekgrover.redditline.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class RedditMedia implements Parcelable {
    @SerializedName("reddit_video")
    private RedditVideo redditVideo;

    protected RedditMedia(Parcel in) {
        redditVideo = in.readParcelable(RedditVideo.class.getClassLoader());
    }

    public static final Creator<RedditMedia> CREATOR = new Creator<RedditMedia>() {
        @Override
        public RedditMedia createFromParcel(Parcel in) {
            return new RedditMedia(in);
        }

        @Override
        public RedditMedia[] newArray(int size) {
            return new RedditMedia[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public RedditVideo getRedditVideo() {
        return redditVideo;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(redditVideo, flags);
    }
}
