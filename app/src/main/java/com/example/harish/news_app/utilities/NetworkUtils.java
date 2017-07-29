package com.example.harish.news_app.utilities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.harish.news_app.data.DBHelper;
import com.example.harish.news_app.data.DBUtils;
import com.example.harish.news_app.pojo.NewsItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by harish on 19-06-2017.
 */

public class NetworkUtils {

    final static String NEWSAPP_BASE_URL =
            "https://newsapi.org/v1/articles";

    final static String PARAM_SOURCE = "source";
    final static  String source = "the-next-web";

    final static String PARAM_SORT = "sort";
    final static String sortBy = "latest";

    final static String PARAM_KEY = "apiKey";
    final static String apiKEY = "6fb48b5dbbea4f6ba7b851c5cc110508";

    public static URL buildUrl() {

        Uri uri = Uri.parse(NEWSAPP_BASE_URL).buildUpon().appendQueryParameter(PARAM_SOURCE,source)
                .appendQueryParameter(PARAM_SORT,sortBy).appendQueryParameter(PARAM_KEY,apiKEY).build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }


    public static ArrayList<NewsItem> parsingJson(String jsonVal) throws JSONException {
        ArrayList<NewsItem> result = new ArrayList<>();
        JSONObject main = new JSONObject(jsonVal);
        JSONArray items = main.getJSONArray("articles");

        for (int i = 0; i < items.length(); i++)
        {
            JSONObject item = items.getJSONObject(i);
            String author= item.getString("author");
            String title = item.getString("title");
            String desc = item.getString("description");
            String urlstring = item.getString("url");
            String imageurl = item.getString("urlToImage");
            String date = item.getString("publishedAt");

            NewsItem newsItem = new NewsItem(author, title, desc, urlstring, imageurl, date);
            result.add(newsItem);
        }

        return result;
    }

    //Reloading the db in background
    public static void reloadDB(Context context)  {
        ArrayList<NewsItem> result = null;
        URL  url = buildUrl();
        SQLiteDatabase sqLiteDatabase = new DBHelper(context).getWritableDatabase();
        try {

            String json = getResponseFromHttpUrl(url);
            result = parsingJson(json);

            if(result!=null)
            {
                DBUtils.deleteAll(sqLiteDatabase);
                DBUtils.bulkInsert(sqLiteDatabase, result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqLiteDatabase.close();
        }

    }

}
