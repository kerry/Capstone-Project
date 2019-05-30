package com.prateekgrover.redditline.repository.network;

import com.prateekgrover.redditline.models.RedditPost;
import com.prateekgrover.redditline.models.RedditPostsResponse;
import com.prateekgrover.redditline.models.RedditPostsResponseData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RedditAPI {
    @GET("/.json")
    Call<RedditPostsResponse> getTopPosts();

    @GET("/.json")
    Call<RedditPostsResponse> getMorePosts(@Query("after") String postId);
}
