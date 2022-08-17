package com.aniruddha.news_feeds;

import android.content.Context;
import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Implementation of AsyncTask used to download XML feed from specified RSS.
 */
public class DownloadRssXmlTask extends AsyncTask<String, Void, Object> {
    public static final String DOWNLOAD_XML_TASK_CONNECTION_ERROR = "connection_error";
    public static final String DOWNLOAD_XML_TASK_XML_PARSING_ERROR = "xml_error";
    private final Context context;
    private final RssEntity data;

    public DownloadRssXmlTask(Context context, RssEntity data) {
        this.context = context;
        this.data = data;
    }

    @Override
    protected Object doInBackground(String... strings) {
        try {
            return loadXmlFromNetwork(data.rssSource);
        } catch (IOException e) {
            return DOWNLOAD_XML_TASK_CONNECTION_ERROR;
        } catch (XmlPullParserException e) {
            return DOWNLOAD_XML_TASK_XML_PARSING_ERROR;
        }
    }

    @Override
    protected void onPostExecute(Object result) {
        if (result instanceof Collection) {
            List<?> list = new ArrayList<>((Collection<?>)result);
            NewsDatabase db = NewsDatabase.getInstance(context.getApplicationContext());
            NewsDao dao = db.newsDao();
            NewsDatabase.databaseWriteExecutor.execute( () -> {
                dao.deleteAllNews(data.rssId);
                for (int i = 0; i< list.size(); i++) {
                    dao.insertNews((News) list.get(i));
                }
            });
        }
        if (result instanceof String) {
            System.out.println("##### error in parsing");
        }
        /*setContentView(R.layout.main);
        // Displays the HTML string in the UI via a WebView
        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadData(result, "text/html", null);*/
    }

    public boolean isValidRssSource() {
        try {
            return !loadXmlFromNetwork(data.rssSource).isEmpty();
        } catch (IOException | XmlPullParserException e) {
            return false;
        }
    }

    /**
     * Uploads XML from stackoverflow.com, parses it, and combines it with
     * HTML markup. Returns HTML string.
     */
    private List<News> loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        // Instantiate the parser
        RssFeedsXmlParser rssParser = new RssFeedsXmlParser();

        try {
            stream = downloadUrl(urlString);
            return rssParser.parse(stream, data.rssId);
        } finally {
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
            if (stream != null) {
                stream.close();
            }
        }
    }

    /**
     * Given a string representation of a URL, sets up a connection and gets
     * an input stream.
     */
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }
}
