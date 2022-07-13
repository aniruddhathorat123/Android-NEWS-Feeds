package com.aniruddha.news_feeds;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Data class for RSS feeds, used to save feeds info in savedInstance when orientation changed.
 * @Deprecated use @{RssEntity} instead of this.
 */
@Deprecated
public class RssData implements Parcelable {
    private final String image;
    private final String title;
    private final String sourceLink;

    public RssData(String title, String image, String sourceLink) {
        this.image = image;
        this.title = title;
        this.sourceLink = sourceLink;
    }

    protected RssData(Parcel in) {
        image = in.readString();
        title = in.readString();
        sourceLink =in.readString();
    }

    /**
     * Create object from parcel
     */
    public static final Creator<RssData> CREATOR = new Creator<RssData>() {
        @Override
        public RssData createFromParcel(Parcel in) {
            return new RssData(in);
        }

        @Override
        public RssData[] newArray(int size) {
            return new RssData[size];
        }
    };

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getSourceLink() {
        return sourceLink;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(image);
        parcel.writeString(title);
        parcel.writeString(sourceLink);
    }
}
