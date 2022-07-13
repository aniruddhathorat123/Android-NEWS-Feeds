package com.aniruddha.news_feeds;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NewsViewModel extends AndroidViewModel {

    private NewsRepository mRepository;
    private LiveData<List<News>> mAllNews;
    private int parentRssId = -1;

    public NewsViewModel (Application application) {
        super(application);
        //this.parentRssId = parentRssId;
        mRepository = new NewsRepository(application);
    }

    LiveData<List<News>> getAllNews(int parentRssId) {
        if (this.parentRssId != parentRssId) {
            this.parentRssId = parentRssId;
            updateNewsList();
        }
        return mAllNews;
    }

    private void updateNewsList() {
        mAllNews = mRepository.getNews(parentRssId);
    }

    public void addNewRssFeed(RssEntity newRssEntity) {
        mRepository.addNewRssData(newRssEntity);
    }

    /*public void insert(Word word) { mRepository.insert(word); }*/
}
