package ty.youngstudy.com.reader;

import android.content.Context;
import android.util.Log;

import java.util.List;

import ty.youngstudy.com.bean.Novel;

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
        if (currentNovel != null && this.currentNovel.getId() == currentNovel.getId()) {
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

}
