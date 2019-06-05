package com.prateekgrover.redditline.utils;

import android.content.Context;

import net.openid.appauth.AuthState;

public class AppState {

    private static AppState instance;

    private AuthState mAuthState;

    public static synchronized AppState getInstance() {
        if (instance == null) {
            instance = new AppState();
        }

        return instance;
    }

    private AppState() {

    }

    public void updateAuthState(AuthState authState) {
        mAuthState = authState;
    }

    public String getAccessToken() {
        return mAuthState != null ? mAuthState.getAccessToken() : "";
    }
}
