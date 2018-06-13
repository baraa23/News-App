package com.example.baraa.newsappstage1;

public class News {

    // News title
    private String newsTitle;

    // News section
    private String newsCategory;

    // Author name
    private String newsAuthor;

    // Date of publishing
    private String newsDate;

    // News URL
    private String newsUrl;

    /**
     * Constructs a new{@link News} object.
     *
     * @param title    is the news title
     * @param category is the section of the news
     * @param author   is the author name
     * @param date     is the news date publishing
     * @param url      is news URL
     */

    public News(String title, String category, String author, String date, String url) {
        newsTitle = title;
        newsCategory = category;
        newsAuthor = author;
        newsDate = date;
        newsUrl = url;
    }

    /**
     * Returns the news title.
     */
    public String getNewsTitle() {
        return newsTitle;
    }

    /**
     * Returns the news category.
     */
    public String getNewsCategory() {
        return newsCategory;
    }

    /**
     * Returns the name of author.
     */
    public String getNewsAuthor() {
        return newsAuthor;
    }

    /**
     * Returns the publishing date.
     */
    public String getNewsDate() {
        return newsDate;
    }

    /**
     * Returns the news URL.
     */
    public String getUrl() {
        return newsUrl;
    }
}

