package com.prateekgrover.redditline.modules.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import androidx.core.app.TaskStackBuilder;
import androidx.core.content.ContextCompat;

import com.prateekgrover.redditline.R;
import com.prateekgrover.redditline.modules.comments.CommentsActivity;
import com.prateekgrover.redditline.modules.home.HomeActivity;
import com.prateekgrover.redditline.modules.splash.SplashActivity;
import com.prateekgrover.redditline.services.UpdateWidgetService;

/**
 * Implementation of App Widget functionality.
 */
public class RedditLine extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(
                context.getPackageName(),
                R.layout.reddit_line
        );
        Intent titleIntent = new Intent(context, SplashActivity.class);
        PendingIntent titlePendingIntent = PendingIntent.getActivity(context, 0, titleIntent, 0);
        views.setOnClickPendingIntent(R.id.appwidget_text, titlePendingIntent);

        Intent intent = new Intent(context, RedditLineRemoteViewsService.class);
        views.setRemoteAdapter(R.id.widget_listview, intent);

        Intent clickIntentTemplate = new Intent(context, CommentsActivity.class);
        PendingIntent clickPendingIntentTemplate = PendingIntent.getActivity(context, 0,
                clickIntentTemplate, PendingIntent.FLAG_UPDATE_CURRENT);
//        PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
//                .addNextIntentWithParentStack(clickIntentTemplate)
//                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_listview, clickPendingIntentTemplate);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_listview);

        System.out.println("widget: on update called");
            ComponentName thisWidget = new ComponentName(context,
                    RedditLine.class);
            int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

            // Build the intent to call the service
            Intent intent = new Intent(context.getApplicationContext(),
                    UpdateWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

            // Update the widgets via the service
//        context.startService(intent);
            ContextCompat.startForegroundService(context, intent);
//        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        if (action.equals("REDDITLINE_WIDGET_UPDATE")) {
            System.out.println("widget: current thread " + Thread.currentThread());
            // refresh all your widgets
            System.out.println("widget: received notification");
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, RedditLine.class);
            int[] widgetIds = appWidgetManager.getAppWidgetIds(cn);

            RemoteViews views = new RemoteViews(
                    context.getPackageName(),
                    R.layout.reddit_line
            );
            Intent titleIntent = new Intent(context, SplashActivity.class);
            PendingIntent titlePendingIntent = PendingIntent.getActivity(context, 0, titleIntent, 0);
            views.setOnClickPendingIntent(R.id.appwidget_text, titlePendingIntent);

            Intent dataIntent = new Intent(context, RedditLineRemoteViewsService.class);
            System.out.println("widget: intent has reddit_post_list " + intent.hasExtra("reddit_post_list"));
            dataIntent.putParcelableArrayListExtra("reddit_post_list", intent.getParcelableArrayListExtra("reddit_post_list"));
            dataIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds);
            views.setRemoteAdapter(R.id.widget_listview, dataIntent);
            System.out.println("widget: updated with count " + intent.getParcelableArrayListExtra("reddit_post_list").size());

            Intent clickIntentTemplate = new Intent(context, SplashActivity.class);
            PendingIntent clickPendingIntentTemplate = PendingIntent.getActivity(context, 0,
                    clickIntentTemplate, PendingIntent.FLAG_UPDATE_CURRENT);
//        PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
//                .addNextIntentWithParentStack(clickIntentTemplate)
//                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widget_listview, clickPendingIntentTemplate);

            appWidgetManager.updateAppWidget(widgetIds, views);
//            appWidgetManager.notifyAppWidgetViewDataChanged(widgetId, R.id.widget_listview);
//            appWidgetManager.notifyAppWidgetViewDataChanged(widgetIds, R.id.widget_listview);
        }

        super.onReceive(context, intent);
    }
}

