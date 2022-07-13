package com.aniruddha.news_feeds;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Objects;

/**
 *
 */
public class NewsWebViewFragment extends Fragment {
    private WebView newsWebView;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View currentView = inflater.inflate(R.layout.fragment_news_webview, container, false);
        newsWebView = currentView.findViewById(R.id.news_web_view);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.news_webView_progress_dialog_message));
        //newsWebView.getSettings().setJavaScriptEnabled(true);
        String url = requireArguments().getString(NewsFragment.NEWS_WEB_VIEW_URL_KEY);
        if (!TextUtils.isEmpty(url)) {
            setupWebView(url);
        }
        return currentView;
    }

    private void setupWebView(String newsUrl) {
        newsWebView.setWebViewClient(new NewsWebViewClient());
        newsWebView.loadUrl(newsUrl);
    }

    private class NewsWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String request) {
            view.loadUrl(request);
            if(!progressDialog.isShowing())
                progressDialog.show();
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if(progressDialog.isShowing())
            {progressDialog.dismiss();}
        }
    }
}