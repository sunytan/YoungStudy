package ty.youngstudy.com.reader;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ty.youngstudy.com.mvp.BaseMvpActivity;
import ty.youngstudy.com.mvp.Presenter;
import ty.youngstudy.com.mvp.ViewEventMessage;

/**
 * Created by Administrator on 2017/8/12.
 */

public class NovelDetailPresenyter extends Presenter<BaseMvpActivity,ReaderModel> {

    private ReaderModel readerModel;

    @Override
    public void onCreate(@NonNull BaseMvpActivity view, Bundle saveInstance) {
        super.onCreate(view, saveInstance);
        readerModel = this.getModel();
    }

    @Override
    public ReaderModel getModel() {
        return super.getModel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public BaseMvpActivity getView() {
        return super.getView();
    }

    @Override
    public void handleMsg(Message message) {

    }

    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ViewEventMessage eventMessage) {

    }
}
