package com.young.study.database;

import org.litepal.crud.LitePalSupport;

/**
 * Created by edz on 2017/11/15.
 */

public class BooksDB extends LitePalSupport {

    private String author;
    private String name;
    private String url;
    private String kind;
    private String thumb;
    private String detail;
    private String lastUpdateTime;
    private String lastUpdateChapter;
    private String CHAPTER_URL;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setCHAPTER_URL(String CHAPTER_URL) {
        this.CHAPTER_URL = CHAPTER_URL;
    }

    public String getCHAPTER_URL() {
        return CHAPTER_URL;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getKind() {
        return kind;
    }

    public void setLastUpdateChapter(String lastUpdateChapter) {
        this.lastUpdateChapter = lastUpdateChapter;
    }

    public String getLastUpdateChapter() {
        return lastUpdateChapter;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getThumb() {
        return thumb;
    }

}
