package com.prateekgrover.redditline.modules.comments;

import android.text.Html;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prateekgrover.redditline.R;
import com.prateekgrover.redditline.models.RedditPost;
import com.prateekgrover.redditline.utils.ImageHelper;
import com.prateekgrover.redditline.utils.Utils;

public class RedditPostViewHolder extends RecyclerView.ViewHolder {
    private TextView postTitle;
    private TextView subredditName;
    private TextView createdAt;
    private TextView postBody;
    private ImageView postPreview;
    private FrameLayout videoPlayImage;
    private RedditPostActionListener mActionListener;

    public RedditPostViewHolder(@NonNull View itemView, RedditPostActionListener actionListener) {
        super(itemView);
        postTitle = itemView.findViewById(R.id.post_title);
        subredditName = itemView.findViewById(R.id.subreddit_name);
        createdAt = itemView.findViewById(R.id.post_time);
        postBody = itemView.findViewById(R.id.post_text);
        postPreview = itemView.findViewById(R.id.post_preview);
        videoPlayImage = itemView.findViewById(R.id.video_play_image);
        mActionListener = actionListener;
    }

    public void bindView(RedditPost redditPost) {
        postTitle.setText(redditPost.getTitle());
        subredditName.setText(redditPost.getSubredditName());
        createdAt.setText(Utils.getDaysAgo(redditPost.getCreatedAt(), postBody.getContext()));
        if (redditPost.getPreviewUrl() == null) {
            if (redditPost.getSelfText().isEmpty()) {
                postPreview.setVisibility(View.VISIBLE);
                postPreview.setImageResource(R.drawable.click);
                videoPlayImage.setVisibility(View.INVISIBLE);
                postBody.setVisibility(View.GONE);
                postPreview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mActionListener.urlClicked(redditPost.getUrl());
                    }
                });
            } else {
                postPreview.setVisibility(View.GONE);
                postBody.setText(redditPost.getSelfText());
            }
        } else {
            postBody.setVisibility(View.GONE);
            ImageHelper.getInstance(postBody.getContext().getApplicationContext()).setImage(postPreview, Html.fromHtml(redditPost.getPreviewUrl()).toString());
            if (!redditPost.isVideo() && !redditPost.getEmbedType().equals("video")) {
                videoPlayImage.setVisibility(View.GONE);
            } else {
                videoPlayImage.setVisibility(View.VISIBLE);
                postPreview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (redditPost.isVideo()) {
                            mActionListener.redditVideoClicked(redditPost.getRedditVideoUrl());
                        } else {
                            mActionListener.urlClicked(redditPost.getUrl());
                        }
                    }
                });
            }
        }
    }
}
