package com.aniruddha.news_feeds;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class NewsDatabase_Impl extends NewsDatabase {
  private volatile NewsDao _newsDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `news_table` (`newsId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `parentId` INTEGER NOT NULL, `news_title` TEXT, `news_description` TEXT, `news_image` TEXT, `news_link` TEXT, `last_update` INTEGER NOT NULL, FOREIGN KEY(`parentId`) REFERENCES `rss_table`(`rssId`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `rss_table` (`rssId` INTEGER NOT NULL, `rss_name` TEXT, `rss_image` TEXT, `rss_source` TEXT, `is_rss_available` INTEGER NOT NULL, PRIMARY KEY(`rssId`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '6b65f21714cd0c2b6ac3d29d6fe0ee1d')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `news_table`");
        _db.execSQL("DROP TABLE IF EXISTS `rss_table`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        _db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsNewsTable = new HashMap<String, TableInfo.Column>(7);
        _columnsNewsTable.put("newsId", new TableInfo.Column("newsId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNewsTable.put("parentId", new TableInfo.Column("parentId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNewsTable.put("news_title", new TableInfo.Column("news_title", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNewsTable.put("news_description", new TableInfo.Column("news_description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNewsTable.put("news_image", new TableInfo.Column("news_image", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNewsTable.put("news_link", new TableInfo.Column("news_link", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNewsTable.put("last_update", new TableInfo.Column("last_update", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysNewsTable = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysNewsTable.add(new TableInfo.ForeignKey("rss_table", "CASCADE", "NO ACTION",Arrays.asList("parentId"), Arrays.asList("rssId")));
        final HashSet<TableInfo.Index> _indicesNewsTable = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoNewsTable = new TableInfo("news_table", _columnsNewsTable, _foreignKeysNewsTable, _indicesNewsTable);
        final TableInfo _existingNewsTable = TableInfo.read(_db, "news_table");
        if (! _infoNewsTable.equals(_existingNewsTable)) {
          return new RoomOpenHelper.ValidationResult(false, "news_table(com.aniruddha.news_feeds.News).\n"
                  + " Expected:\n" + _infoNewsTable + "\n"
                  + " Found:\n" + _existingNewsTable);
        }
        final HashMap<String, TableInfo.Column> _columnsRssTable = new HashMap<String, TableInfo.Column>(5);
        _columnsRssTable.put("rssId", new TableInfo.Column("rssId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRssTable.put("rss_name", new TableInfo.Column("rss_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRssTable.put("rss_image", new TableInfo.Column("rss_image", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRssTable.put("rss_source", new TableInfo.Column("rss_source", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRssTable.put("is_rss_available", new TableInfo.Column("is_rss_available", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRssTable = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRssTable = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRssTable = new TableInfo("rss_table", _columnsRssTable, _foreignKeysRssTable, _indicesRssTable);
        final TableInfo _existingRssTable = TableInfo.read(_db, "rss_table");
        if (! _infoRssTable.equals(_existingRssTable)) {
          return new RoomOpenHelper.ValidationResult(false, "rss_table(com.aniruddha.news_feeds.RssEntity).\n"
                  + " Expected:\n" + _infoRssTable + "\n"
                  + " Found:\n" + _existingRssTable);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "6b65f21714cd0c2b6ac3d29d6fe0ee1d", "041b75d15019cfea6a7bb3f36e271964");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "news_table","rss_table");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `news_table`");
      _db.execSQL("DELETE FROM `rss_table`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(NewsDao.class, NewsDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  public List<Migration> getAutoMigrations(
      @NonNull Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecsMap) {
    return Arrays.asList();
  }

  @Override
  public NewsDao newsDao() {
    if (_newsDao != null) {
      return _newsDao;
    } else {
      synchronized(this) {
        if(_newsDao == null) {
          _newsDao = new NewsDao_Impl(this);
        }
        return _newsDao;
      }
    }
  }
}
