package com.aniruddha.news_feeds;

import android.text.TextUtils;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 */
public class RssFeedsXmlParser {
    // We don't use namespaces
    private static final String ns = null;
    private int parentId;

    public List<News> parse(InputStream in, int parentId) throws XmlPullParserException, IOException {
        this.parentId = parentId;
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List<News> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<News> entries = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, ns, "rss");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the item tag
            if (name.equals("channel")) {
                parser.nextTag();
                name = parser.getName();
                while(!name.equals("item")) {
                    skip(parser);
                    parser.nextTag();
                    name = parser.getName();
                }
            }
            if (name.equals("item")) {
                entries.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    /**
     *
     */
    public static class Entry {
        public final String title;
        public final String link;
        public final String description;
        public final String imageLink;

        private Entry(String title, String summary, String link, String imageLink) {
            this.title = title;
            this.description = summary;
            this.link = link;
            this.imageLink = imageLink;
        }
    }

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
    private News readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "item");
        String title = null;
        String description = null;
        String link = null;
        String photoLink = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case "title":
                    title = readTitle(parser);
                    break;
                case "description":
                    String data = readDescription(parser);
                    if (!TextUtils.isEmpty(data)) {
                        description = getDescriptionText(data);
                        photoLink = getImageUrl(data);
                        System.out.println("##### photolink : " + photoLink);
                    }
                    break;
                case "link":
                    link = readLink(parser);
                    break;
                default:
                    skip(parser);
            }
        }
        return new News(parentId, title, description, photoLink, link, Calendar.getInstance().getTimeInMillis());
    }

    // Processes title tags in the feed.
    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return title;
    }

    // Processes link tags in the feed.
    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        String link = "";
        parser.require(XmlPullParser.START_TAG, ns, "link");
        link = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "link");
        return link;
    }

    // Processes summary tags in the feed.
    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "description");
        String summary = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "description");
        return summary;
    }

    private String getImageUrl(String data) {
        final String TAG = "src=\"";
        final int TAG_LENGTH = TAG.length();
        int startIndex = 0, endIndex = 0;
        String nextSubstring = "";
        startIndex = data.indexOf(TAG);
        nextSubstring = data.substring(startIndex + TAG_LENGTH);
        if (startIndex != -1) {
            endIndex = nextSubstring.indexOf("\"");
            if (endIndex != -1) {
                return nextSubstring.substring(0, endIndex).trim();
            }
        }
        return "";
    }

    private String getDescriptionText(String data) {
        final String TAG = "</a>";
        final int TAG_LENGTH = TAG.length();
        int startIndex = 0;
        startIndex = data.indexOf(TAG);
        if (startIndex != -1) {
            return data.substring(startIndex + TAG_LENGTH);
        }
        return "";
    }

    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    // skip the unwanted tags.
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
