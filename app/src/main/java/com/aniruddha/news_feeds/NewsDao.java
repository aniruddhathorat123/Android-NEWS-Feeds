package com.aniruddha.news_feeds;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 *
 */
@Dao
public interface NewsDao {
    // RSS fetching queries
    @Query("Select * from rss_table")
    List<RssEntity> getAllRss();

    @Insert
    void insertRss(RssEntity rssEntity);

    @Update
    void updateRss(RssEntity rssEntity);

    @Delete
    void deleteRss(RssEntity rssEntity);

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
