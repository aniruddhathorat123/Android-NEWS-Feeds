package com.aniruddha.news_feeds;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 *
 */
public class NewsFragment extends Fragment {
    public static final String NEWS_WEB_VIEW_URL_KEY = "news_web_view_key";

    private RecyclerView newsRecyclerView;
    private NewsListAdapter newsListAdapter;
    private NewsViewModel newsViewModel;
    private RssEntity rssData;

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
                getActivity().getSupportFragmentManager().beginTransaction()
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
            newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (rssData != null) {
            newsRecyclerView = view.findViewById(R.id.news_list);
            setupNewsAdapter();
            setupNewsLiveData();
        }
    }

    private void setupNewsAdapter() {
        newsListAdapter = new NewsListAdapter(getActivity());
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        newsRecyclerView.setAdapter(newsListAdapter);
        newsRecyclerView.addItemDecoration(dividerItemDecoration);
        newsListAdapter.setNewsClickListener(newsListener);
    }

    private void setupNewsLiveData() {
        newsViewModel.getAllNews(rssData.rssId).observe(getViewLifecycleOwner(), newsObserver);
    }
}