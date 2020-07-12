package ty.youngstudy.com.database;

import org.litepal.crud.LitePalSupport;

/**
 * Created by edz on 2017/11/15.
 */

public class ChapterDB extends LitePalSupport {

    private int book_id;
    private String chapter_title;
    private String chapter_content_uri;
    private String chapter_url;
    private String SOURCE;

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setChapter_content_uri(String chapter_content_uri) {
        this.chapter_content_uri = chapter_content_uri;
    }

    public String getChapter_content_uri() {
        return chapter_content_uri;
    }

    public void setChapter_title(String chapter_title) {
        this.chapter_title = chapter_title;
    }

    public String getChapter_title() {
        return chapter_title;
    }

    public void setChapter_url(String chapter_url) {
        this.chapter_url = chapter_url;
    }

    public String getChapter_url() {
        return chapter_url;
    }

    public void setSOURCE(String SOURCE) {
        this.SOURCE = SOURCE;
    }

    public String getSOURCE() {
        return SOURCE;
    }
}
