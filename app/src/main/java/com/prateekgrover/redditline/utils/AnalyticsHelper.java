package com.prateekgrover.redditline.utils;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class AnalyticsHelper {
    private static AnalyticsHelper instance;

    private AnalyticsHelper mAnalyticsHelper;
    private FirebaseAnalytics mFirebaseAnalytics;

    public static synchronized AnalyticsHelper getInstance(Context context) {
        if (instance == null) {
            instance = new AnalyticsHelper(context);
        }

        return instance;
    }

    private AnalyticsHelper(Context context) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    public void recordLoginButtonClickEvent() {
        Bundle bundle = new Bundle();
        bundle.putString("type", "click");
        mFirebaseAnalytics.logEvent("login_button", bundle);
    }

    public void recordUpvoteButtonClickEvent(String postId) {
        Bundle bundle = new Bundle();
        bundle.putString("type", "click");
        bundle.putString("post_id", postId);
        mFirebaseAnalytics.logEvent("upvote_post", bundle);
    }

    public void recordDownvoteButtonClickEvent(String postId) {
        Bundle bundle = new Bundle();
        bundle.putString("type", "click");
        bundle.putString("post_id", postId);
        mFirebaseAnalytics.logEvent("downvote_post", bundle);
    }

    public void recordCommentsButtonClickEvent(String postId) {
        Bundle bundle = new Bundle();
        bundle.putString("type", "click");
        bundle.putString("post_id", postId);
        mFirebaseAnalytics.logEvent("open_comments", bundle);
    }
}
