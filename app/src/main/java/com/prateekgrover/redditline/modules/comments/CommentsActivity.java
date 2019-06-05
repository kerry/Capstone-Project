package com.prateekgrover.redditline.modules.comments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.prateekgrover.redditline.R;
import com.prateekgrover.redditline.models.RedditComment;
import com.prateekgrover.redditline.models.RedditPost;
import com.prateekgrover.redditline.modules.videoplayer.VideoPlayerActivity;

import java.util.ArrayList;
import java.util.List;

public class CommentsActivity extends AppCompatActivity implements RedditPostActionListener {

    private CommentsViewModel commentsViewModel;
    private RedditPost mRedditPost;
    private RecyclerView mRecyclerView;
    private CommentsAdapter mAdapter;
    private List<RedditComment> mRedditCommentsList;
    private ProgressBar mProgressBar;
    private LinearLayout errorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("redditPost")) {
            mRedditPost = intent.getParcelableExtra("redditPost");
        }

        if (mRedditPost == null) {
            throw new IllegalStateException("Reddit Post cannot be null");
        }

        commentsViewModel = ViewModelProviders.of(this, new CommentsViewModelFactory(mRedditPost)).get(CommentsViewModel.class);
        mRedditCommentsList = new ArrayList<>();
        mRecyclerView = findViewById(R.id.comments_recycler_view);
        mAdapter = new CommentsAdapter(mRedditPost, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mProgressBar = findViewById(R.id.comments_pb);
        errorView = findViewById(R.id.comments_error_view);

        setTitle("Comments");

        mProgressBar.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);

        commentsViewModel.redditCommentListLiveData.observe(this, new Observer<List<RedditComment>>() {
            @Override
            public void onChanged(List<RedditComment> redditCommentList) {
                if (redditCommentList != null) {
                    mAdapter.updateData(redditCommentList);
                    mProgressBar.clearAnimation();
                    mProgressBar.setVisibility(View.INVISIBLE);
                    errorView.setVisibility(View.INVISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    // first time error
                    // show error
                    mProgressBar.clearAnimation();
                    mProgressBar.setVisibility(View.INVISIBLE);
                    errorView.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void redditVideoClicked(String url) {
        Intent videoIntent = new Intent(this, VideoPlayerActivity.class);
        videoIntent.putExtra("url", url);
        startActivity(videoIntent);
    }

    @Override
    public void urlClicked(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}
