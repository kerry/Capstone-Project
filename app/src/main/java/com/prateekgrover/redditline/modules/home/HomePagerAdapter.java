package com.prateekgrover.redditline.modules.home;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.prateekgrover.redditline.models.RedditPost;

import java.util.List;

public class HomePagerAdapter extends FragmentStatePagerAdapter {
    private List<RedditPost> mRedditPosts;
    private RedditPostsRefreshListener mListener;

    interface RedditPostsRefreshListener {
        void getItemCalledAtPosition(int position);
    }

    public HomePagerAdapter(FragmentManager fm, RedditPostsRefreshListener listener) {
        super(fm);
        mListener = listener;
    }

    public void updateData(List<RedditPost> redditPosts) {
        mRedditPosts = redditPosts;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        mListener.getItemCalledAtPosition(position);
        return RedditPostFragment.newInstance(mRedditPosts.get(position));
    }

    @Override
    public int getCount() {
        return mRedditPosts == null ? 0 : mRedditPosts.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }
}
