package ty.youngstudy.com.reader;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;

import ty.youngstudy.com.mvp.BaseMvpActivity;
import ty.youngstudy.com.mvp.Presenter;
import ty.youngstudy.com.mvp.ViewEventMessage;

/**
 * Created by edz on 2017/8/10.
 */

public class ReaderPresenter extends Presenter<BaseMvpActivity,ReaderModel> {

    private ReaderModel readerModel;


    @Override
    public void onCreate(@NonNull BaseMvpActivity view, Bundle saveInstance) {
        super.onCreate(view, saveInstance);
        readerModel = this.getModel();
    }

    @Override
    public void handleMsg(Message message) {
        //readerModel.mModelHandler.sendMessage(message);
    }

    @Override
    public void onEvent(ViewEventMessage eventMessage) {

    }
}
