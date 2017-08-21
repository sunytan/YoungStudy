package ty.youngstudy.com.reader.message;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ty.youngstudy.com.mvp.BaseMvpActivity;
import ty.youngstudy.com.mvp.Presenter;
import ty.youngstudy.com.mvp.ViewEventMessage;
import ty.youngstudy.com.reader.NovelInfoModel;
import ty.youngstudy.com.reader.ReaderMessage;

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

    }

    @Override
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEvent(ViewEventMessage eventMessage) {
        switch (eventMessage.getEventType()) {
            case "get_detail_info":
                infoModel.mModelHandler.sendEmptyMessage(ReaderMessage.GET_DETAIL_INFO);
                break;
            default:

        }
    }
}
