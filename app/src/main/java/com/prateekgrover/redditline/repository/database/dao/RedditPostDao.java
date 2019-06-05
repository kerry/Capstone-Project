package com.prateekgrover.redditline.repository.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.prateekgrover.redditline.models.RedditPost;

import java.util.List;

@Dao
public interface RedditPostDao {
    @Query("SELECT * FROM reddit_post ORDER BY created_utc DESC LIMIT 5")
    List<RedditPost> loadPosts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRedditPosts(List<RedditPost> redditPosts);
}
