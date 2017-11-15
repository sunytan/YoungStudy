package ty.youngstudy.com;

import android.graphics.Bitmap;

import org.litepal.crud.DataSupport;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import ty.youngstudy.com.database.ChapterDB;
import ty.youngstudy.com.database.UserInfo;
import ty.youngstudy.com.reader.Chapter;

/**
 * Created by edz on 2017/10/27.
 */

public class DataHelper {

    public static void updateUserHead(String userName,Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        byte[] bytes = baos.toByteArray();
        UserInfo userInfo = null;
        List<UserInfo> userInfoList = DataSupport.where("userName = ?",userName).find(UserInfo.class);
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

    public List<Chapter> queryNovelChapterList(int bookid) {
        List<Chapter> chapters = new ArrayList<>();
        List<ChapterDB> chapterDBs = DataSupport.where("book_id = ?",String.valueOf(bookid)).find(ChapterDB.class);
        for (ChapterDB chapterDB : chapterDBs) {
            
        }

    }
}
