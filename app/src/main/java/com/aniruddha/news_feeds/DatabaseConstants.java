package com.aniruddha.news_feeds;

/**
 *
 */
public class DatabaseConstants {
    public static final int NEWS_DB_VERSION = 2;
    public static final int NEWS_DB_NUM_OF_THREADS = 4;

    public static final String NEWS_DB_NAME = "news_database";

    public static final String NEWS_DB_NEWS_TABLE_NAME = "news_table";
    public static final String NEWS_DB_NEWS_TITLE_COLUMN = "news_title";
    public static final String NEWS_DB_NEWS_DESCRIPTION_COLUMN = "news_description";
    public static final String NEWS_DB_NEWS_IMAGE_COLUMN = "news_image";
    public static final String NEWS_DB_NEWS_LINK_COLUMN = "news_link";
    public static final String NEWS_DB_NEWS_LAST_UPDATE_COLUMN = "last_update";

    public static final String NEWS_DB_RSS_TABLE_NAME = "rss_table";
    public static final String NEWS_DB_RSS_NAME_COLUMN = "rss_name";
    public static final String NEWS_DB_RSS_IMAGE_COLUMN = "rss_image";
    public static final String NEWS_DB_RSS_SOURCE_COLUMN = "rss_source";
    public static final String NEWS_DB_RSS_IS_AVAIL_COLUMN = "is_rss_available";

    public static final String NEWS_DB_TOI_NAME = "The Times of India";
    public static final String NEWS_DB_TOI_IMAGE = "https://timesofindia.indiatimes.com/photo/507610.cms";
    public static final String NEWS_DB_TOI_SOURCE_LINK = "https://timesofindia.indiatimes.com/rssfeeds/-2128936835.cms";
}

