package com.prateekgrover.redditline.modules.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.prateekgrover.redditline.R;
import com.prateekgrover.redditline.modules.home.HomeActivity;

public class SplashActivity extends AppCompatActivity {

    private SplashViewModel mSplashViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mSplashViewModel = ViewModelProviders.of(this).get(SplashViewModel.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForLogin();
    }

    public void checkForLogin() {
        mSplashViewModel.actionLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean value) {
                mSplashViewModel.actionLiveData.removeObserver(this);
                final Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                intent.putExtra("isLogin", value);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent);
                        finish();
                    }

                }, 1000);
            }
        });
        mSplashViewModel.isLoggedIn();
    }
}