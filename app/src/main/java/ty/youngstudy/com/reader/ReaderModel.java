package ty.youngstudy.com.reader;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;

import ty.youngstudy.com.BaseModel;
import ty.youngstudy.com.R;
import ty.youngstudy.com.bean.Novels;

/**
 * Created by edz on 2017/8/10.
 */

public class ReaderModel extends BaseModel {

    public final static String NAME = "ReaderModel";
    //发消息给Model
    public Handler mModelHandler;
    private ArrayList<Novels> mData = new ArrayList<Novels>();
    private final static int GET_NOVEL_TOTLE_INFO = 1000;
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
        mModelHandler.sendEmptyMessage(GET_NOVEL_TOTLE_INFO);
        url = context.getResources().getStringArray(R.array.sort_urls);
        kind = context.getResources().getStringArray(R.array.sort_novel_kind);

    }

    @Override
    public void stopModel() {
        super.stopModel();
    }



    private class ReaderModelCallBack implements Handler.Callback{
        @Override
        public boolean handleMessage(Message message) {
            int what = message.what;
            switch (what){
                case GET_NOVEL_TOTLE_INFO:
                    //DataQueryManager.instance().loadNovelFromUrl(subject,url,kind);
                    break;
                default:
                    break;
            
            }
            return false;
        }
    }
}
