package com.prateekgrover.redditline.modules.home;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.adgvcxz.cardlayoutmanager.CardLayoutManager;
import com.adgvcxz.cardlayoutmanager.CardSnapHelper;
import com.adgvcxz.cardlayoutmanager.OnCardSwipeListener;
import com.prateekgrover.redditline.R;
import com.prateekgrover.redditline.models.RedditPost;

import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationService;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private List<RedditPost> mCurrentRedditPosts;
    private HomeCardAdapter mHomeCardAdapter;
    private HomeViewModel mHomeViewModel;
    private CardLayoutManager mLayoutManager;
    private String USED_INTENT = "1";

    private List<RedditPost> mRedditPosts;
    private CardView mLoginView;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mHomeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        mRecyclerView = findViewById(R.id.recycler_view);
        mProgressBar = findViewById(R.id.progress_bar);
        mLoginView = findViewById(R.id.login_view);

        mRecyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        mLayoutManager = new CardLayoutManager(CardLayoutManager.TransX.RIGHT, CardLayoutManager.TransY.NONE);
        mLayoutManager.setVerticalSwipe(false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mHomeCardAdapter = new HomeCardAdapter();
        mRecyclerView.setAdapter(mHomeCardAdapter);

        new CardSnapHelper().attachToRecyclerView(mRecyclerView);

        mLayoutManager.setYInterval(getResources().getDimensionPixelSize(R.dimen.card_bottom_interval));
        mLayoutManager.setShowCardCount(2);

        mCurrentRedditPosts = new ArrayList<>();

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mRedditPosts != null) {
                    System.out.println("TT: Adding more");
                    mCurrentRedditPosts.addAll(mRedditPosts);
                    mRedditPosts = null;
                    mHomeCardAdapter.notifyDataSetChanged();
                    showPosts();
                    mLayoutManager.setHorizontalSwipe(true);
                }
            }
        });

        mLayoutManager.setOnCardSwipeListener(new OnCardSwipeListener() {
            @Override
            public void onSwipe(View view, int position, int dx, int dy) {
                System.out.println("anim on swipe");
            }

            @Override
            public void onAnimOutStart(View view, int position, int direction) {
                System.out.println("anim out start " + position + " " + direction);
            }

            @Override
            public void onAnimOutStop(View view, int position, int direction) {
                System.out.println("anim out stop " + position + " " + direction);
                if (position == mCurrentRedditPosts.size() - 5) {
                    System.out.println("TT: going for refresh");
                    mHomeViewModel.fetchPosts();
                } else if (position == mCurrentRedditPosts.size() - 2) {
                    mLayoutManager.setHorizontalSwipe(false);
                    System.out.println("TT: clearing existing data");
                }
            }

            @Override
            public void onAnimInStart(View view, int position) {
                System.out.println("anim in start " + position + " ");
            }

            @Override
            public void onAnimInStop(View view, int position) {
                System.out.println("anim in stop " + position + " ");
            }
        });

        boolean isLogin = false;
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("isLogin")) {
            isLogin = intent.getBooleanExtra("isLogin", false);
        }


        final Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButtonClicked();
            }
        });

        mHomeViewModel.redditPostsLiveData.observe(this, new Observer<List<RedditPost>>() {
            @Override
            public void onChanged(List<RedditPost> redditPosts) {
                if (redditPosts != null) {
                    System.out.println("TT: in on changed");
                    if (mCurrentRedditPosts.isEmpty() || mRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                        System.out.println("TT: in new data");
                        mCurrentRedditPosts.addAll(redditPosts);
                        mHomeCardAdapter.updateData(mCurrentRedditPosts);
                        showPosts();
                        mLayoutManager.setHorizontalSwipe(true);
                    } else {
                        System.out.println("TT: in old data");
                        mRedditPosts = redditPosts;
                    }
                } else {
                    System.out.println("Error in getting data");
                }
            }
        });

        if (isLogin) {
            mRecyclerView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
            mLoginView.setVisibility(View.INVISIBLE);
            mHomeViewModel.fetchPosts();
        } else {
            mRecyclerView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
            mLoginView.setVisibility(View.VISIBLE);
        }
    }

    private void showPosts() {
        mProgressBar.clearAnimation();
        mLoginView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    public void loginButtonClicked() {
        mHomeViewModel.actionLiveData.observe(this, new Observer<AuthorizationRequest>() {
            @Override
            public void onChanged(AuthorizationRequest authorizationRequest) {
                mHomeViewModel.actionLiveData.removeObserver(this);
                AuthorizationService authorizationService = new AuthorizationService(HomeActivity.this);

                Intent postAuthorizationIntent = new Intent(HomeActivity.this, HomeActivity.class).setAction("com.prateekgrover.redditline.HANDLE_AUTHORIZATION_RESPONSE");
                PendingIntent pendingIntent = PendingIntent.getActivity(HomeActivity.this, 0, postAuthorizationIntent, 0);
                authorizationService.performAuthorizationRequest(authorizationRequest, pendingIntent);
            }
        });

        mHomeViewModel.loginButtonClicked();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();
            switch (action) {
                case "com.prateekgrover.redditline.HANDLE_AUTHORIZATION_RESPONSE":
                    redirectIntent(intent);
                    break;
                default:
            }
        }
    }

    private void redirectIntent(@Nullable Intent intent) {
        if (!intent.hasExtra(USED_INTENT)) {
            mHomeViewModel.actionLiveData.observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean value) {
                    mHomeViewModel.actionLiveData.removeObserver(this);
                    if (value) {
                        // user is authorized
                        // show progress bar
                        mLoginView.setVisibility(View.INVISIBLE);
                        mProgressBar.setVisibility(View.VISIBLE);
                        mProgressBar.animate();
                        // call for data
                    } else {

                    }
                }
            });
            mHomeViewModel.handleAuthorizationResponse(intent);
            intent.putExtra(USED_INTENT, true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();
            switch (action) {
                case "com.prateekgrover.redditline.HANDLE_AUTHORIZATION_RESPONSE":
                    redirectIntent(intent);
                    break;
                default:
            }
        }
    }
}
