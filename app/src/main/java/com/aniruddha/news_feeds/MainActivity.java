package com.aniruddha.news_feeds;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.List;

/**
 * App's launcher activity.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        FirestoreOperationCallback {
    public static final String NEWS_DATA_KEY = "news_data_key";
    private static final boolean IS_OWNER_MODE_ENABLED = false;

    private RecyclerView rssRecyclerView;
    private RssListAdapter rssAdapter;
    private ProgressBar feedsProgressBar;
    private ExtendedFloatingActionButton addFeedButton;
    private NewsViewModel newsViewModel;
    private FirebaseOperations firebaseOperations;

    // Click listener for Rss item clicked.
    private final RssListAdapter.OnItemClickListener rssClickListener =
            new RssListAdapter.OnItemClickListener() {
                @Override
                public void onItemClicked(RssEntity rssData) {
                    new DownloadRssXmlTask(getApplicationContext(), rssData).execute();
                    // initiate the news viewModel.
                    instantiateNewsFragment(rssData);
                    //updateActionBar(true, rssData.rssName);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseOperations = FirebaseOperations.getInstance(this);
        newsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        rssRecyclerView = findViewById(R.id.rss_list);
        feedsProgressBar = findViewById(R.id.main_progressBr);
        addFeedButton = findViewById(R.id.add_feed_button);
        if (IS_OWNER_MODE_ENABLED) {
            addFeedButton.setVisibility(View.VISIBLE);
            addFeedButton.shrink();
            addFeedButton.setOnClickListener(this);
        }
        updateProgressBar(true);
        syncRssList();
        setupRssList();
        setupRssFeedObserver();
        setupBackPressEvent();
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            handleBackPressEvent();
        }
        return false;
    }

    @Override
    public void onRssSyncCompletion(List<RssEntity> rssEntities) {
        NewsDatabase.databaseWriteExecutor.execute( () -> {
            for (RssEntity entity : rssEntities) {
                newsViewModel.syncRssFeed(entity);
            }
        });
    }

    @Override
    public void onDeletedRssSyncCompletion(List<RssEntity> rssEntities) {
        NewsDatabase.databaseWriteExecutor.execute( () -> {
            for (RssEntity entity : rssEntities) {
                newsViewModel.deleteRssFeed(entity);
            }
        });
    }

    /**
     * Sync all RSS feeds data with firestore DB.
     */
    private void syncRssList() {
        firebaseOperations.getAllRssDetails();
        firebaseOperations.getAllDeletedRssDetails();
    }

    private void updateProgressBar(boolean shouldShow) {
        if (shouldShow) {
            feedsProgressBar.setVisibility(View.VISIBLE);
        } else {
            feedsProgressBar.setVisibility(View.GONE);
        }
    }

    private void setupRssList() {
        rssRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(getBaseContext(), DividerItemDecoration.VERTICAL);
        rssAdapter = new RssListAdapter(getBaseContext());
        rssRecyclerView.setAdapter(rssAdapter);
        //rssRecyclerView.addItemDecoration(dividerItemDecoration);
        rssAdapter.setOnItemClickListener(rssClickListener);
    }

    public void setupRssFeedObserver() {
        Observer<List<RssEntity>> newsObserver = new Observer<List<RssEntity>>() {
            @Override
            public void onChanged(List<RssEntity> rssEntities) {
                if (rssAdapter.isRssDataChanged(rssEntities)) {
                    rssAdapter.setRssData(rssEntities);
                    rssAdapter.notifyDataSetChanged();
                    updateProgressBar(false);
                }
            }
        };
        newsViewModel.getAllAvailableRss().observe(this, newsObserver);
    }

    private void instantiateNewsFragment(RssEntity rssEntity) {
        Bundle data = new Bundle();
        data.putParcelable(NEWS_DATA_KEY, rssEntity);
        addFeedButton.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.news_fragment_container, NewsFragment.class, data)
                .commit();
    }



    private void setupBackPressEvent() {
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handleBackPressEvent();
            }
        });
    }

    private void handleBackPressEvent() {
        List<Fragment> availableFragments = getSupportFragmentManager().getFragments();
        if (availableFragments.size() == 0) {
            finish();
            return;
        }
        // means all fragments are getting removed so reset the activity's action bar.
        if (availableFragments.size() == 1) {
            resetActionBar();
        }
        Fragment currentFragment = availableFragments.get(availableFragments.size() - 1);
        if (currentFragment.isVisible()) {
            getSupportFragmentManager().beginTransaction()
                    .remove(currentFragment)
                    .commit();
            if (addFeedButton.getVisibility() == View.GONE && IS_OWNER_MODE_ENABLED) {
                addFeedButton.setVisibility(View.VISIBLE);
            }
        }
    }

    private void resetActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setSubtitle(null);
        }
    }
}