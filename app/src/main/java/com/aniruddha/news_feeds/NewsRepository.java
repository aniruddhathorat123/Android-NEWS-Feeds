package com.aniruddha.news_feeds;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;

/**
 *
 */
public class NewsRepository {
    private final NewsDao newsDao;
    private final Executor executor;
    private LiveData<List<News>> allNews;
    private LiveData<List<RssEntity>> allRss;

    NewsRepository(Application application, Executor executor) {
        NewsDatabase db = NewsDatabase.getInstance(application);
        this.executor = executor;
        newsDao = db.newsDao();
    }

    public LiveData<List<News>> getNews(int parentRssId) {
        allNews = newsDao.getNews(parentRssId);
        return allNews;
    }

    public void addNewRssData(RssEntity newRss) {
        newsDao.insertRss(newRss);
    }

    public LiveData<List<RssEntity>> getAvailableRss() {
        allRss = newsDao.getAvailableRss();
        return allRss;
    }

    public void syncRss(RssEntity rssEntity) {
        newsDao.syncRss(rssEntity);
    }

    public void deleteRss(RssEntity rssEntity) {
        newsDao.deleteRss(rssEntity.rssId, rssEntity.rssName);
    }

    /*public void getRss(final BackgroundThreadRepositoryCallback<RssEntity> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<RssEntity> entities = newsDao.getAllRss1();
                callback.onCompleteBackgroundTask(entities);
            }
        });
    }*/
}


/**
 * Used to get the success or failure result of the background operations.
 * @param <T>
 */
abstract class Result<T> {
    private Result() {}

    public static final class Success<T> extends Result<T> {
        public List<T> data;

        public Success(List<T> data) {
            this.data = data;
        }
    }

    public static final class Error<T> extends Result<T> {
        public Exception exception;

        public Error(Exception exception) {
            this.exception = exception;
        }
    }
}

/**
 * Listener interface which execute `onCompleteBackgroundTask` when backgroun thread
 * operation is finished.
 * @param <T>
 */
interface BackgroundThreadRepositoryCallback<T> {
    void onCompleteBackgroundTask(Result<T> result);
}