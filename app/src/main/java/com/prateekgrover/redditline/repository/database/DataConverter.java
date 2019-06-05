package com.prateekgrover.redditline.repository.database;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prateekgrover.redditline.models.RedditMedia;
import com.prateekgrover.redditline.models.RedditPostPreview;
import com.prateekgrover.redditline.models.RedditSecureMedia;

import java.lang.reflect.Type;

public class DataConverter {

    @TypeConverter
    public String fromRedditMedia(RedditMedia redditMedia) {
        if (redditMedia == null) {
            return null;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<RedditMedia>() {}.getType();
        String json = gson.toJson(redditMedia, type);
        return json;
    }

    @TypeConverter
    public RedditMedia toRedditMedia(String redditMediaString) {
        if (redditMediaString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<RedditMedia>() {}.getType();
        RedditMedia redditMedia = gson.fromJson(redditMediaString, type);
        return redditMedia;
    }

    @TypeConverter
    public String fromRedditPostPreview(RedditPostPreview redditPostPreview) {
        if (redditPostPreview == null) {
            return null;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<RedditPostPreview>() {}.getType();
        String json = gson.toJson(redditPostPreview, type);
        return json;
    }

    @TypeConverter
    public RedditPostPreview toRedditPostPreview(String redditPostPreviewString) {
        if (redditPostPreviewString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<RedditPostPreview>() {}.getType();
        RedditPostPreview redditPostPreview = gson.fromJson(redditPostPreviewString, type);
        return redditPostPreview;
    }

    @TypeConverter
    public String fromRedditSecureMedia(RedditSecureMedia redditSecureMedia) {
        if (redditSecureMedia == null) {
            return null;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<RedditSecureMedia>() {}.getType();
        String json = gson.toJson(redditSecureMedia, type);
        return json;
    }

    @TypeConverter
    public RedditSecureMedia toRedditSecureMedia(String redditSecureMediaString) {
        if (redditSecureMediaString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<RedditSecureMedia>() {}.getType();
        RedditSecureMedia redditSecureMedia = gson.fromJson(redditSecureMediaString, type);
        return redditSecureMedia;
    }
}
