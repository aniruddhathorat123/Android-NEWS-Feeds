package com.aniruddha.news_feeds;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * main database entry class for News database
 * Reason of updating DB version:
 * - increment to 2: Added foreign key constraint in @{News} from @{RssEntity}.
 */
@Database(entities = {News.class, RssEntity.class}, version = DatabaseConstants.NEWS_DB_VERSION)
public abstract class NewsDatabase extends RoomDatabase {
    public abstract NewsDao newsDao();
    private static NewsDatabase instance;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(DatabaseConstants.NEWS_DB_NUM_OF_THREADS);

    public static synchronized NewsDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NewsDatabase.class, DatabaseConstants.NEWS_DB_NAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(initiateNewsDB)
                    .build();
        }
        return instance;
    }

    private static final RoomDatabase.Callback initiateNewsDB = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new InitialiseDBAsyncTask(instance).execute();
        }
    };

    // Initiate the DB with initial RSS data.
    private static class InitialiseDBAsyncTask extends AsyncTask<Void, Void, Void> {
        private final NewsDao newsDao;

        private InitialiseDBAsyncTask(NewsDatabase db) {
            newsDao = db.newsDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            RssEntity toiEntity = new RssEntity();
            toiEntity.rssId = 1;
            toiEntity.rssName = DatabaseConstants.NEWS_DB_TOI_NAME;
            toiEntity.rssImage = DatabaseConstants.NEWS_DB_TOI_IMAGE;
            toiEntity.rssSource = DatabaseConstants.NEWS_DB_TOI_SOURCE_LINK;
            toiEntity.isRssAvail = true;
            newsDao.insertRss(toiEntity);
            return null;
        }
    }
}
