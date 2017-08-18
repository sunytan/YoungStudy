package ty.youngstudy.com.reader;

import java.util.ArrayList;

import ty.youngstudy.com.bean.Novels;

/**
 * Created by edz on 2017/8/16.
 */

public class NovelTotleInfo {

    private static NovelTotleInfo instance = new NovelTotleInfo();

    private boolean isUpdate = false;

    private ArrayList<Novels> novels = new ArrayList<Novels>();

    public static NovelTotleInfo getInstance(){
        return instance;
    }

    public ArrayList<Novels> getNovels() {
        return novels;
    }

    public void setNovels(ArrayList<Novels> novels) {
        this.novels = novels;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public boolean getUpdate(){
        return this.isUpdate;
    }
}
