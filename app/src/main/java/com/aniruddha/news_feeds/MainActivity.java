package com.aniruddha.news_feeds;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String NEWS_DATA_KEY = "news_data_key";

    private RecyclerView rssRecyclerView;
    private RssListAdapter rssAdapter;
    private ProgressBar feedsProgressBar;
    private ExtendedFloatingActionButton addFeedButton;
    //String URL2 = "https://www.indiatoday.in/rss/1206514";
    //String URL1 = "https://timesofindia.indiatimes.com/rssfeeds/-2128936835.cms";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rssRecyclerView = findViewById(R.id.rss_list);
        feedsProgressBar = findViewById(R.id.main_progressBr);
        addFeedButton = findViewById(R.id.add_feed_button);
        addFeedButton.shrink();
        addFeedButton.setOnClickListener(this);
        updateProgressBar(true);

        setupRssList();
        setupRssDataBase();
        setupBackPressEvent();
    }

    // Click listener for Rss item clicked.
    private final RssListAdapter.OnItemClickListener rssClickListener = new RssListAdapter.OnItemClickListener() {
        @Override
        public void onItemClicked(RssEntity rssData) {
            new DownloadRssXmlTask(getApplicationContext(), rssData).execute();
            // initiate the news viewModel.
            //setupNewsViewModel(rssData.rssId);
            instantiateNewsFragment(rssData);
        }
    };

    private void setupRssList() {
        rssRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(getBaseContext(), DividerItemDecoration.VERTICAL);
        rssAdapter = new RssListAdapter(getBaseContext());
        rssRecyclerView.setAdapter(rssAdapter);
        rssRecyclerView.addItemDecoration(dividerItemDecoration);
        rssAdapter.setOnItemClickListener(rssClickListener);
    }

    public List<RssEntity> getDefaultData(@NonNull NewsDao newsDao) throws ExecutionException, InterruptedException {
        Callable<List<RssEntity>> callable = new Callable<List<RssEntity>>() {
            @Override
            public List<RssEntity> call() throws Exception {
                return newsDao.getAllRss();
            }
        };
        Future<List<RssEntity>> future = NewsDatabase.databaseWriteExecutor.submit(callable);
        ListenableFuture 
        CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver<>().).addListener(callable, NewsDatabase.databaseWriteExecutor);
        return future.get();
    }

    private void setupRssDataBase() {
        NewsDatabase db = NewsDatabase.getInstance(getApplicationContext());
        NewsDao dao = db.newsDao();
        //execute on separate thread
        try {
            List<RssEntity> rssData = getDefaultData(dao);
            rssAdapter.setRssData(rssData);
            rssAdapter.notifyDataSetChanged();
            updateProgressBar(false);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        /*NewsDatabase.databaseWriteExecutor.execute( () -> {
            rssAdapter.setRssData(dao.getAllRss());
        });*/
    }

    private void updateProgressBar(boolean shouldShow) {
        if (shouldShow) {
            feedsProgressBar.setVisibility(View.VISIBLE);
        } else {
            feedsProgressBar.setVisibility(View.GONE);
        }
    }

    /*private void setupNewsViewModel(int selectedRssId) {
        newsViewModel.getAllNews(selectedRssId).observe(this, newsObserver);
    }*/

    /*private final Observer<List<News>> newsObserver = new Observer<List<News>>() {
        @Override
        public void onChanged(List<News> news) {
            Toast.makeText(getBaseContext(), "News Data changed:"+i,Toast.LENGTH_LONG).show();
            i++;
        }
    };*/

    private void instantiateNewsFragment(RssEntity rssEntity) {
        Bundle data = new Bundle();
        data.putParcelable(NEWS_DATA_KEY, rssEntity);
        addFeedButton.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.news_fragment_container, NewsFragment.class, data)
                .commit();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == addFeedButton.getId()) {
            if (addFeedButton.isExtended()) {
                addFeedButton.shrink();
                addFeedButton.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.news_fragment_container, AddRssFragment.class, null)
                        .commit();
            } else {
                addFeedButton.extend();
            }
        }
    }

    private void setupBackPressEvent() {
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                List<Fragment> availableFragments = getSupportFragmentManager().getFragments();
                if (availableFragments.size() == 0) {
                    finish();
                    return;
                }
                Fragment currentFragment = availableFragments.get(availableFragments.size() - 1);
                if (currentFragment.isVisible()) {
                    getSupportFragmentManager().beginTransaction()
                            .remove(currentFragment)
                            .commit();
                    if (addFeedButton.getVisibility() == View.GONE) {
                        addFeedButton.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }
}