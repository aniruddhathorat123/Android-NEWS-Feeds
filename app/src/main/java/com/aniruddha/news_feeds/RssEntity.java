package com.aniruddha.news_feeds;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = DatabaseConstants.NEWS_DB_RSS_TABLE_NAME)
public class RssEntity implements Parcelable {

    public RssEntity(){}

    @PrimaryKey(autoGenerate = true)
    public int rssId;

    @ColumnInfo(name = DatabaseConstants.NEWS_DB_RSS_NAME_COLUMN)
    public String rssName;

    @ColumnInfo(name = DatabaseConstants.NEWS_DB_RSS_IMAGE_COLUMN)
    public String rssImage;

    @ColumnInfo(name = DatabaseConstants.NEWS_DB_RSS_SOURCE_COLUMN)
    public String rssSource;

    protected RssEntity(Parcel in) {
        rssId = in.readInt();
        rssName = in.readString();
        rssImage = in.readString();
        rssSource = in.readString();
    }

    public static final Creator<RssEntity> CREATOR = new Creator<RssEntity>() {
        @Override
        public RssEntity createFromParcel(Parcel in) {
            return new RssEntity(in);
        }

        @Override
        public RssEntity[] newArray(int size) {
            return new RssEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(rssId);
        parcel.writeString(rssName);
        parcel.writeString(rssImage);
        parcel.writeString(rssSource);
    }
}
