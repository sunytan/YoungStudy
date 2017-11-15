package ty.youngstudy.com.reader.manager;

import android.content.Context;
import android.util.Log;

import java.util.List;

import ty.youngstudy.com.bean.Novel;
import ty.youngstudy.com.reader.Chapter;

/**
 * Created by edz on 2017/8/21.
 */

public class NovelManager {

    private static NovelManager instance;

    private Novel currentNovel;
    private List<Chapter> chapterList;
    private int chapterId = 1;

    public static NovelManager getInstance() {
        if (instance == null)
            instance = new NovelManager();
        return instance;
    }

    public void init(Context context){

    }

    public void setCurrentNovel(Novel currentNovel) {
        if (currentNovel != null && this.currentNovel != null && this.currentNovel.getId() == currentNovel.getId()) {
            Log.i("tanyang","setCurrentNovel return");
            return;
        }
        this.currentNovel = currentNovel;
        chapterList = null;
        chapterId = 1;
    }

    public Novel getCurrentNovel() {
        return currentNovel;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterList(List<Chapter> chapterList) {
        this.chapterList = chapterList;
    }

    public List<Chapter> getChapterList() {
        return chapterList;
    }

    public int getChapterSize() {
        if(chapterList == null) {
            return 0;
        }
        return chapterList.size();
    }

    public Chapter getChapter() {
        return chapterList.get(chapterId - 1);
    }

    public Chapter getChapter(int chapter) {
        return chapterList.get(chapter - 1);
    }

}
