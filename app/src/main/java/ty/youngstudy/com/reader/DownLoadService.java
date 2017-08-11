package ty.youngstudy.com.reader;

import android.os.Handler;
import android.os.Message;

import ty.youngstudy.com.ui.widget.BaseService;

/**
 * Created by edz on 2017/8/10.
 */

public class DownLoadService extends BaseService {
    private Handler mServiceHandler;

    public DownLoadService(String name) {
        super(name);
    }

    @Override
    public void startService() {
        super.startService();
        mServiceHandler = new Handler(this.getLooper(),new DownLoadCallBack());
    }


    private class DownLoadCallBack implements Handler.Callback{
        @Override
        public boolean handleMessage(Message message) {
            return false;
        }
    }
}
