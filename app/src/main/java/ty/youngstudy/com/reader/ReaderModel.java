package ty.youngstudy.com.reader;

import android.os.Handler;
import android.os.Message;

import ty.youngstudy.com.BaseModel;

/**
 * Created by edz on 2017/8/10.
 */

public class ReaderModel extends BaseModel {

    public Handler mModelHandler;

    public ReaderModel(String name) {
        super(name);
    }

    @Override
    public void startModel() {
        super.startModel();
        mModelHandler = new Handler(this.getLooper(),new ReaderModelCallBack());
    }

    @Override
    public void stopModel() {
        super.stopModel();
    }



    private class ReaderModelCallBack implements Handler.Callback{
        @Override
        public boolean handleMessage(Message message) {
            return false;
        }
    }
}
