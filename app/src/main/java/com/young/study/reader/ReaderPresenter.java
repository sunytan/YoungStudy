package com.young.study.reader;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import com.young.study.mvp.BaseMvpActivity;
import com.young.study.mvp.Presenter;
import com.young.study.mvp.PresenterEventMessage;
import com.young.study.mvp.ViewEventMessage;

import static com.young.study.reader.ReaderModel.LOAD_CHAPTER_CONTENT;

/**
 * Created by edz on 2017/8/10.
 */

public class ReaderPresenter extends Presenter<BaseMvpActivity,ReaderModel> {

    private final static String TAG = "ReaderModel";
    private ReaderModel readerModel;
    private PresenterEventMessage presenterEventMessage;


    @Override
    public void onCreate(@NonNull BaseMvpActivity view, Bundle saveInstance) {
        super.onCreate(view, saveInstance);
        readerModel = this.getModel();
    }

    @Override
    public void handleMsg(Message message) {
        presenterEventMessage = (PresenterEventMessage) message.obj;
        NovelTotleInfo novelTotleInfo = presenterEventMessage.getNovelTotleInfo();
        //发送消息实时更新界面信息
        if (novelTotleInfo != null) {
            //readerModel.mModelHandler.sendEmptyMessage(ReaderMessage.UPDATE_VIEW);
        }
        if (presenterEventMessage != null) {
            postPresenterMsgToView(presenterEventMessage);
        }
    }

    @Override
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEvent(ViewEventMessage eventMessage) {
        if (eventMessage != null) {
            switch (eventMessage.getEventType()) {
                case "load_chapter":
                    Message message = new Message();
                    message.what = LOAD_CHAPTER_CONTENT;
                    message.arg1 = eventMessage.getChapterId();
                    readerModel.mModelHandler.sendMessage(message);
                    break;
                default:
                    break;
            }
        }
    }
}
