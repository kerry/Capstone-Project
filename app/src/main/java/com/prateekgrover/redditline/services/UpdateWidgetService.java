package com.prateekgrover.redditline.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.prateekgrover.redditline.R;
import com.prateekgrover.redditline.models.RedditPost;
import com.prateekgrover.redditline.models.RedditPostsResponse;
import com.prateekgrover.redditline.models.RedditPostsResponseDataChildren;
import com.prateekgrover.redditline.modules.widget.RedditLine;
import com.prateekgrover.redditline.repository.database.DatabaseManager;
import com.prateekgrover.redditline.repository.database.SharedPreferenceManager;
import com.prateekgrover.redditline.repository.network.NetworkManager;
import com.prateekgrover.redditline.utils.AppExecutors;
import com.prateekgrover.redditline.utils.AppState;

import net.openid.appauth.AuthState;

import org.json.JSONException;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateWidgetService extends IntentService {

    public UpdateWidgetService() {
        super("Update Widget Service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        System.out.println("widget: update service started");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            String channelId = "widget_update_channel";
            CharSequence channelName = "Widget Notifications";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(notificationChannel);

            Notification.Builder builder = new Notification.Builder(this, "widget_update_channel")
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Updating widget")
                    .setAutoCancel(true);

            Notification notification = builder.build();
            startForeground(1, notification);

        } else {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Updating widget")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            Notification notification = builder.build();

            startForeground(1, notification);
        }
        boolean isLoggedIn = false;
        String authStateString = SharedPreferenceManager.getInstance(getApplication().getApplicationContext()).getString(SharedPreferenceManager.Keys.AUTH, null);
        try {
            AuthState authState = AuthState.jsonDeserialize(authStateString);
            NetworkManager.getInstance().updateAccessToken(authState.getAccessToken());
            AppState.getInstance().updateAuthState(authState);
            isLoggedIn = authState.isAuthorized() && !authState.getNeedsTokenRefresh();
        } catch (NullPointerException | JSONException e) {
            isLoggedIn = false;
        }

        if (isLoggedIn) {
            System.out.println("widget: logged in");
            getData(intent);
        } else {

        }
    }

    private void getData(final Intent intent) {
        System.out.println("widget: network called");
        NetworkManager.getInstance().getRedditAPI().getTopPosts().enqueue(new Callback<RedditPostsResponse>() {
            @Override
            public void onResponse(Call<RedditPostsResponse> call, Response<RedditPostsResponse> response) {
                System.out.println("widget: got response");
                RedditPostsResponse redditPostsResponse = response.body();
                if (redditPostsResponse != null) {
                    ArrayList<RedditPost> redditPosts = new ArrayList<>();
                    for (RedditPostsResponseDataChildren child : redditPostsResponse.getRedditPostsResponseData().getChildren()) {
                        redditPosts.add(child.getRedditPost());
                    }
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            DatabaseManager.getInstance(UpdateWidgetService.this.getApplicationContext()).redditPostDao().insertRedditPosts(redditPosts);
                        }
                    });

                    updateAppWidget(redditPosts, intent);
                } else {
                    System.out.println("widget: null response from network");
                }
            }

            @Override
            public void onFailure(Call<RedditPostsResponse> call, Throwable t) {
                System.out.println("widget: failure response from network");
            }
        });
    }

    private void updateAppWidget(ArrayList<RedditPost> redditPosts, Intent intent) {
        System.out.println("widget: updating app widget with count " + redditPosts.size());
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("REDDITLINE_WIDGET_UPDATE");
        broadcastIntent.setComponent(new ComponentName(this.getApplicationContext(), RedditLine.class));
        broadcastIntent.putParcelableArrayListExtra("reddit_post_list", redditPosts);
        sendBroadcast(broadcastIntent);
    }
}
