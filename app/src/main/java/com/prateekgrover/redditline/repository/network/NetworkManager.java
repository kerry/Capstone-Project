package com.prateekgrover.redditline.repository.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class NetworkManager {

    private static final Object LOCK = new Object();
    private static NetworkManager sInstance;

    private static final String BASE_URL = "https://oauth.reddit.com";
    private HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    private final Retrofit mRetrofitInstance;
    private String mAuthorizarionHeaderValue = "";
    private final RedditAPI mRedditAPI;

    public static NetworkManager getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = new NetworkManager();
                }
            }
        }
        return sInstance;
    }

    private NetworkManager() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("Authorization", mAuthorizarionHeaderValue).build();
                return chain.proceed(request);
            }
        });

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient.addInterceptor(logging);

        mRetrofitInstance = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        mRedditAPI = mRetrofitInstance.create(RedditAPI.class);
    }

    public void updateAccessToken(String accessToken) {
        mAuthorizarionHeaderValue = "Bearer " + accessToken;
    }

    public RedditAPI getRedditAPI() {
        return mRedditAPI;
    }
}