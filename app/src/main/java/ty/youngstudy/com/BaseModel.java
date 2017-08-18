package ty.youngstudy.com;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;


/**
 * Created by edz on 2017/8/10.
 */

public class BaseModel extends HandlerThread {
    private final static String TAG = "BaseModel";

    //消息回传给Presenter
    private Handler mModelMsgHandler;

    private final byte[] bytelock = new byte[0];
    private boolean loopHasRun = false;

    //用于消息发回给Model
    private Handler handler;
    public BaseModel(String name) {
        super(name);
    }

    public void startModel(){

    }

    public void stopModel(){

    }

    @Override
    public Looper getLooper() {
        return super.getLooper();
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        synchronized (bytelock){
            bytelock.notify();
            loopHasRun = true;
        }
    }

    protected void waitModelReady(){
        synchronized (bytelock){
            if (loopHasRun)
                return;
            try {
                bytelock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public final void setModelHandler(Handler mModelMsgHandler){
        this.mModelMsgHandler = mModelMsgHandler;
    }

    public final void postModelMsgToPresenter(Message msg){
        if (this.mModelMsgHandler != null) {
            this.mModelMsgHandler.sendMessage(msg);
        } else {
            Log.i(TAG,"ModelHandler is null");
        }
    }
}
