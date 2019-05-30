package com.prateekgrover.redditline.modules.home;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prateekgrover.redditline.R;
import com.prateekgrover.redditline.models.RedditPost;

public class HomeCardViewHolder extends RecyclerView.ViewHolder {

    TextView textView;

    public HomeCardViewHolder(View itemView) {
        super(itemView);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        int margin = itemView.getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
        lp.setMargins(margin, margin, margin, margin);
        itemView.setLayoutParams(lp);
        textView = itemView.findViewById(R.id.item_text);
    }

    public void bindView(RedditPost redditPost) {
        textView.setText(redditPost.getTitle());
    }
}