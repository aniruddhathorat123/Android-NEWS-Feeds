package com.aniruddha.news_feeds;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class NewsDao_Impl implements NewsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RssEntity> __insertionAdapterOfRssEntity;

  private final EntityInsertionAdapter<RssEntity> __insertionAdapterOfRssEntity_1;

  private final EntityInsertionAdapter<News> __insertionAdapterOfNews;

  private final EntityDeletionOrUpdateAdapter<News> __deletionAdapterOfNews;

  private final EntityDeletionOrUpdateAdapter<RssEntity> __updateAdapterOfRssEntity;

  private final EntityDeletionOrUpdateAdapter<News> __updateAdapterOfNews;

  private final SharedSQLiteStatement __preparedStmtOfDeleteRss;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllNews;

  public NewsDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRssEntity = new EntityInsertionAdapter<RssEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `rss_table` (`rssId`,`rss_name`,`rss_image`,`rss_source`,`is_rss_available`) VALUES (?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, RssEntity value) {
        stmt.bindLong(1, value.rssId);
        if (value.rssName == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.rssName);
        }
        if (value.rssImage == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.rssImage);
        }
        if (value.rssSource == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.rssSource);
        }
        final int _tmp = value.isRssAvail ? 1 : 0;
        stmt.bindLong(5, _tmp);
      }
    };
    this.__insertionAdapterOfRssEntity_1 = new EntityInsertionAdapter<RssEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `rss_table` (`rssId`,`rss_name`,`rss_image`,`rss_source`,`is_rss_available`) VALUES (?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, RssEntity value) {
        stmt.bindLong(1, value.rssId);
        if (value.rssName == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.rssName);
        }
        if (value.rssImage == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.rssImage);
        }
        if (value.rssSource == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.rssSource);
        }
        final int _tmp = value.isRssAvail ? 1 : 0;
        stmt.bindLong(5, _tmp);
      }
    };
    this.__insertionAdapterOfNews = new EntityInsertionAdapter<News>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `news_table` (`newsId`,`parentId`,`news_title`,`news_description`,`news_image`,`news_link`,`last_update`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, News value) {
        stmt.bindLong(1, value.newsId);
        stmt.bindLong(2, value.parentId);
        if (value.newsTitle == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.newsTitle);
        }
        if (value.newsDescription == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.newsDescription);
        }
        if (value.newsImage == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.newsImage);
        }
        if (value.newsLink == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.newsLink);
        }
        stmt.bindLong(7, value.lastUpdate);
      }
    };
    this.__deletionAdapterOfNews = new EntityDeletionOrUpdateAdapter<News>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `news_table` WHERE `newsId` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, News value) {
        stmt.bindLong(1, value.newsId);
      }
    };
    this.__updateAdapterOfRssEntity = new EntityDeletionOrUpdateAdapter<RssEntity>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `rss_table` SET `rssId` = ?,`rss_name` = ?,`rss_image` = ?,`rss_source` = ?,`is_rss_available` = ? WHERE `rssId` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, RssEntity value) {
        stmt.bindLong(1, value.rssId);
        if (value.rssName == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.rssName);
        }
        if (value.rssImage == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.rssImage);
        }
        if (value.rssSource == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.rssSource);
        }
        final int _tmp = value.isRssAvail ? 1 : 0;
        stmt.bindLong(5, _tmp);
        stmt.bindLong(6, value.rssId);
      }
    };
    this.__updateAdapterOfNews = new EntityDeletionOrUpdateAdapter<News>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `news_table` SET `newsId` = ?,`parentId` = ?,`news_title` = ?,`news_description` = ?,`news_image` = ?,`news_link` = ?,`last_update` = ? WHERE `newsId` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, News value) {
        stmt.bindLong(1, value.newsId);
        stmt.bindLong(2, value.parentId);
        if (value.newsTitle == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.newsTitle);
        }
        if (value.newsDescription == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.newsDescription);
        }
        if (value.newsImage == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.newsImage);
        }
        if (value.newsLink == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.newsLink);
        }
        stmt.bindLong(7, value.lastUpdate);
        stmt.bindLong(8, value.newsId);
      }
    };
    this.__preparedStmtOfDeleteRss = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from rss_table where rssId = ? and rss_name = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllNews = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from news_table where parentId IN (?)";
        return _query;
      }
    };
  }

  @Override
  public void insertRss(final RssEntity rssEntity) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfRssEntity.insert(rssEntity);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void syncRss(final RssEntity rssEntity) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfRssEntity_1.insert(rssEntity);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertNews(final News news) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfNews.insert(news);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertAllNews(final List<News> newsList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfNews.insert(newsList);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteNews(final News news) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfNews.handle(news);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateRss(final RssEntity rssEntity) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfRssEntity.handle(rssEntity);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateNews(final News news) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfNews.handle(news);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteRss(final int rssId, final String rssName) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteRss.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, rssId);
    _argIndex = 2;
    if (rssName == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, rssName);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteRss.release(_stmt);
    }
  }

  @Override
  public void deleteAllNews(final int parentId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllNews.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, parentId);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAllNews.release(_stmt);
    }
  }

  @Override
  public RssEntity getAllRss() {
    final String _sql = "Select * from rss_table";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfRssId = CursorUtil.getColumnIndexOrThrow(_cursor, "rssId");
      final int _cursorIndexOfRssName = CursorUtil.getColumnIndexOrThrow(_cursor, "rss_name");
      final int _cursorIndexOfRssImage = CursorUtil.getColumnIndexOrThrow(_cursor, "rss_image");
      final int _cursorIndexOfRssSource = CursorUtil.getColumnIndexOrThrow(_cursor, "rss_source");
      final int _cursorIndexOfIsRssAvail = CursorUtil.getColumnIndexOrThrow(_cursor, "is_rss_available");
      final RssEntity _result;
      if(_cursor.moveToFirst()) {
        _result = new RssEntity();
        _result.rssId = _cursor.getInt(_cursorIndexOfRssId);
        if (_cursor.isNull(_cursorIndexOfRssName)) {
          _result.rssName = null;
        } else {
          _result.rssName = _cursor.getString(_cursorIndexOfRssName);
        }
        if (_cursor.isNull(_cursorIndexOfRssImage)) {
          _result.rssImage = null;
        } else {
          _result.rssImage = _cursor.getString(_cursorIndexOfRssImage);
        }
        if (_cursor.isNull(_cursorIndexOfRssSource)) {
          _result.rssSource = null;
        } else {
          _result.rssSource = _cursor.getString(_cursorIndexOfRssSource);
        }
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsRssAvail);
        _result.isRssAvail = _tmp != 0;
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<List<RssEntity>> getAvailableRss() {
    final String _sql = "Select * from rss_table where is_rss_available = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"rss_table"}, false, new Callable<List<RssEntity>>() {
      @Override
      public List<RssEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfRssId = CursorUtil.getColumnIndexOrThrow(_cursor, "rssId");
          final int _cursorIndexOfRssName = CursorUtil.getColumnIndexOrThrow(_cursor, "rss_name");
          final int _cursorIndexOfRssImage = CursorUtil.getColumnIndexOrThrow(_cursor, "rss_image");
          final int _cursorIndexOfRssSource = CursorUtil.getColumnIndexOrThrow(_cursor, "rss_source");
          final int _cursorIndexOfIsRssAvail = CursorUtil.getColumnIndexOrThrow(_cursor, "is_rss_available");
          final List<RssEntity> _result = new ArrayList<RssEntity>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final RssEntity _item;
            _item = new RssEntity();
            _item.rssId = _cursor.getInt(_cursorIndexOfRssId);
            if (_cursor.isNull(_cursorIndexOfRssName)) {
              _item.rssName = null;
            } else {
              _item.rssName = _cursor.getString(_cursorIndexOfRssName);
            }
            if (_cursor.isNull(_cursorIndexOfRssImage)) {
              _item.rssImage = null;
            } else {
              _item.rssImage = _cursor.getString(_cursorIndexOfRssImage);
            }
            if (_cursor.isNull(_cursorIndexOfRssSource)) {
              _item.rssSource = null;
            } else {
              _item.rssSource = _cursor.getString(_cursorIndexOfRssSource);
            }
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsRssAvail);
            _item.isRssAvail = _tmp != 0;
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public List<News> getAllNews() {
    final String _sql = "select * from news_table";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfNewsId = CursorUtil.getColumnIndexOrThrow(_cursor, "newsId");
      final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(_cursor, "parentId");
      final int _cursorIndexOfNewsTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "news_title");
      final int _cursorIndexOfNewsDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "news_description");
      final int _cursorIndexOfNewsImage = CursorUtil.getColumnIndexOrThrow(_cursor, "news_image");
      final int _cursorIndexOfNewsLink = CursorUtil.getColumnIndexOrThrow(_cursor, "news_link");
      final int _cursorIndexOfLastUpdate = CursorUtil.getColumnIndexOrThrow(_cursor, "last_update");
      final List<News> _result = new ArrayList<News>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final News _item;
        final int _tmpParentId;
        _tmpParentId = _cursor.getInt(_cursorIndexOfParentId);
        final String _tmpNewsTitle;
        if (_cursor.isNull(_cursorIndexOfNewsTitle)) {
          _tmpNewsTitle = null;
        } else {
          _tmpNewsTitle = _cursor.getString(_cursorIndexOfNewsTitle);
        }
        final String _tmpNewsDescription;
        if (_cursor.isNull(_cursorIndexOfNewsDescription)) {
          _tmpNewsDescription = null;
        } else {
          _tmpNewsDescription = _cursor.getString(_cursorIndexOfNewsDescription);
        }
        final String _tmpNewsImage;
        if (_cursor.isNull(_cursorIndexOfNewsImage)) {
          _tmpNewsImage = null;
        } else {
          _tmpNewsImage = _cursor.getString(_cursorIndexOfNewsImage);
        }
        final String _tmpNewsLink;
        if (_cursor.isNull(_cursorIndexOfNewsLink)) {
          _tmpNewsLink = null;
        } else {
          _tmpNewsLink = _cursor.getString(_cursorIndexOfNewsLink);
        }
        final long _tmpLastUpdate;
        _tmpLastUpdate = _cursor.getLong(_cursorIndexOfLastUpdate);
        _item = new News(_tmpParentId,_tmpNewsTitle,_tmpNewsDescription,_tmpNewsImage,_tmpNewsLink,_tmpLastUpdate);
        _item.newsId = _cursor.getInt(_cursorIndexOfNewsId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<List<News>> getNews(final int id) {
    final String _sql = "select * from news_table where parentId IN (?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return __db.getInvalidationTracker().createLiveData(new String[]{"news_table"}, false, new Callable<List<News>>() {
      @Override
      public List<News> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfNewsId = CursorUtil.getColumnIndexOrThrow(_cursor, "newsId");
          final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(_cursor, "parentId");
          final int _cursorIndexOfNewsTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "news_title");
          final int _cursorIndexOfNewsDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "news_description");
          final int _cursorIndexOfNewsImage = CursorUtil.getColumnIndexOrThrow(_cursor, "news_image");
          final int _cursorIndexOfNewsLink = CursorUtil.getColumnIndexOrThrow(_cursor, "news_link");
          final int _cursorIndexOfLastUpdate = CursorUtil.getColumnIndexOrThrow(_cursor, "last_update");
          final List<News> _result = new ArrayList<News>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final News _item;
            final int _tmpParentId;
            _tmpParentId = _cursor.getInt(_cursorIndexOfParentId);
            final String _tmpNewsTitle;
            if (_cursor.isNull(_cursorIndexOfNewsTitle)) {
              _tmpNewsTitle = null;
            } else {
              _tmpNewsTitle = _cursor.getString(_cursorIndexOfNewsTitle);
            }
            final String _tmpNewsDescription;
            if (_cursor.isNull(_cursorIndexOfNewsDescription)) {
              _tmpNewsDescription = null;
            } else {
              _tmpNewsDescription = _cursor.getString(_cursorIndexOfNewsDescription);
            }
            final String _tmpNewsImage;
            if (_cursor.isNull(_cursorIndexOfNewsImage)) {
              _tmpNewsImage = null;
            } else {
              _tmpNewsImage = _cursor.getString(_cursorIndexOfNewsImage);
            }
            final String _tmpNewsLink;
            if (_cursor.isNull(_cursorIndexOfNewsLink)) {
              _tmpNewsLink = null;
            } else {
              _tmpNewsLink = _cursor.getString(_cursorIndexOfNewsLink);
            }
            final long _tmpLastUpdate;
            _tmpLastUpdate = _cursor.getLong(_cursorIndexOfLastUpdate);
            _item = new News(_tmpParentId,_tmpNewsTitle,_tmpNewsDescription,_tmpNewsImage,_tmpNewsLink,_tmpLastUpdate);
            _item.newsId = _cursor.getInt(_cursorIndexOfNewsId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
