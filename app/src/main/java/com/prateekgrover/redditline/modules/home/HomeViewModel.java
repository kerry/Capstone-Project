package com.prateekgrover.redditline.modules.home;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.prateekgrover.redditline.models.RedditPost;
import com.prateekgrover.redditline.models.RedditPostsResponse;
import com.prateekgrover.redditline.models.RedditPostsResponseData;
import com.prateekgrover.redditline.models.RedditPostsResponseDataChildren;
import com.prateekgrover.redditline.repository.database.SharedPreferenceManager;
import com.prateekgrover.redditline.repository.network.NetworkManager;
import com.prateekgrover.redditline.utils.SingleLiveEvent;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.TokenResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends AndroidViewModel {
    public SingleLiveEvent actionLiveData;
    public final MutableLiveData<List<RedditPost>> redditPostsLiveData = new MutableLiveData<>();
    private SharedPreferenceManager mSharedPreferenceManager;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        actionLiveData = new SingleLiveEvent();
        mSharedPreferenceManager = SharedPreferenceManager.getInstance(getApplication().getApplicationContext());
    }

    public void loginButtonClicked() {
        String uuid = UUID.randomUUID().toString();
        AuthorizationServiceConfiguration serviceConfiguration = new AuthorizationServiceConfiguration(
                Uri.parse("https://www.reddit.com/api/v1/authorize") /* auth endpoint */,
                Uri.parse("https://www.reddit.com/api/v1/access_token") /* token endpoint */
        );
        String clientId = "HIx5ExiMorG7Iw";
        Uri redirectUri = Uri.parse("com.prateekgrover.redditline://oauth2callback");
        AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(
                serviceConfiguration,
                clientId,
                "code",
                redirectUri
        );
        builder.setState(uuid);
        Map<String, String> additionalParameters = new HashMap<>();
        additionalParameters.put("duration", "permanent");
        builder.setAdditionalParameters(additionalParameters);
        builder.setScopes("identity", "mysubreddits", "read", "save", "submit", "subscribe", "vote");
        AuthorizationRequest request = builder.build();
        actionLiveData.setValue(request);
    }

    public void handleAuthorizationResponse(Intent intent) {
        AuthorizationResponse response = AuthorizationResponse.fromIntent(intent);
        AuthorizationException error = AuthorizationException.fromIntent(intent);
        final AuthState authState = new AuthState(response, error);

        if (response != null) {
            AuthorizationService service = new AuthorizationService(getApplication().getApplicationContext());
            service.performTokenRequest(response.createTokenExchangeRequest(), RedditNoClientAuthentication.INSTANCE, new AuthorizationService.TokenResponseCallback() {
                @Override
                public void onTokenRequestCompleted(@Nullable TokenResponse tokenResponse, @Nullable AuthorizationException exception) {
                    if (exception != null) {
                        actionLiveData.setValue(false);
                    } else {
                        if (tokenResponse != null) {
                            authState.update(tokenResponse, exception);
                            mSharedPreferenceManager.putString(SharedPreferenceManager.Keys.AUTH, authState.jsonSerializeString());
                            actionLiveData.setValue(true);
                            NetworkManager.getInstance().updateAccessToken(tokenResponse.accessToken);
                            fetchPosts();
                        }
                    }
                }
            });
        }
    }

    public void fetchPosts() {
        Call<RedditPostsResponse> redditPostsResponseCall;
        List<RedditPost> redditPosts = redditPostsLiveData.getValue();
        if (redditPosts != null) {
            redditPostsResponseCall = NetworkManager.getInstance().getRedditAPI().getMorePosts(redditPosts.get(redditPosts.size()-1).getPostId());
        } else {
            redditPostsResponseCall = NetworkManager.getInstance().getRedditAPI().getTopPosts();
        }
        redditPostsResponseCall.enqueue(new Callback<RedditPostsResponse>() {
            @Override
            public void onResponse(Call<RedditPostsResponse> call, Response<RedditPostsResponse> response) {
                RedditPostsResponse redditPostsResponse = response.body();
                if (redditPostsResponse != null) {
                    ArrayList<RedditPost> redditPosts = new ArrayList<>();
                    for (RedditPostsResponseDataChildren child : redditPostsResponse.getRedditPostsResponseData().getChildren()) {
                        redditPosts.add(child.getRedditPost());
                    }
                    redditPostsLiveData.setValue(redditPosts);
                } else {
                    redditPostsLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<RedditPostsResponse> call, Throwable t) {
                redditPostsLiveData.setValue(null);
            }
        });
    }
}
