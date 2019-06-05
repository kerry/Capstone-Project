package com.prateekgrover.redditline.models;

import android.os.Parcel;
import android.os.Parcelable;

public class RedditEmbed implements Parcelable {
    private String type;

    protected RedditEmbed(Parcel in) {
        type = in.readString();
    }

    public static final Creator<RedditEmbed> CREATOR = new Creator<RedditEmbed>() {
        @Override
        public RedditEmbed createFromParcel(Parcel in) {
            return new RedditEmbed(in);
        }

        @Override
        public RedditEmbed[] newArray(int size) {
            return new RedditEmbed[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public String getType() {
        return type;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
    }
}
