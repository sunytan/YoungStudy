package ty.youngstudy.com.ttzw;

import android.util.Log;

import org.htmlparser.util.ParserException;

import java.net.URI;

import ty.youngstudy.com.bean.Novels;
import ty.youngstudy.com.reader.DataInterface;
import ty.youngstudy.com.reader.NovelDetail;

import static ty.youngstudy.com.ttzw.TTZWUtil.TTZW_BASE_URL;

/**
 * Created by edz on 2017/8/11.
 */

public class TTZWDataImpl implements DataInterface{

    private static final String TAG = "ttzw";
    @Override
    public Novels getSortKindNovels(String url) throws ParserException {
        Log.i(TAG,"getSortKindNovels");
        return TTZWUtil.getTTZWSortKindNovels(url);
    }

    public NovelDetail getNovelDetail(String url) throws ParserException {
        Log.i(TAG,"getNovelDetail");

        URI uri = URI.create(url);
        String host = uri.getHost();
        NovelDetail detail = null;
        if(host.startsWith("m")) {
            detail =  TTZWUtil.getTTZWNovelDetail(url);
            if(detail.getChapters() == null) {
                detail.setChapters(TTZWUtil.getNovelChapers(TTZW_BASE_URL,detail.getChapterUrl(),TAG));
            }
        } else {
            //detail = TTZWManager.getNovelDetailByMeta(url,getTag());
        }
        Log.d(TAG,"detail = "+"novel = "+detail.getNovel().toString()+",,,chapterurl = "+detail.getChapterUrl()
                +",,,chaptersize = "+detail.getChapters().size()+",,,chapter = "+detail.getChapters().get(detail.getChapters().size()-1));
        return detail;
    }

    @Override
    public DataInterface select(String url) {
        if (url.contains(TAG)) {
            return this;
        }
        return null;
    }
}
