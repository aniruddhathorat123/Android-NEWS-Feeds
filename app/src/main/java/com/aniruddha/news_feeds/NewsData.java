package com.aniruddha.news_feeds;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Data class for RSS feeds, used to save feeds info in savedInstance when orientation changed.
 * @Deprecated used @{News} instead of this class.
 */
@Deprecated
public class NewsData implements Parcelable {
    private final String imageLink;
    private final String title;
    private final String description;
    private final String link;

    public NewsData(String title, String description, String link, String imageLink) {
        this.imageLink = imageLink;
        this.title = title;
        this.description = description;
        this.link = link;
    }

    protected NewsData(Parcel in) {
        imageLink = in.readString();
        title = in.readString();
        description = in.readString();;
        link = in.readString();;
    }

    /**
     * Create object from parcel
     */
    public static final Creator<NewsData> CREATOR = new Creator<NewsData>() {
        @Override
        public NewsData createFromParcel(Parcel in) {
            return new NewsData(in);
        }

        @Override
        public NewsData[] newArray(int size) {
            return new NewsData[size];
        }
    };

    public String getImageLink() {
        return imageLink;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(link);
        parcel.writeString(imageLink);
    }
}
