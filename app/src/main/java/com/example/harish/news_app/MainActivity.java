package com.example.harish.news_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.harish.news_app.data.DBHelper;
import com.example.harish.news_app.data.DBUtils;
import com.example.harish.news_app.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity implements News_Adapter.ItemClickListener,LoaderManager.LoaderCallbacks<Void>{

    /*private TextView news_data;*/
    private ProgressBar pBar;
    private  News_Adapter news_adapter;
    private RecyclerView recyclerView;
    /*private  TextView errorMessage;*/
    /*Toast toast;*/
    final int NEWS_APP_LOADER = 22;
    private SQLiteDatabase sqLiteDatabase;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*news_data = (TextView) findViewById(R.id.news_data);*/
        pBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        /*errorMessage = (TextView) findViewById(R.id.error_message_display);*/
        recyclerView = (RecyclerView) findViewById(R.id.news_data);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setHasFixedSize(true);

        sqLiteDatabase = new DBHelper(MainActivity.this).getReadableDatabase();
        cursor = DBUtils.getAll(sqLiteDatabase);

        news_adapter = new News_Adapter(cursor,this);

        recyclerView.setAdapter(news_adapter);

        //loadNewsData();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean bool = sharedPreferences.getBoolean("bool",true);

        if (bool)
        {
            getSupportLoaderManager().initLoader(NEWS_APP_LOADER, null, MainActivity.this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("bool", false);
            editor.commit();
        }

        JobDispatcher.schedule(this);

    }


    /*public void loadNewsData() {
        showJsonDataView();
        QueryTask qt = new QueryTask();
        qt.execute();

    }*/

    public void showNewsData()
    {
        recyclerView.setVisibility(View.VISIBLE);
    }

   /* private void makeSearchQuery()
    {
        //String gitQuery = mSearchBoxEditText.toString();
        URL gitURL = NetworkUtils.buildUrl();
        news_data.setText(gitURL.toString());
        new QueryTask().execute(gitURL);
    }*/

    @Override
    protected void onStart() {
        super.onStart();

        if(!sqLiteDatabase.isOpen())
        {
            sqLiteDatabase = new DBHelper(MainActivity.this).getReadableDatabase();
            cursor = DBUtils.getAll(sqLiteDatabase);
            news_adapter.swapCursor(cursor);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!sqLiteDatabase.isOpen()) {
            sqLiteDatabase = new DBHelper(MainActivity.this).getReadableDatabase();
            cursor = DBUtils.getAll(sqLiteDatabase);
            news_adapter.swapCursor(cursor);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        sqLiteDatabase.close();
        cursor.close();
    }

    /*private void showJsonDataView()
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

    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_search)
        {
            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<Void> loader = loaderManager.getLoader(NEWS_APP_LOADER);

            if (loader == null)
                {
                    loaderManager.initLoader(NEWS_APP_LOADER, null, this);
                }
            else
                {
                loaderManager.restartLoader(NEWS_APP_LOADER, null, this).forceLoad();
                }


            /*news_adapter.setNewsItems(null);
            *//*makeSearchQuery();*//*

            loadNewsData();
            *//*return true;*/
        }
        return true;
        /*return super.onOptionsItemSelected(item);*/
    }

    /*public class QueryTask extends AsyncTask<URL,Void,ArrayList<NewsItem>>
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
    }*/


    @Override
    public void onListItemClick(int position) {
        String news_data = news_adapter.getNewsURL(position);
        Uri uri = Uri.parse(news_data);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        if (intent.resolveActivity(getPackageManager()) != null)
        {
            startActivity(intent);
        }
    }

    @Override
    public Loader<Void> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Void>(this) {

            @Override
            protected void onStartLoading()
            {
                pBar.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Override
            public Void loadInBackground() {
                NetworkUtils.reloadDB(MainActivity.this);
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Void> loader, Void data) {
        pBar.setVisibility(View.GONE);
        showNewsData();
        sqLiteDatabase = new DBHelper(MainActivity.this).getReadableDatabase();
        cursor = DBUtils.getAll(sqLiteDatabase);
        news_adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Void> loader) {

    }

}
