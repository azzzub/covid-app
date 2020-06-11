package com.zub.covid_19.api.newsData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsData {

    @Expose
    @SerializedName("articles")
    private List<Articles> articles;
    @Expose
    @SerializedName("totalResults")
    private int totalresults;
    @Expose
    @SerializedName("status")
    private String status;

    public List<Articles> getArticles() {
        return articles;
    }

    public int getTotalresults() {
        return totalresults;
    }

    public String getStatus() {
        return status;
    }

    public static class Articles {
        @Expose
        @SerializedName("content")
        private String content;
        @Expose
        @SerializedName("publishedAt")
        private String publishedat;
        @Expose
        @SerializedName("urlToImage")
        private String urltoimage;
        @Expose
        @SerializedName("url")
        private String url;
        @Expose
        @SerializedName("description")
        private String description;
        @Expose
        @SerializedName("title")
        private String title;
        @Expose
        @SerializedName("author")
        private String author;
        @Expose
        @SerializedName("source")
        private Source source;

        public String getContent() {
            return content;
        }

        public String getPublishedat() {
            return publishedat;
        }

        public String getUrltoimage() {
            return urltoimage;
        }

        public String getUrl() {
            return url;
        }

        public String getDescription() {
            return description;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public Source getSource() {
            return source;
        }
    }

    public static class Source {
        @Expose
        @SerializedName("name")
        private String name;

        public String getName() {
            return name;
        }
    }
}
