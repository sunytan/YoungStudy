package ty.youngstudy.com.reader;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import org.htmlparser.util.ParserException;

import ty.youngstudy.com.BaseModel;
import ty.youngstudy.com.bean.Novel;

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
                    Novel novel = (Novel) message.obj;
                    try {
                        NovelDetail novelDetail = DataQueryManager.instance().getNovelDetail(novel.getUrl());
                        Message msg = new Message();
                        msg.obj = novelDetail;
                        postModelMsgToPresenter(msg);
                    } catch (ParserException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;

            }
            return false;
        }
    }

}
