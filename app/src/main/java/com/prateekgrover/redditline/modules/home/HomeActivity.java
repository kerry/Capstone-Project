package com.prateekgrover.redditline.modules.home;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.prateekgrover.redditline.R;
import com.prateekgrover.redditline.models.RedditPost;
import com.prateekgrover.redditline.modules.comments.CommentsActivity;

import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationService;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements HomePagerAdapter.RedditPostsRefreshListener {

    private List<RedditPost> mCurrentRedditPosts;
    private HomePagerAdapter mHomePagerAdapter;
    private HomeViewModel mHomeViewModel;
    private String USED_INTENT = "1";

    private List<RedditPost> mRedditPosts;
    private CardView mLoginView;
    private ViewPager mViewPager;
    private ProgressBar mProgressBar;
    private FrameLayout mLoadMoreView;
    private ProgressBar mLoadMoreProgressBar;
    private ImageButton mRefreshButton;
    private TextView mInfoText;
    private ImageButton mUpvoteButton;
    private ImageButton mDownvoteButton;
    private ImageButton mCommentsButton;
    private LinearLayout mBottomActionsView;
    private int mCurrentPosition = 0;

    @Override
    public void getItemCalledAtPosition(int position) {
        mHomeViewModel.getItemCalledAtPosition(position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mHomeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        mViewPager = findViewById(R.id.view_pager);
        mProgressBar = findViewById(R.id.progress_bar);
        mLoginView = findViewById(R.id.login_view);
        mLoadMoreView = findViewById(R.id.load_more_view);
        mLoadMoreProgressBar = findViewById(R.id.loading_pb);
        mRefreshButton = findViewById(R.id.refresh);
        mInfoText = findViewById(R.id.info_text);
        mUpvoteButton = findViewById(R.id.upvote);
        mDownvoteButton = findViewById(R.id.downvote);
        mCommentsButton = findViewById(R.id.comments);
        mBottomActionsView = findViewById(R.id.bottom_actions);

        mHomePagerAdapter = new HomePagerAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(mHomePagerAdapter);
        mCurrentRedditPosts = new ArrayList<>();

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

        mRefreshButton.setVisibility(View.VISIBLE);
        mLoadMoreProgressBar.setVisibility(View.INVISIBLE);
        mInfoText.setVisibility(View.INVISIBLE);

        mHomeViewModel.showLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean value) {
                if (value) {
                    mRefreshButton.setVisibility(View.INVISIBLE);
                    mLoadMoreProgressBar.setVisibility(View.VISIBLE);
                    mLoadMoreProgressBar.animate();
                }
            }
        });

        mHomeViewModel.redditPostsLiveData.observe(this, new Observer<List<RedditPost>>() {
            @Override
            public void onChanged(List<RedditPost> redditPosts) {
                mLoadMoreProgressBar.clearAnimation();
                mRefreshButton.setVisibility(View.VISIBLE);
                mLoadMoreProgressBar.setVisibility(View.INVISIBLE);
                mInfoText.setVisibility(View.VISIBLE);
                if (redditPosts != null) {
                    System.out.println("TT: in on changed");
                    mCurrentRedditPosts.addAll(redditPosts);
                    mHomePagerAdapter.updateData(redditPosts);
                    showPosts();
                    Boolean isLiked = mCurrentRedditPosts.get(0).isLikes();
                    mUpvoteButton.setSelected(isLiked != null && isLiked);
                    mDownvoteButton.setSelected(isLiked != null && !isLiked);
                } else {
                    System.out.println("Error in getting data");
                }
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
                Boolean isLiked = mCurrentRedditPosts.get(position).isLikes();
                mUpvoteButton.setSelected(isLiked != null && isLiked);
                mDownvoteButton.setSelected(isLiked != null && !isLiked);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mUpvoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RedditPost redditPost = mCurrentRedditPosts.get(mCurrentPosition);
                int dir = redditPost.isLikes() != null ? (redditPost.isLikes() ? 0 : 1) : 1;

                mHomeViewModel.voteButtonClicked(mCurrentRedditPosts.get(mCurrentPosition), dir).observe(HomeActivity.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean value) {
                        System.out.println("upvote done " + value);
                        if (value) {
                            mCurrentRedditPosts.get(mCurrentPosition).setLikes(dir == 0 ? null : dir == 1);
                            mUpvoteButton.setSelected(dir == 1);
                            mDownvoteButton.setSelected(false);
                        }
                    }
                });
            }
        });

        mDownvoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RedditPost redditPost = mCurrentRedditPosts.get(mCurrentPosition);
                int dir = redditPost.isLikes() != null ? (redditPost.isLikes() ? -1 : 0) : -1;

                mHomeViewModel.voteButtonClicked(mCurrentRedditPosts.get(mCurrentPosition), dir).observe(HomeActivity.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean value) {
                        System.out.println("upvote done " + value);
                        if (value) {
                            mCurrentRedditPosts.get(mCurrentPosition).setLikes(dir == 0 ? null : dir == 1);
                            mUpvoteButton.setSelected(false);
                            mDownvoteButton.setSelected(dir == -1);
                        }
                    }
                });
            }
        });

        mCommentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CommentsActivity.class);
                intent.putExtra("redditPost", mCurrentRedditPosts.get(mCurrentPosition));
                startActivity(intent);
            }
        });

        if (isLogin) {
            mViewPager.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
            mLoginView.setVisibility(View.INVISIBLE);
            mLoadMoreView.setVisibility(View.INVISIBLE);
            mInfoText.setVisibility(View.INVISIBLE);
            mBottomActionsView.setVisibility(View.INVISIBLE);
            mHomeViewModel.fetchPosts();
        } else {
            mViewPager.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
            mLoginView.setVisibility(View.VISIBLE);
            mLoadMoreView.setVisibility(View.INVISIBLE);
            mInfoText.setVisibility(View.INVISIBLE);
            mBottomActionsView.setVisibility(View.INVISIBLE);
        }
    }

//    private void toggleUpvoteButton(boolean value) {
//        ImageViewCompat.setImageTintList(mUpvoteButton, ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorAccent)));
//        ImageViewCompat.setImageTintList(mDownvoteButton, ColorStateList.valueOf(ContextCompat.getColor(this, R.color.)));
//        mDownvoteButton.setBackgroundColor(this.getResources().getColor(R.color.textGrayBackground));
//    }

    private void showPosts() {
        mProgressBar.clearAnimation();
        mLoginView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mViewPager.setVisibility(View.VISIBLE);
        mLoadMoreView.setVisibility(View.VISIBLE);
        mBottomActionsView.setVisibility(View.VISIBLE);
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
