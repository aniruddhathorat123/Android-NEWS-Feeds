package com.aniruddha.news_feeds;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The ViewModel class is designed to store and manage UI-related data in a lifecycle conscious way.
 * The ViewModel class allows data to survive configuration changes such as screen rotations.
 */
public class NewsViewModel extends AndroidViewModel {

    private NewsRepository newsRepository;
    private LiveData<List<News>> mAllNews;
    private LiveData<List<RssEntity>> rssEntities;
    private int parentRssId = -1;
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);
    private final NewsDatabase db;
    private final NewsDao dao;

    public NewsViewModel (Application application) {
        super(application);
        //this.parentRssId = parentRssId;
        newsRepository = new NewsRepository(application, executorService);
        db = NewsDatabase.getInstance(application);
        dao = db.newsDao();
    }

    LiveData<List<News>> getAllNews(int parentRssId) {
        if (this.parentRssId != parentRssId) {
            this.parentRssId = parentRssId;
            updateNewsList();
        }
        return mAllNews;
    }

    LiveData<List<RssEntity>> getAllAvailableRss() {
        updateAvailableRssList();
        return rssEntities;
        /*newsRepository.getRss(new BackgroundThreadRepositoryCallback<RssEntity>() {
            @Override
            public void onCompleteBackgroundTask(Result<RssEntity> result) {
                if (result instanceof Result.Success) {
                    List<RssEntity> rssEntities = ((Result.Success<RssEntity>)result ).data;
                    return rssEntities;
                }
            }
        });*/
    }

    private void updateNewsList() {
        mAllNews = newsRepository.getNews(parentRssId);
    }

    public void addNewRssFeed(RssEntity newRssEntity) {
        newsRepository.addNewRssData(newRssEntity);
    }

    public void syncRssFeed(RssEntity rssEntity) {
        newsRepository.syncRss(rssEntity);
    }

    public void deleteRssFeed(RssEntity rssEntity) {
        newsRepository.deleteRss(rssEntity);
    }

    public void updateAvailableRssList() {
        rssEntities = newsRepository.getAvailableRss();
    }

    /*public void insert(Word word) { mRepository.insert(word); }*/
}
