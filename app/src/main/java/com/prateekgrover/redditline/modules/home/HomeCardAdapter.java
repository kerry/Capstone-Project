package com.prateekgrover.redditline.modules.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.prateekgrover.redditline.R;
import com.prateekgrover.redditline.models.RedditPost;

import java.util.ArrayList;
import java.util.List;

public class HomeCardAdapter extends RecyclerView.Adapter<HomeCardViewHolder> {

    private List<RedditPost> mRedditPosts;
    private LayoutInflater inflater;

    @Override
    public HomeCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        return new HomeCardViewHolder(inflater.inflate(viewType, null, false));
    }

    public void updateData(List<RedditPost> redditPosts) {
        System.out.println("TT: updating adapeter data " + redditPosts.size());
        mRedditPosts = redditPosts;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(HomeCardViewHolder holder, int position) {
        holder.bindView(mRedditPosts.get(position));
    }

    @Override
    public int getItemCount() {
        return mRedditPosts != null ? mRedditPosts.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 == 0 ? R.layout.item_layout_top : R.layout.item_layout_down;
    }
}
