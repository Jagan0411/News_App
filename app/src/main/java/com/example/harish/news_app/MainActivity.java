package com.example.harish.news_app;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.harish.news_app.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText mSearchBoxEditText;

    private TextView mUrlDisplayTextView;

    private TextView mSearchResultsTextView;

    private TextView errorMessage;

    private ProgressBar pBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBoxEditText = (EditText) findViewById(R.id.search_box);
        mUrlDisplayTextView = (TextView) findViewById(R.id.url_display);
        mSearchResultsTextView = (TextView) findViewById(R.id.search_results_json);
        errorMessage = (TextView) findViewById(R.id.error_message_display);
        pBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);

    }

    private void makeSearchQuery()
    {
        String gitQuery = mSearchBoxEditText.toString();
        URL gitURL = NetworkUtils.buildUrl(gitQuery);
        mUrlDisplayTextView.setText(gitURL.toString());
        new QueryTask().execute(gitURL);
    }

    private void showJsonDataView()
    {
        errorMessage.setVisibility(View.INVISIBLE);
        mSearchResultsTextView.setVisibility(View.VISIBLE);
    }

    private void  showErrorMessage(){
        errorMessage.setVisibility(View.VISIBLE);
        mSearchResultsTextView.setVisibility(View.INVISIBLE);
    }

    public class QueryTask extends AsyncTask<URL,Void,String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL searchURL = urls[0];
            String searchResults = null;

            try {
                searchResults = NetworkUtils.getResponseFromHttpUrl(searchURL);
            }catch (IOException e)
            {
                e.printStackTrace();
            }

            return searchResults;
        }

        @Override
        protected void onPostExecute(String s) {

            pBar.setVisibility(View.INVISIBLE);

            if(s!= null && !s.equals(""))
            {
                showJsonDataView();
                mSearchResultsTextView.setText(s);
            }
            else{
                showErrorMessage();
            }
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

            makeSearchQuery();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
