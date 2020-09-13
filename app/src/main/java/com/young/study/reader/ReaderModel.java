package com.young.study.reader;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import org.htmlparser.util.ParserException;

import java.util.ArrayList;

import com.young.study.BaseModel;
import com.young.study.R;
import com.young.study.bean.Novel;
import com.young.study.bean.Novels;
import com.young.study.mvp.PresenterEventMessage;
import com.young.study.reader.manager.DataQueryManager;
import com.young.study.reader.manager.NovelManager;
import com.young.study.util.NovelFileUtils;

/**
 * Created by edz on 2017/8/10.
 */

public class ReaderModel extends BaseModel {

    public final static String NAME = "ReaderModel";
    //发消息给Model
    public Handler mModelHandler;
    private ArrayList<Novels> mData = new ArrayList<Novels>();

    public final static int LOAD_CHAPTER_CONTENT = 1000;

    private Context context;
    String[] url = new String[12];
    String[] kind = new String[12];

    public ReaderModel(String name, Context context) {
        super(name);
        this.context = context;
    }

    @Override
    public void startModel() {
        super.startModel();
        mModelHandler = new Handler(this.getLooper(),new ReaderModelCallBack());
        url = context.getResources().getStringArray(R.array.sort_urls);
        kind = context.getResources().getStringArray(R.array.sort_novel_kind);

    }

    @Override
    public void stopModel() {
        super.stopModel();
    }


    public void getChapterContent(final Novel novel, final Chapter chapter, final OnChapterContentListener listener) {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                String path = null;
                try {
                    path = NovelFileUtils.saveChapter(novel.getName(), novel.getAuthor(), chapter.getTitle(),
                            DataQueryManager.instance().getChapterContent(chapter.getUrl()));
                    chapter.setContentPath(path);
                } catch (ParserException e) {
                    e.printStackTrace();
                }
                // try {
                // path = FileUtil.saveChapter("s", "s", "s", "ds");
                // System.out.println("ddd");
                // } catch (IOException e) {
                // // TODO Auto-generated catch block
                // e.printStackTrace();
                // }
                return path;
            }

            @Override
            protected void onPostExecute(String result) {
                listener.onFile(result);
            }

        }.execute();
    }

    private class ReaderModelCallBack implements Handler.Callback{
        @Override
        public boolean handleMessage(Message message) {
            int what = message.what;
            switch (what){
                case LOAD_CHAPTER_CONTENT:
                    final int id = message.arg1;
                    getChapterContent(NovelManager.getInstance().getCurrentNovel(), NovelManager.getInstance().getChapter(id), new OnChapterContentListener() {
                        @Override
                        public void onFile(String path) {
                            Message msg = new Message();
                            PresenterEventMessage eventMessage = new PresenterEventMessage();
                            PresenterEventMessage.ChapterEvent event = new PresenterEventMessage.ChapterEvent();
                            if (TextUtils.isEmpty(path)) {
                                event.setResult(0);
                                event.setChapterId(id);
                            } else {
                                event.setResult(1);
                                event.setChapterId(id);
                            }
                            msg.obj = eventMessage;
                            postModelMsgToPresenter(msg);
                        }
                    });
                    break;
                default:
                    break;
            
            }
            return false;
        }
    }
}
