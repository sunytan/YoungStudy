package com.young.study;

import android.graphics.Bitmap;

import org.litepal.Operator;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.young.study.bean.Novel;
import com.young.study.bean.ShelftBook;
import com.young.study.database.BookShelftDB;
import com.young.study.database.ChapterDB;
import com.young.study.database.UserInfo;
import com.young.study.reader.Chapter;

/**
 * Created by edz on 2017/10/27.
 */

public class DataHelper {

    public static void updateUserHead(String userName,Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        byte[] bytes = baos.toByteArray();
        UserInfo userInfo = null;
        List<UserInfo> userInfoList = Operator.where("userName = ?",userName).find(UserInfo.class);
        if (userInfoList == null || userInfoList.size() == 0) {
            userInfo = new UserInfo();
            userInfo.setUserName(userName);
            userInfo.setUserHead(bytes);
            userInfo.save();
        }else {
            userInfo = userInfoList.get(0);
            userInfo.setUserHead(bytes);
            userInfo.save();
        }
    }

    public static List<Chapter> queryNovelChapterList(int bookid) {
        List<Chapter> chapters = new ArrayList<>();
        List<ChapterDB> chapterDBs = Operator.where("book_id = ?",String.valueOf(bookid)).find(ChapterDB.class);
        for (ChapterDB chapterDB : chapterDBs) {
            Chapter chapter = new Chapter();
            chapter.setTitle(chapterDB.getChapter_title());
            chapter.setBookId(chapterDB.getBook_id());
            chapter.setUrl(chapterDB.getChapter_url());
            chapter.setContentPath(chapterDB.getChapter_content_uri());
            chapter.setSource(chapterDB.getSOURCE());
            chapters.add(chapter);
        }
        return chapters;
    }

    public static void updateReadtime(int bookid) {
        String date = getCurrentReadTimeString();
        BookShelftDB bookShelftDB = Operator.where("book_id = ?",String.valueOf(bookid)).findFirst(BookShelftDB.class);
        if (bookShelftDB != null) {
            bookShelftDB.setReadtime(date);
            bookShelftDB.save();
        } else {
            bookShelftDB = new BookShelftDB();
            bookShelftDB.setReadtime(date);
            bookShelftDB.setBOOK_ID(bookid);
            bookShelftDB.save();
        }
    }

    public static List<ShelftBook> queryBookShelft() {
        List<ShelftBook> novels = new ArrayList<ShelftBook>();
        /*ContentResolver cr = AppUtils.getAppContext().getContentResolver();
        Cursor cursor = cr.query(NovelProvider.BOOKSHELFT_VIEW_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            ShelftBook novel = new ShelftBook();
            novel.id = cursor.getInt(cursor.getColumnIndex("ID"));
            novel.name = cursor.getString(cursor.getColumnIndex(SqliteHelper.Books.name));
            novel.readtime = cursor.getString(cursor.getColumnIndex(SqliteHelper.BookShelft.readtime));
            novel.chapterCount = cursor.getInt(cursor.getColumnIndex(SqliteHelper.BookShelft.chapter_count));
            novel.thumb = cursor.getString(cursor.getColumnIndex(SqliteHelper.Books.thumb));
            novel.url = cursor.getString(cursor.getColumnIndex(SqliteHelper.Books.url));
            novel.author = cursor.getString(cursor.getColumnIndex(SqliteHelper.Books.author));
            novel.brief = cursor.getString(cursor.getColumnIndex(Books.detail));
            novel.kind = cursor.getString(cursor.getColumnIndex(Books.kind));
            novel.lastUpdateChapter = cursor.getString(cursor.getColumnIndex("lastUpdateChapter"));
            novel.lastUpdateTime = cursor.getString(cursor.getColumnIndex("lastUpdateTime"));
            novel.setCurrentChapterPosition(
                    cursor.getInt(cursor.getColumnIndex(SqliteHelper.BookShelft.current_chapterposition)));
            novels.add(novel);
        }
        cursor.close();*/
        return novels;
    }

    public static Novel queryNovelById(int id) {
       /* ContentResolver cr = AppUtils.getAppContext().getContentResolver();
        Cursor cursor = cr.query(NovelProvider.NOVEL_URI, null, "ID = " + id, null, null);
        if (cursor.moveToNext()) {
            Novel novel = new Novel();
            novel.id = id;
            novel.name = cursor.getString(cursor.getColumnIndex(SqliteHelper.Books.name));
            novel.thumb = cursor.getString(cursor.getColumnIndex(SqliteHelper.Books.thumb));
            novel.url = cursor.getString(cursor.getColumnIndex(SqliteHelper.Books.url));
            novel.author = cursor.getString(cursor.getColumnIndex(SqliteHelper.Books.author));
            novel.brief = cursor.getString(cursor.getColumnIndex(Books.detail));
            novel.kind = cursor.getString(cursor.getColumnIndex(Books.kind));
            novel.lastUpdateChapter = cursor.getString(cursor.getColumnIndex("lastUpdateChapter"));
            novel.lastUpdateTime = cursor.getString(cursor.getColumnIndex("lastUpdateTime"));

            return novel;
        }*/
        return null;
    }


    public static boolean addToBookShelft(int bookid, int chapterSize) {
        /*ContentResolver cr = AppUtils.getAppContext().getContentResolver();
        ContentValues cv = new ContentValues();
        cv.put(BookShelft.BOOK_ID, bookid);
        cv.put(BookShelft.chapter_count, chapterSize);
        cv.put(BookShelft.readtime, getCurrentReadTimeString());
        cv.put(BookShelft.SOURCE, SourceSelector.getDefaultSourceTag());
        Uri uri = cr.insert(NovelProvider.BOOKSHELFT_URI, cv);
        if (!"-1".equals(uri.getLastPathSegment())) {
            return true;
        }*/
        return false;
    }

    public static void rmFromBookShelft(int bookId) {
        /*ContentResolver cr = AppUtils.getAppContext().getContentResolver();
        cr.delete(NovelProvider.BOOKSHELFT_URI, BookShelft.BOOK_ID + " = " + bookId, null);*/
    }


    public static boolean setCurrentReadChapterPosition(int bookid, int p) {
        BookShelftDB bookShelftDB = Operator.where("book_id = ?",String.valueOf(bookid)).findFirst(BookShelftDB.class);
        boolean saveSuccess = false;
        if (bookShelftDB != null) {
            bookShelftDB.setCurrent_chapterposition(p);
            saveSuccess = bookShelftDB.save();
        } else {
            bookShelftDB = new BookShelftDB();
            bookShelftDB.setBOOK_ID(bookid);
            bookShelftDB.setCurrent_chapterposition(p);
            saveSuccess = bookShelftDB.save();
        }
        return saveSuccess;
    }

    private static String getCurrentReadTimeString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = format.format(new java.util.Date());
        return date;
    }
}
