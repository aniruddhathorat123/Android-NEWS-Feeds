package com.aniruddha.news_feeds;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Fragment responsible to show all available news once user click on the NEWS RSS feed.
 */
public class NewsFragment extends Fragment {
    public static final String NEWS_WEB_VIEW_URL_KEY = "news_web_view_key";

    private RecyclerView newsRecyclerView;
    private NewsListAdapter newsListAdapter;
    private NewsViewModel newsViewModel;
    private RssEntity rssData;
    private FragmentActivity parentActivity;

    private final Observer<List<News>> newsObserver = new Observer<List<News>>() {
        @Override
        public void onChanged(List<News> news) {
            if (newsListAdapter != null) {
                newsListAdapter.setNewsData(news);
                newsListAdapter.notifyDataSetChanged();
            }
        }
    };

    private final NewsListAdapter.NewsClickListener newsListener = new NewsListAdapter.NewsClickListener() {
        @Override
        public void onNewsSelected(@Nullable String url) {
            if (!TextUtils.isEmpty(url)) {
                Bundle urlData = new Bundle();
                urlData.putString(NEWS_WEB_VIEW_URL_KEY, url);
                parentActivity.getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .add(R.id.news_web_view_container, NewsWebViewFragment.class, urlData)
                        .commit();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.rssData = requireArguments().getParcelable(MainActivity.NEWS_DATA_KEY);
        if (rssData != null) {
            newsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentActivity = getActivity();
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (rssData != null) {
            newsRecyclerView = view.findViewById(R.id.news_list);
            setupNewsAdapter();
            setupNewsLiveData();
            setupActionBar(rssData.rssName);
        }
    }

    private void setupNewsAdapter() {
        newsListAdapter = new NewsListAdapter(parentActivity);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        newsRecyclerView.setAdapter(newsListAdapter);
        newsListAdapter.setNewsClickListener(newsListener);
    }

    private void setupNewsLiveData() {
        newsViewModel.getAllNews(rssData.rssId).observe(getViewLifecycleOwner(), newsObserver);
    }

    private void setupActionBar(String subtitle) {
        ActionBar actionBar = ((AppCompatActivity)parentActivity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setSubtitle(subtitle);
        }
    }
}