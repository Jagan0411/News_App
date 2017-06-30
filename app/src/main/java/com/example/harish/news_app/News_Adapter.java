package com.example.harish.news_app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.harish.news_app.pojo.NewsItem;

import java.util.ArrayList;

/**
 * Created by harish on 29-06-2017.
 */

public class News_Adapter extends RecyclerView.Adapter<News_Adapter.NewsViewHolder> {

    private ArrayList<NewsItem> newsItems;
    private final ItemClickListener itemClickListener;


    public interface ItemClickListener {
        void onListItemClick(int position);
    }

    public News_Adapter(ItemClickListener listener)
    {
        itemClickListener = listener;
    }

    @Override
    public News_Adapter.NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context =  parent.getContext();
        int listItemid = R.layout.news_list_item;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean attachParent = false;

        View view = layoutInflater.inflate(listItemid,parent,attachParent);

        NewsViewHolder newsViewHolder = new NewsViewHolder(view);
        return newsViewHolder;

    }

    @Override
    public void onBindViewHolder(News_Adapter.NewsViewHolder holder, int position) {

        NewsItem info = newsItems.get(position);
        holder.onBind(info);
    }

    @Override
    public int getItemCount() {
        if(newsItems == null)
        return 0;
        else
            return newsItems.size();
    }


    public void setNewsItems(ArrayList<NewsItem> items)
    {
        newsItems = items;
        notifyDataSetChanged();
    }

    public ArrayList<NewsItem> getNewsItems()
    {
        return newsItems;
    }

    class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title,desc,date;

        public NewsViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title_display);
            desc = (TextView) itemView.findViewById(R.id.desc_display);
            date = (TextView) itemView.findViewById(R.id.date_display);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {

                int posClicked = getAdapterPosition();
                itemClickListener.onListItemClick(posClicked);

        }

        void onBind(NewsItem item)
        {
            title.setText(item.getTitle());
            desc.setText(item.getDescription());
            date.setText(item.getDate());
        }
    }

}
