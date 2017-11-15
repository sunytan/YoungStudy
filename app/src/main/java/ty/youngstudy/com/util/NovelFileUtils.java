package ty.youngstudy.com.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;

import ty.youngstudy.com.MyApplication;
import ty.youngstudy.com.bean.Novel;
import ty.youngstudy.com.reader.Chapter;
import ty.youngstudy.com.reader.manager.NovelManager;

/**
 * Created by edz on 2017/11/15.
 */

public class NovelFileUtils {

    private static final String TAG = "NovelFileUtils";

    public static String DIRECTORY;

    public static void init() {
        DIRECTORY = MyApplication.getInstance().getContext().getPackageName()+"novel";
        File baseDir = Environment.getExternalStorageDirectory();
        File file = new File(baseDir, DIRECTORY);
        if(file.exists()) {
            if(!file.isDirectory()) {
                file.delete();
                file.mkdir();
            }
        } else {
            file.mkdir();
        }
    }

    public static void deleteNovel(Novel n) {
        String base = getBaseDir();
        File dir = new File(new File(base), n.getName() + "/" + n.getAuthor());
        File files[] = dir.listFiles();
        if(files == null)
            return;
        for(File file : files) {
            file.delete();
        }
        dir.delete();
    }

    public static String getBaseDir() {
        File baseDir = Environment.getExternalStorageDirectory();
        File file = new File(baseDir, DIRECTORY);
        return file.getPath();
    }

    public static String getChapterPath(Novel novel,int c) {
        File baseDir = Environment.getExternalStorageDirectory();
        Chapter chapter = NovelManager.getInstance().getChapter(c);
        File distFile = new File(baseDir, DIRECTORY + "/" +novel.getName() +"/" + novel.getAuthor()  + "/" + chapter.getTitle().trim() + ".txt");
        return distFile.getPath();
    }

    public static String getChapterPath(Novel novel,Chapter chapter) {
        File baseDir = Environment.getExternalStorageDirectory();
        File distFile = new File(baseDir, DIRECTORY + "/" +novel.getName() +"/" + novel.getAuthor()  + "/" + chapter.getTitle().trim() + ".txt");
        return distFile.getPath();
    }

    public static File getChapterFile(String bookId, int chapter) {
        File file = new File(getChapterPath(NovelManager.getInstance().getCurrentNovel(), chapter));
        if (!file.exists())
            createFile(file);
        return file;
    }

    public static boolean isChapterExist(Novel novel, Chapter chapter) {
        String path = getChapterPath(novel, chapter);
        File file = new File(path);
        return file.exists() && file.length() > 10;
    }

    public static boolean isChapterExist(Chapter chapter) {
        Novel novel = NovelManager.getInstance().getCurrentNovel();
        String path = getChapterPath(novel, chapter);
        File file = new File(path);
        return file.exists() && file.length() > 10;
    }

    public static String createFile(File file) {
        try {
            if (file.getParentFile().exists()) {
                Log.i(TAG,"----- 创建文件" + file.getAbsolutePath());
                file.createNewFile();
                return file.getAbsolutePath();
            } else {
                createDir(file.getParentFile().getAbsolutePath());
                file.createNewFile();
                Log.i(TAG,"----- 创建文件" + file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String createDir(String dirPath) {
        try {
            File file = new File(dirPath);
            if (file.getParentFile().exists()) {
                Log.i(TAG,"----- 创建文件夹" + file.getAbsolutePath());
                file.mkdir();
                return file.getAbsolutePath();
            } else {
                createDir(file.getParentFile().getAbsolutePath());
                Log.i(TAG,"----- 创建文件夹" + file.getAbsolutePath());
                file.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dirPath;
    }
}
