package com.prateekgrover.redditline.repository.database;

import android.content.Context;
import android.graphics.Movie;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.prateekgrover.redditline.models.RedditPost;
import com.prateekgrover.redditline.repository.database.dao.RedditPostDao;

@Database(entities = {RedditPost.class}, version = 1, exportSchema = false)
public abstract class DatabaseManager extends RoomDatabase {

    private static final String LOG_TAG = DatabaseManager.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "redditline";
    private static DatabaseManager sInstance;

    public static DatabaseManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized(LOCK) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            DatabaseManager.class, DatabaseManager.DATABASE_NAME).build();
                }
            }
        }
        return sInstance;
    }

    public abstract RedditPostDao redditPostDao();
}