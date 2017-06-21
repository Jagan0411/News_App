package com.example.harish.news_app.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

    final static String PARAM_SEARCH = "search";


    /**
     * Builds the URL used to query Github.
     *
     * @param searchQuery The keyword that will be queried for.
     * @return The URL to use to query the weather server.
     */
    public static URL buildUrl(String searchQuery) {

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
}
