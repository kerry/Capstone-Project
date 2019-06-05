package com.prateekgrover.redditline.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class RedditVideo implements Parcelable {
    @SerializedName("fallback_url")
    private String url;
    private int duration;

    protected RedditVideo(Parcel in) {
        url = in.readString();
        duration = in.readInt();
    }

    public static final Creator<RedditVideo> CREATOR = new Creator<RedditVideo>() {
        @Override
        public RedditVideo createFromParcel(Parcel in) {
            return new RedditVideo(in);
        }

        @Override
        public RedditVideo[] newArray(int size) {
            return new RedditVideo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeInt(duration);
    }
}
