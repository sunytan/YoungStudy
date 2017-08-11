package ty.youngstudy.com;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;


/**
 * Created by edz on 2017/8/10.
 */

public class BaseModel extends HandlerThread {
    private final static String TAG = "BaseModel";
    private Handler mModelHandler;

    public BaseModel(String name) {
        super(name);
    }

    public void startModel(){

    }

    public void stopModel(){

    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
    }

    public final void setModelHandler(Handler mModelHandler){
        this.mModelHandler = mModelHandler;
    }

    public final void postModelMsgToPresenter(Message msg){
        if (this.mModelHandler != null) {
            this.mModelHandler.sendMessage(msg);
        } else {
            Log.i(TAG,"ModelHandler is null");
        }
    }
}
