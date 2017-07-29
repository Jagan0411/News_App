package com.example.harish.news_app;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.harish.news_app.pojo.NewsItem;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.harish.news_app.data.Contract.NEWS_TABLE.COLUMN_NAME_DATE;
import static com.example.harish.news_app.data.Contract.NEWS_TABLE.COLUMN_NAME_DESCRIPTION;
import static com.example.harish.news_app.data.Contract.NEWS_TABLE.COLUMN_NAME_TITLE;
import static com.example.harish.news_app.data.Contract.NEWS_TABLE.COLUMN_NAME_URL;
import static com.example.harish.news_app.data.Contract.NEWS_TABLE.COLUMN_NAME_URL_TO_IMAGE;

/**
 * Created by harish on 29-06-2017.
 */

public class News_Adapter extends RecyclerView.Adapter<News_Adapter.NewsViewHolder> {

    private ArrayList<NewsItem> newsItems;
    private final ItemClickListener itemClickListener;
    private Cursor cursor;
    private Context context;


    public interface ItemClickListener {
        void onListItemClick(int position);
    }

    public News_Adapter(Cursor cursor,ItemClickListener listener)
    {
        itemClickListener = listener; this.cursor = cursor;
    }

    @Override
    public News_Adapter.NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context =  parent.getContext();
        int listItemid = R.layout.news_list_item;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean attachParent = false;

        View view = layoutInflater.inflate(listItemid,parent,attachParent);

        NewsViewHolder newsViewHolder = new NewsViewHolder(view);
        return newsViewHolder;

    }

    @Override
    public void onBindViewHolder(News_Adapter.NewsViewHolder holder, int position) {

        //NewsItem info = newsItems.get(position);
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {

        return cursor.getCount();
        /*if(newsItems == null)
        return 0;
        else
            return newsItems.size();*/
    }


    /*public void setNewsItems(ArrayList<NewsItem> items)
    {
        newsItems = items;
        notifyDataSetChanged();
    }*/


    public void swapCursor(Cursor cursor1) {
        if (cursor != null) cursor.close();
        cursor = cursor1;
        if (cursor1 != null) {
            this.notifyDataSetChanged(); //Refreshing rv
        }
    }

    public String getNewsURL(int position) {
        cursor.moveToPosition(position);
        return cursor.getString(cursor.getColumnIndex(COLUMN_NAME_URL));
    }

    /*public ArrayList<NewsItem> getNewsItems()
    {
        return newsItems;
    }*/

    class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, desc, date;
        ImageView iv;

        public NewsViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title_display);
            desc = (TextView) itemView.findViewById(R.id.desc_display);
            date = (TextView) itemView.findViewById(R.id.date_display);
            iv = (ImageView) itemView.findViewById(R.id.imgView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int posClicked = getAdapterPosition();
            itemClickListener.onListItemClick(posClicked);

        }

        //Binding to recyclerview from db
        void onBind(int position) {
            cursor.moveToPosition(position);
            title.setText(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TITLE)));
            desc.setText(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DESCRIPTION)));
            String getdate = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DATE));

            try {
                String formattedDate = DateParse(getdate);
                date.setText(formattedDate);
            } catch (ParseException e) {
                date.setText(getdate);
            }
            /*title.setText(item.getTitle());
            desc.setText(item.getDescription());
            date.setText(item.getDate());*/

            String url = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_URL_TO_IMAGE));
            if (url != null) {
                Picasso.with(context)
                        .load(url)
                        .into(iv);
            }
        }

    }
    //Parsing date and returning the same
    public static String DateParse(String input) throws ParseException
    {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-d'T'HH:mm:ss'Z'");
        Date date = parser.parse(input);
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy - hh:mma");
        String formattedDate = formatter.format(date);
        Log.d("News_Adapter", formattedDate);
        return formattedDate;
    }

}
