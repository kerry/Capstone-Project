package com.prateekgrover.redditline.modules.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.prateekgrover.redditline.R;
import com.prateekgrover.redditline.models.RedditPost;
import com.prateekgrover.redditline.modules.comments.CommentsActivity;
import com.prateekgrover.redditline.modules.videoplayer.VideoPlayerActivity;
import com.prateekgrover.redditline.utils.ImageHelper;
import com.prateekgrover.redditline.utils.Utils;

public class RedditPostFragment extends Fragment {

    private RedditPost redditPost;

    public static RedditPostFragment newInstance(RedditPost redditPost) {
        Bundle args = new Bundle();
        args.putParcelable("redditPost", redditPost);
        RedditPostFragment fragment = new RedditPostFragment();
        fragment.setArguments(args);
        System.out.println("fragment " + fragment);
        return fragment;
    }

    public RedditPostFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            redditPost = getArguments().getParcelable("redditPost");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layoutView = inflater.inflate(R.layout.fragment_reddit_post, container, false);
        CardView postView = layoutView.findViewById(R.id.post_view);
        postView.setCardElevation(getActivity().getResources().getDimension(R.dimen.card_elevation));
        TextView title = layoutView.findViewById(R.id.post_title);
        title.setText(redditPost.getTitle());
        TextView subredditName = layoutView.findViewById(R.id.subreddit_name);
        subredditName.setText(redditPost.getSubredditName());
        TextView postTime = layoutView.findViewById(R.id.post_time);
        postTime.setText(Utils.getDaysAgo(redditPost.getCreatedAt(), getContext()));
        TextView postText = layoutView.findViewById(R.id.post_text);
        ImageView postPreview = layoutView.findViewById(R.id.post_preview);
        FrameLayout videoPlayImageView = layoutView.findViewById(R.id.video_play_image);
        if (redditPost.getPreviewUrl() == null) {
            if (redditPost.getSelfText().isEmpty()) {
                postPreview.setVisibility(View.VISIBLE);
                videoPlayImageView.setVisibility(View.GONE);
                postPreview.setImageResource(R.drawable.click);
                postText.setVisibility(View.GONE);
                postPreview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(redditPost.getUrl()));
                        startActivity(intent);
                    }
                });
            } else {
                postPreview.setVisibility(View.GONE);
                postText.setText(redditPost.getSelfText());
            }
        } else {
            postText.setVisibility(View.GONE);
            ImageHelper.getInstance(getActivity().getApplicationContext()).setImage(postPreview, Html.fromHtml(redditPost.getPreviewUrl()).toString());
            if (!redditPost.isVideo() && !redditPost.getEmbedType().equals("video")) {
                videoPlayImageView.setVisibility(View.GONE);
            } else {
                videoPlayImageView.setVisibility(View.VISIBLE);
                postPreview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (redditPost.isVideo()) {
                            Intent videoIntent = new Intent(getActivity(), VideoPlayerActivity.class);
                            videoIntent.putExtra("url", redditPost.getRedditVideoUrl());
                            startActivity(videoIntent);
                        } else {
                            Intent videoIntent = new Intent(Intent.ACTION_VIEW);
                            videoIntent.setData(Uri.parse(redditPost.getUrl()));
                            startActivity(videoIntent);
                        }
                    }
                });
            }
        }
        postView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CommentsActivity.class);
                intent.putExtra("redditPost", redditPost);
                startActivity(intent);
            }
        });
        return layoutView;
    }
}
