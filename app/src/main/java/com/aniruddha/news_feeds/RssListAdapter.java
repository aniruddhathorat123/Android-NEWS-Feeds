package com.aniruddha.news_feeds;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RssListAdapter extends RecyclerView.Adapter<RssListAdapter.ViewHolder> {
    private final Context context;
    private List<RssEntity> rssEntities = new ArrayList<>();
    private OnItemClickListener listener;

    public RssListAdapter(Context context) {
        this.context = context;
    }

    public void setRssData(List<RssEntity> rssEntities) {
        this.rssEntities = rssEntities;
    }

    public boolean isRssDataChanged(List<RssEntity> rssEntities) {
        return !(this.rssEntities.equals(rssEntities));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rss_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RssEntity currentData = rssEntities.get(position);
        String image = currentData.rssImage;
        String title = currentData.rssName;

        holder.feedTitle.setText(title);
        if (TextUtils.isEmpty(image)) {
            holder.feedImage.setImageResource(R.drawable.baseline_broken_image_24);
        } else {
            Picasso.with(context).load(image).fit().centerInside().into(holder.feedImage);
        }
    }

    @Override
    public int getItemCount() {
        return rssEntities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView feedImage;
        private final TextView feedTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            feedImage = itemView.findViewById(R.id.rss_image);
            feedTitle = itemView.findViewById(R.id.rss_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClicked(rssEntities.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(RssEntity rssData);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
