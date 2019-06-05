package com.prateekgrover.redditline.modules.comments;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.prateekgrover.redditline.R;
import com.prateekgrover.redditline.models.RedditComment;
import com.prateekgrover.redditline.utils.Utils;

public class CommentsViewHolder extends RecyclerView.ViewHolder {
    private View mItemView;
    private TextView authorName;
    private TextView time;
    private TextView commentBody;
    private ConstraintLayout insideView;
    private ViewGroup.MarginLayoutParams insideViewLayoutParams;

    public CommentsViewHolder(@NonNull View itemView) {
        super(itemView);
        mItemView = itemView;
        authorName = itemView.findViewById(R.id.authorName);
        time = itemView.findViewById(R.id.comment_time);
        commentBody = itemView.findViewById(R.id.comment_body);
        insideView = itemView.findViewById(R.id.inside_view);
        insideViewLayoutParams = (ViewGroup.MarginLayoutParams) insideView.getLayoutParams();
    }

    public void bindView(RedditComment redditComment) {
        authorName.setText(redditComment.getAuthor());
        time.setText(Utils.getDaysAgo(redditComment.getCreated(), mItemView.getContext()));
        commentBody.setText(redditComment.getBody());
        insideViewLayoutParams.setMarginStart((redditComment.getLevel() + 1)*(int)mItemView.getContext().getResources().getDimension(R.dimen.comment_start_margin_base));
    }
}
