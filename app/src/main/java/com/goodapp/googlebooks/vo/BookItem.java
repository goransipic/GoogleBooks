package com.goodapp.googlebooks.vo;

/**
 * Created by gsipic on 11/01/2018.
 */

public class BookItem {

    private String title;
    private String subtitle;
    private long publishedDate;
    private String author;
    private float aspectRatio;
    private String imageUrl;

    public BookItem(){

    }
    public BookItem(
            String title,
            String subtitle,
            long publishedDate,
            String author,
            float apectRatio,
            String imageUrl
    ) {
        this.title = title;
        this.subtitle = subtitle;
        this.publishedDate = publishedDate;
        this.author = author;
        this.aspectRatio = apectRatio;
        this.imageUrl = imageUrl;
    }


    public void setTitle(String mTitle) {
        this.title = mTitle;
    }

    public void setSubtitle(String mSubtitle) {
        this.subtitle = mSubtitle;
    }

    public void setPublishedDate(long mPublishedDate) {
        this.publishedDate = mPublishedDate;
    }

    public void setAuthor(String mAuthor) {
        this.author = mAuthor;
    }

    public void setAspectRatio(float mAspectRatio) {
        this.aspectRatio = mAspectRatio;
    }

    public String getTitle() {
        return title;
    }

    public long getPublishedDate() {
        return publishedDate;
    }

    public String getAuthor() {
        return author;
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
