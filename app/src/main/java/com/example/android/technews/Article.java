package com.example.android.technews;

/**
 * Created by Aloraifi on 02/05/2018.
 */

public class Article {
    private String mTitle;
    private String mSection;
    private String mAuthor;
    private int mNoAuthorResourceId;
    private String mPublicationDate;
    private String mWebUrl;

    // Constructor when there is an article author.
    public Article(String title, String section, String author, String publicationDate, String webUrl) {
        mTitle = title;
        mSection = section;
        mAuthor = author;
        mPublicationDate = publicationDate;
        mWebUrl = webUrl;
    }

    // Constructor when there is no article author.
    public Article(String title, String section, int noAuthorResourceId, String publicationDate, String webUrl) {
        mTitle = title;
        mSection = section;
        mNoAuthorResourceId = noAuthorResourceId;
        mPublicationDate = publicationDate;
        mWebUrl = webUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSection() {
        return mSection;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public int getNoAuthorResourceId() {
        return mNoAuthorResourceId;
    }

    public String getPublicationDate() {
        return mPublicationDate;
    }

    public String getWebUrl() {
        return mWebUrl;
    }
}
