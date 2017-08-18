package ty.youngstudy.com.reader;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ty.youngstudy.com.mvp.BaseMvpActivity;
import ty.youngstudy.com.mvp.Presenter;
import ty.youngstudy.com.mvp.PresenterEventMessage;
import ty.youngstudy.com.mvp.ViewEventMessage;
import ty.youngstudy.com.ui.activity.reader.OneKindNovelActivity;

/**
 * Created by edz on 2017/8/10.
 */

public class ReaderPresenter extends Presenter<BaseMvpActivity,ReaderModel> {

    private final static String TAG = "ReaderModel";
    private ReaderModel readerModel;
    private OneKindNovelActivity oneKindNovelActivity;
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

    }
}
