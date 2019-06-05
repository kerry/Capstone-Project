package com.prateekgrover.redditline.utils;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.prateekgrover.redditline.models.RedditCommentsResponse;

import java.io.IOException;

public class RedditCommentsResponseTypeAdapterFactory implements TypeAdapterFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (type.getRawType()!= RedditCommentsResponse.class) return null;

        TypeAdapter<RedditCommentsResponse> defaultAdapter = (TypeAdapter<RedditCommentsResponse>) gson.getDelegateAdapter(this, type);
        return (TypeAdapter<T>) new RedditCommentsResponseAdapter(defaultAdapter);
    }

    public class RedditCommentsResponseAdapter extends TypeAdapter<RedditCommentsResponse> {

        protected TypeAdapter<RedditCommentsResponse> defaultAdapter;


        public RedditCommentsResponseAdapter(TypeAdapter<RedditCommentsResponse> defaultAdapter) {
            this.defaultAdapter = defaultAdapter;
        }

        @Override
        public void write(JsonWriter out, RedditCommentsResponse value) throws IOException {
            defaultAdapter.write(out, value);
        }

        @Override
        public RedditCommentsResponse read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.STRING) {
                in.skipValue();
                return null;
            }
            return defaultAdapter.read(in);
        }
    }
}