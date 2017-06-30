package com.example.harish.news_app;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.harish.news_app.pojo.NewsItem;
import com.example.harish.news_app.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements News_Adapter.ItemClickListener{

    private TextView news_data;
    private ProgressBar pBar;
    private  News_Adapter news_adapter;
    private RecyclerView recyclerView;
    private  TextView errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*news_data = (TextView) findViewById(R.id.news_data);*/
        pBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        errorMessage = (TextView) findViewById(R.id.error_message_display);
        recyclerView = (RecyclerView) findViewById(R.id.news_data);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setHasFixedSize(true);

        news_adapter = new News_Adapter(this);

        recyclerView.setAdapter(news_adapter);

        loadNewsData();

    }


    public void loadNewsData() {
        showJsonDataView();
        QueryTask qt = new QueryTask();
        qt.execute();

    }

   /* private void makeSearchQuery()
    {
        //String gitQuery = mSearchBoxEditText.toString();
        URL gitURL = NetworkUtils.buildUrl();
        news_data.setText(gitURL.toString());
        new QueryTask().execute(gitURL);
    }*/

    private void showJsonDataView()
    {
        errorMessage.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void  showErrorMessage(){
        errorMessage.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onListItemClick(int position)
    {
        ArrayList<NewsItem> news_info = news_adapter.getNewsItems();
        Uri page = Uri.parse(news_info.get(position).getUrl());
        Intent i = new Intent(Intent.ACTION_VIEW,page);
        if(i.resolveActivity(getPackageManager()) != null)
        {
            startActivity(i);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_search) {

            news_adapter.setNewsItems(null);
            /*makeSearchQuery();*/

            loadNewsData();
            /*return true;*/
        }
        return true;
        /*return super.onOptionsItemSelected(item);*/
    }


    public class QueryTask extends AsyncTask<URL,Void,ArrayList<NewsItem>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<NewsItem> doInBackground(URL... urls) {

            ArrayList<NewsItem> searchResults = null;

            URL url = NetworkUtils.buildUrl();

            try {
                String json = NetworkUtils.getResponseFromHttpUrl(url);
                searchResults = NetworkUtils.parsingJson(json);
            }catch (IOException e)
            {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return searchResults;
        }

        @Override
        protected void onPostExecute(ArrayList<NewsItem> items) {
            super.onPostExecute(items);
            pBar.setVisibility(View.INVISIBLE);

            if(items!= null && !items.equals(""))
            {
                showJsonDataView();
                news_adapter.setNewsItems(items);
            }
            else{
                showErrorMessage();
            }
        }
    }

}
