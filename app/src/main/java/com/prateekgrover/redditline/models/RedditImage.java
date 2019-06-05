package com.prateekgrover.redditline.models;

import android.os.Parcel;
import android.os.Parcelable;

public class RedditImage implements Parcelable {
    private RedditSource source;

    protected RedditImage(Parcel in) {
        source = in.readParcelable(RedditSource.class.getClassLoader());
    }

    public static final Creator<RedditImage> CREATOR = new Creator<RedditImage>() {
        @Override
        public RedditImage createFromParcel(Parcel in) {
            return new RedditImage(in);
        }

        @Override
        public RedditImage[] newArray(int size) {
            return new RedditImage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(source, flags);
    }

    public RedditSource getSource() {
        return source;
    }
}
