package com.prateekgrover.redditline.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RedditPostPreview implements Parcelable {
    @SerializedName("images")
    private List<RedditImage> images;

    protected RedditPostPreview(Parcel in) {
        images = in.createTypedArrayList(RedditImage.CREATOR);
    }

    public static final Creator<RedditPostPreview> CREATOR = new Creator<RedditPostPreview>() {
        @Override
        public RedditPostPreview createFromParcel(Parcel in) {
            return new RedditPostPreview(in);
        }

        @Override
        public RedditPostPreview[] newArray(int size) {
            return new RedditPostPreview[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(images);
    }

    public List<RedditImage> getImages() {
        return images;
    }
}
