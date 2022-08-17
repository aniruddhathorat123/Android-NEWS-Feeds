package com.aniruddha.news_feeds;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 *
 */
@Dao
public interface NewsDao {
    @Query("Select * from rss_table")
    RssEntity getAllRss();

    // Available RSS fetching queries
    @Query("Select * from rss_table where is_rss_available = 1")
    LiveData<List<RssEntity>> getAvailableRss();

    @Insert
    void insertRss(RssEntity rssEntity);

    @Update
    void updateRss(RssEntity rssEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void syncRss(RssEntity rssEntity);

    @Query("Delete from rss_table where rssId = :rssId and rss_name = :rssName")
    void deleteRss(int rssId, String rssName);

    // NEWS fetching queries
    @Query("select * from news_table")
    List<News> getAllNews();

    @Query("select * from news_table where parentId IN (:id)")
    LiveData<List<News>> getNews(int id);

    @Insert
    void insertNews(News news);

    @Insert
    void insertAllNews(List<News> newsList);

    @Update
    void updateNews(News news);

    @Delete
    void deleteNews(News news);

    @Query("Delete from news_table where parentId IN (:parentId)")
    void deleteAllNews(int parentId);
}
