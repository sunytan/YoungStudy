package ty.youngstudy.com.database;

import org.litepal.crud.DataSupport;

/**
 * Created by edz on 2017/11/15.
 */

public class BookShelftDB extends DataSupport {

    private int BOOK_ID;
    private String readtime; //update time
    private String chapter_count;
    private String SOURCE;
    private String current_chapter_id;
    private String current_chapterposition;

    public void setBOOK_ID(int BOOK_ID) {
        this.BOOK_ID = BOOK_ID;
    }

    public int getBOOK_ID() {
        return BOOK_ID;
    }

    public void setChapter_count(String chapter_count) {
        this.chapter_count = chapter_count;
    }

    public String getChapter_count() {
        return chapter_count;
    }

    public void setCurrent_chapter_id(String current_chapter_id) {
        this.current_chapter_id = current_chapter_id;
    }

    public String getCurrent_chapter_id() {
        return current_chapter_id;
    }

    public void setCurrent_chapterposition(String current_chapterposition) {
        this.current_chapterposition = current_chapterposition;
    }

    public String getCurrent_chapterposition() {
        return current_chapterposition;
    }

    public void setReadtime(String readtime) {
        this.readtime = readtime;
    }

    public String getReadtime() {
        return readtime;
    }

    public void setSOURCE(String SOURCE) {
        this.SOURCE = SOURCE;
    }

    public String getSOURCE() {
        return SOURCE;
    }
}
