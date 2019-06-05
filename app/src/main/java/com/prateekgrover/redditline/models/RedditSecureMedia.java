package com.prateekgrover.redditline.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class RedditSecureMedia implements Parcelable {
    @SerializedName("oembed")
    private RedditEmbed redditEmbed;

    protected RedditSecureMedia(Parcel in) {
        redditEmbed = in.readParcelable(RedditEmbed.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(redditEmbed, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public RedditEmbed getRedditEmbed() {
        return redditEmbed;
    }

    public static final Creator<RedditSecureMedia> CREATOR = new Creator<RedditSecureMedia>() {
        @Override
        public RedditSecureMedia createFromParcel(Parcel in) {
            return new RedditSecureMedia(in);
        }

        @Override
        public RedditSecureMedia[] newArray(int size) {
            return new RedditSecureMedia[size];
        }
    };
}
