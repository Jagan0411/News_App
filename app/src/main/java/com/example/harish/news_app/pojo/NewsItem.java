package com.example.harish.news_app.pojo;

/**
 * Created by harish on 29-06-2017.
 */

public class NewsItem {

    String author;
    String title;
    String description;
    String url;
    String urlToImage;
    String date;



    public NewsItem(String author, String title, String description, String url, String urlToImage, String date) {
        this.author = author;
        this.title = title;

        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
