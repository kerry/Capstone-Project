package com.prateekgrover.redditline.modules.splash;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.prateekgrover.redditline.repository.database.SharedPreferenceManager;
import com.prateekgrover.redditline.repository.network.NetworkManager;
import com.prateekgrover.redditline.utils.SingleLiveEvent;

import net.openid.appauth.AuthState;

import org.json.JSONException;

public class SplashViewModel extends AndroidViewModel {
    public SingleLiveEvent actionLiveData;
    public SplashViewModel(@NonNull Application application) {
        super(application);
        actionLiveData = new SingleLiveEvent();
    }

    public void isLoggedIn() {
        String authStateString = SharedPreferenceManager.getInstance(getApplication().getApplicationContext()).getString(SharedPreferenceManager.Keys.AUTH, null);
        System.out.println("Auth state " + authStateString);
        try {
            AuthState authState = AuthState.jsonDeserialize(authStateString);
            NetworkManager.getInstance().updateAccessToken(authState.getAccessToken());
            actionLiveData.setValue(authState.isAuthorized() && !authState.getNeedsTokenRefresh());
        } catch (NullPointerException | JSONException e) {
            actionLiveData.setValue(false);
        }
    }
}
