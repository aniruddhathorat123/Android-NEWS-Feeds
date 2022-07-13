package com.aniruddha.news_feeds;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

/**
 *
 */
@Entity(tableName = DatabaseConstants.NEWS_DB_NEWS_TABLE_NAME,
        foreignKeys = @ForeignKey(entity = RssEntity.class,
                parentColumns = "rssId",
                childColumns = "parentId",
                onDelete = CASCADE))
public class News implements Parcelable {
    public News(int parentId, String newsTitle, String newsDescription, String newsImage, String newsLink, long lastUpdate) {
        this.parentId = parentId;
        this.newsTitle = newsTitle;
        this.newsDescription = newsDescription;
        this.newsImage = newsImage;
        this.newsLink = newsLink;
        this.lastUpdate = lastUpdate;
    }

    @PrimaryKey(autoGenerate = true)
    public int newsId;

    public int parentId;

    @ColumnInfo(name = DatabaseConstants.NEWS_DB_NEWS_TITLE_COLUMN)
    public String newsTitle;

    @ColumnInfo(name = DatabaseConstants.NEWS_DB_NEWS_DESCRIPTION_COLUMN)
    public String newsDescription;

    @ColumnInfo(name = DatabaseConstants.NEWS_DB_NEWS_IMAGE_COLUMN)
    public String newsImage;

    @ColumnInfo(name = DatabaseConstants.NEWS_DB_NEWS_LINK_COLUMN)
    public String newsLink;

    @ColumnInfo(name = DatabaseConstants.NEWS_DB_NEWS_LAST_UPDATE_COLUMN)
    public long lastUpdate;

    protected News(Parcel in) {
        parentId = in.readInt();
        newsTitle = in.readString();
        newsDescription = in.readString();
        newsImage = in.readString();
        newsLink = in.readString();
        lastUpdate = in.readLong();
    }

    public static final Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(parentId);
        parcel.writeString(newsTitle);
        parcel.writeString(newsDescription);
        parcel.writeString(newsImage);
        parcel.writeString(newsLink);
        parcel.writeLong(lastUpdate);
    }
}
