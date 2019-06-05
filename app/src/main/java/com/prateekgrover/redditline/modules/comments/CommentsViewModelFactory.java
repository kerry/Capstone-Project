package com.prateekgrover.redditline.modules.comments;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.prateekgrover.redditline.models.RedditPost;

public class CommentsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private RedditPost mRedditPost;

    public CommentsViewModelFactory(RedditPost redditPost) {
        mRedditPost = redditPost;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) (new CommentsViewModel(mRedditPost));
    }
}
