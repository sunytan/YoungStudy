package com.young.study.reader.message;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import com.young.study.bean.Novel;
import com.young.study.mvp.BaseMvpActivity;
import com.young.study.mvp.Presenter;
import com.young.study.mvp.PresenterEventMessage;
import com.young.study.mvp.ViewEventMessage;
import com.young.study.reader.NovelDetail;
import com.young.study.reader.NovelInfoModel;
import com.young.study.reader.ReaderMessage;

/**
 * Created by edz on 2017/8/21.
 */

public class NovelInfoPresenter extends Presenter<BaseMvpActivity,NovelInfoModel> {

    private final static String TAG = "NovelInfoModel";
    private NovelInfoModel infoModel;


    @Override
    public void onCreate(@NonNull BaseMvpActivity view, Bundle saveInstance) {
        super.onCreate(view, saveInstance);
        infoModel = this.getModel();
    }

    @Override
    public void handleMsg(Message message) {
        if (message.obj != null) {
            NovelDetail novelDetail = (NovelDetail) message.obj;
            PresenterEventMessage eventMessage = new PresenterEventMessage();
            eventMessage.setNovelDetail(novelDetail);
            postPresenterMsgToView(eventMessage);
        }
    }

    @Override
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEvent(ViewEventMessage eventMessage) {
        switch (eventMessage.getEventType()) {
            case "get_detail_info":
                Novel novel = eventMessage.getNovel();
                Message msg = infoModel.mModelHandler.obtainMessage(ReaderMessage.GET_DETAIL_INFO);
                msg.obj = novel;
                infoModel.mModelHandler.sendMessage(msg);
                break;
            default:

        }
    }
}
