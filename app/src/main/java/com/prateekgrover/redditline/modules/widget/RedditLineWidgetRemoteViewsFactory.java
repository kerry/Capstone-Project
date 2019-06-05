package com.prateekgrover.redditline.modules.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.prateekgrover.redditline.R;
import com.prateekgrover.redditline.models.RedditPost;
import com.prateekgrover.redditline.repository.database.DatabaseManager;

import java.util.List;

public class RedditLineWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<RedditPost> redditPosts;

    public RedditLineWidgetRemoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
        System.out.println("widget: got intent");
        if (intent != null && intent.hasExtra("reddit_post_list")) {
            System.out.println("widget: intent has required data");
            redditPosts = intent.getParcelableArrayListExtra("reddit_post_list");
        }
    }

    @Override
    public void onCreate() {
        System.out.println("widget: factory service on create called");
    }

    @Override
    public void onDataSetChanged() {
        System.out.println("widget: data set changed");
        redditPosts = DatabaseManager.getInstance(mContext).redditPostDao().loadPosts();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        int count = redditPosts != null ? redditPosts.size() : 0;
        System.out.println("widget: data count " + count);
        return count;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RedditPost redditPost = redditPosts.get(position);
        RemoteViews remoteView = new RemoteViews(mContext.getPackageName(),
                R.layout.redditline_widget_list_item);
        remoteView.setTextViewText(R.id.post_title, redditPost.getTitle());

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra("targetActivity", "comments");
        fillInIntent.putExtra("widget_intent", true);
        fillInIntent.putExtra("redditPost", redditPost);
        remoteView.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);

        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
