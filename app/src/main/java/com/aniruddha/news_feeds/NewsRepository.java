package com.aniruddha.news_feeds;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

/**
 *
 */
public class NewsRepository {
    private final NewsDao newsDao;
    private LiveData<List<News>> allNews;

    NewsRepository(Application application) {
        NewsDatabase db = NewsDatabase.getInstance(application);
        newsDao = db.newsDao();
    }

    public LiveData<List<News>> getNews(int parentRssId) {
        allNews = newsDao.getNews(parentRssId);
        return allNews;
    }

    public void addNewRssData(RssEntity newRss) {
        newsDao.insertRss(newRss);
    }
}
