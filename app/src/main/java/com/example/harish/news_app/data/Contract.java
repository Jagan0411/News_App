package com.example.harish.news_app.data;

import android.provider.BaseColumns;

/**
 * Created by harish on 28-07-2017.
 */

//Creating constants for the table
public class Contract {

    public static class NEWS_TABLE implements BaseColumns {
        public static final String TABLE_NAME = "newsitems";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_AUTHOR = "author";
        public static final String COLUMN_NAME_URL = "url";
        public static final String COLUMN_NAME_URL_TO_IMAGE = "urltoimage";


    }
}
