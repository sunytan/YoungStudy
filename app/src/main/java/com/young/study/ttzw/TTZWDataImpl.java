package com.young.study.ttzw;

import android.text.TextUtils;
import android.util.Log;

import org.htmlparser.util.ParserException;

import java.net.URI;

import com.young.study.bean.Novels;
import com.young.study.reader.DataInterface;
import com.young.study.reader.NovelDetail;
import com.young.study.util.HtmlUtil;

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

    @Override
    public String getChapterContent(String url) throws ParserException {
        System.out.println("url = " + url);
        String html = HtmlUtil.readHtml(url);
        if(TextUtils.isEmpty(html)) {
            html = url;
        }
        return TTZWUtil.getChapterContent(html);
        //return TTZWManager.getChapterContent(html);
    }

    public NovelDetail getNovelDetail(String url) throws ParserException {
        Log.i(TAG,"getNovelDetail");

        URI uri = URI.create(url);
        String host = uri.getHost();
        NovelDetail detail = null;
        if(host.startsWith("m")) {
            detail =  TTZWUtil.getTTZWNovelDetail(url);
            if(detail.getChapters() == null) {
                detail.setChapters(TTZWUtil.getNovelChapers(url,detail.getChapterUrl(),TAG));
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
