package com.aniruddha.news_feeds;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 *
 */
public class AddRssFragment extends Fragment implements View.OnClickListener {
    private EditText rssTitle;
    private EditText rssSource;
    private EditText rssImageLink;
    private Button addRssButton;
    private Button clearFieldsButton;
    private NewsViewModel newsViewModel;

  @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_rss, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rssTitle = view.findViewById(R.id.add_rss_title);
        rssSource = view.findViewById(R.id.add_rss_source_link);
        rssImageLink = view.findViewById(R.id.add_rss_image_link);
        addRssButton = view.findViewById(R.id.insert_rss_button);
        clearFieldsButton = view.findViewById(R.id.clear_text_field_button);
        addRssButton.setOnClickListener(this);
        clearFieldsButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.insert_rss_button) {
            String title = rssTitle.getText().toString();
            String source = rssSource.getText().toString();
            String imageLink = rssImageLink.getText().toString();

            if (TextUtils.isEmpty(title) || !URLUtil.isValidUrl(source) || !URLUtil.isValidUrl(imageLink)) {
                Toast.makeText(getContext(), "Please enter valid data", Toast.LENGTH_LONG).show();
                return;
            }

            RssEntity newRssEntity = new RssEntity();
            newRssEntity.rssName = title;
            newRssEntity.rssSource = source;
            newRssEntity.rssImage = imageLink;

            if (!validateSource(newRssEntity)) {
                Toast.makeText(getContext(), "Invalid Rss link", Toast.LENGTH_SHORT).show();
                return;
            }

            // We validated the RSS info, now add the Rss Data in DB.
            NewsDatabase.databaseWriteExecutor.execute( () -> {
                newsViewModel.addNewRssFeed(newRssEntity);
            });
            Toast.makeText(getContext(), "Adding Rss Entity in DB...", Toast.LENGTH_LONG).show();
        } else  if (v.getId() == R.id.clear_text_field_button) {
            rssTitle.setText("");
            rssSource.setText("");
            rssImageLink.setText("");
        }
    }

    /*private boolean validateImage(String imageLink) {
        try {
            Picasso.with(getContext()).load(imageLink).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }*/

    private boolean validateSource(RssEntity newRssEntity) {
      return new DownloadRssXmlTask(getContext(), newRssEntity).isValidRssSource();
    }
}