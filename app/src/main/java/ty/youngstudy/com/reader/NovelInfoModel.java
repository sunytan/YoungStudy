package ty.youngstudy.com.reader;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import ty.youngstudy.com.BaseModel;

/**
 * Created by edz on 2017/8/21.
 */


public class NovelInfoModel extends BaseModel {

    public final static String NAME = "NovelInfoModel";

    public Handler mModelHandler;
    private Context mContext;

    public NovelInfoModel(String name, Context context) {
        super(name);
        mContext = context;
    }


    @Override
    public void startModel() {
        super.startModel();
        mModelHandler = new Handler(this.getLooper(),new NovelInfoCallback());

    }


    @Override
    public void stopModel() {
        super.stopModel();
    }

    @Override
    protected void waitModelReady() {
        super.waitModelReady();
    }


    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
    }

    private class NovelInfoCallback implements Handler.Callback{
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case ReaderMessage.GET_DETAIL_INFO:

                    break;
                default:
                    break;

            }
            return false;
        }
    }

}
