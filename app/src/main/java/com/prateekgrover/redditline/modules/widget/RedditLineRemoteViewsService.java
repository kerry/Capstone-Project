package com.prateekgrover.redditline.modules.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class RedditLineRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        System.out.println("widget: getting new factory");
        return new RedditLineWidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
