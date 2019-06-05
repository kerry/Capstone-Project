package com.prateekgrover.redditline.models;

import android.os.Parcel;
import android.os.Parcelable;

public class RedditSource implements Parcelable {
    private String url;

    protected RedditSource(Parcel in) {
        url = in.readString();
    }

    public static final Creator<RedditSource> CREATOR = new Creator<RedditSource>() {
        @Override
        public RedditSource createFromParcel(Parcel in) {
            return new RedditSource(in);
        }

        @Override
        public RedditSource[] newArray(int size) {
            return new RedditSource[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
    }

    public String getUrl() {
        return url;
    }
}
