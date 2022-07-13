package com.aniruddha.news_feeds;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {
    private final Context context;
    private List<News> newsData = new ArrayList<>();

    private NewsClickListener newslistener;

    public NewsListAdapter(Context context) {
        this.context = context;
    }

    public void setNewsData(List<News> newsData) {
        this.newsData = newsData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View newsItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_list_item, parent, false);
        return new ViewHolder(newsItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        News currentNews = newsData.get(position);
        String imageUrl = currentNews.newsImage;
        String title = currentNews.newsTitle;
        String description = currentNews.newsDescription;

        holder.newsTitle.setText(title);
        if (!TextUtils.isEmpty(description)) {
            holder.newsDescription.setVisibility(View.VISIBLE);
            holder.newsDescription.setText(description);
        }
        if (TextUtils.isEmpty(imageUrl)) {
            holder.newsImage.setImageResource(R.drawable.baseline_broken_image_24);
        } else {
            Picasso.with(context).load(imageUrl).fit().centerInside().into(holder.newsImage);
        }
    }

    @Override
    public int getItemCount() {
        return newsData.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView newsImage;
        private final TextView newsTitle;
        private final TextView newsDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            newsImage = itemView.findViewById(R.id.news_image);
            newsTitle = itemView.findViewById(R.id.news_title);
            newsDescription = itemView.findViewById(R.id.news_description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    newslistener.onNewsSelected(newsData.get(getAdapterPosition()).newsLink);
                }
            });
        }
    }

    public interface NewsClickListener{
        void onNewsSelected(@Nullable String url);
    }

    public void setNewsClickListener(NewsClickListener listener) {
        this.newslistener = listener;
    }
}

