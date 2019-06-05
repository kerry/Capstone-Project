package com.prateekgrover.redditline.modules.comments;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prateekgrover.redditline.R;
import com.prateekgrover.redditline.models.RedditComment;
import com.prateekgrover.redditline.models.RedditPost;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RedditComment> mRedditCommentList;
    private RedditPost mRedditPost;
    private LayoutInflater inflater;
    private RedditPostActionListener mActionListener;

    public CommentsAdapter(RedditPost redditPost, RedditPostActionListener actionListener) {
        mRedditPost = redditPost;
        mActionListener = actionListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        switch (viewType) {
            case R.layout.item_reddit_post:
                return new RedditPostViewHolder(inflater.inflate(viewType, parent, false), mActionListener);
            case R.layout.item_comments_separator:
                return new CommentsSeparatorViewHolder(inflater.inflate(viewType, parent, false));
            default:
                return new CommentsViewHolder(inflater.inflate(viewType, parent, false));
        }
    }

    public void updateData(List<RedditComment> redditCommentList) {
        mRedditCommentList = redditCommentList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            ((RedditPostViewHolder) holder).bindView(mRedditPost);
        } else if (position != 1) {
            ((CommentsViewHolder) holder).bindView(mRedditCommentList.get(position-2));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return R.layout.item_reddit_post;
        } else if (position == 1) {
            return R.layout.item_comments_separator;
        } else {
            return R.layout.item_comment;
        }
    }

    @Override
    public int getItemCount() {
        return mRedditCommentList != null ? mRedditCommentList.size() + 2 : 2;
    }
}
