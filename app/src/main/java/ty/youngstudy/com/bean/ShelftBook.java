package ty.youngstudy.com.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by edz on 2017/7/28.
 */

public class ShelftBook extends Novel implements Parcelable {
    private int currentChapterId;
    private int currentChapterPosition;

    public int chapterCount;
    private String readTime;

    public ShelftBook() {}

    public ShelftBook(int id){
        this.id = id;
    }

    public int getBookId() {
        return id;
    }
    public void setBookId(int bookId) {
        this.id = bookId;
    }

    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }

    public int getChapterCount() {
        return chapterCount;
    }

    public int getCurrentChapterId() {
        return currentChapterId;
    }

    public void setCurrentChapterId(int currentChapterId) {
        this.currentChapterId = currentChapterId;
    }

    public int getCurrentChapterPosition() {
        return currentChapterPosition;
    }

    public void setCurrentChapterPosition(int currentChapterPosition) {
        this.currentChapterPosition = currentChapterPosition;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(author);
        parcel.writeString(brief);
        parcel.writeString(thumb);
        parcel.writeString(url);
        parcel.writeString(kind);
        parcel.writeString(lastUpdateTime);
        parcel.writeString(lastUpdateChapter);
        parcel.writeString(lastUpdateChapterUrl);
        parcel.writeInt(currentChapterId);
        parcel.writeInt(currentChapterPosition);
        parcel.writeInt(chapterCount);
        parcel.writeString(readTime);
    }

    public static final Parcelable.Creator<ShelftBook> CREATOR = new Creator<ShelftBook>() {
        @Override
        public ShelftBook createFromParcel(Parcel parcel) {
            ShelftBook book = new ShelftBook();
            book.id = parcel.readInt();
            book.name = parcel.readString();
            book.author = parcel.readString();
            book.brief = parcel.readString();
            book.thumb = parcel.readString();
            book.url = parcel.readString();
            book.kind = parcel.readString();
            book.lastUpdateTime = parcel.readString();
            book.lastUpdateChapter = parcel.readString();
            book.lastUpdateChapterUrl = parcel.readString();
            book.currentChapterId = parcel.readInt();
            book.currentChapterPosition = parcel.readInt();
            book.chapterCount = parcel.readInt();
            book.readTime = parcel.readString();
            return book;

        }

        @Override
        public ShelftBook[] newArray(int i) {
            return new ShelftBook[i];
        }
    };
}
