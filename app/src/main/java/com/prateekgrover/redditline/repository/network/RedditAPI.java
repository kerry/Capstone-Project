package com.prateekgrover.redditline.repository.network;

import com.prateekgrover.redditline.models.RedditCommentsResponse;
import com.prateekgrover.redditline.models.RedditPostsResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RedditAPI {
    @GET("/.json")
    Call<RedditPostsResponse> getTopPosts();

    @GET("/.json")
    Call<RedditPostsResponse> getMorePosts(@Query("after") String postId);

    @GET("/{subredditName}/comments/{postId}/")
    Call<List<RedditCommentsResponse>> getComments(@Path("subredditName") String subredditName, @Path("postId") String postId, @Query("depth") int depth);

    @FormUrlEncoded
    @POST("/api/vote")
    Call<ResponseBody> vote(@Field("dir") int dir, @Field("id") String id);
}
