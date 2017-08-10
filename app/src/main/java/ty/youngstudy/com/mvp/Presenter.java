package ty.youngstudy.com.mvp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;

/**
 * Created by edz on 2017/8/8.
 */

public abstract class Presenter<ViewType,ModelType> {
    private ViewType view;
    private String name;
    private ModelType modelType;
    private ModelMsgHandler handler;


    public void onCreate(@NonNull ViewType view, Bundle saveInstance){
        this.view = view;
        this.name = ((BaseMvpActivity)view).getModelName();
        handler = new ModelMsgHandler(((BaseMvpActivity) view).getMainLooper());
    }



    /**
     * 处理Model返回给Presenter的消息
     */
    public final class ModelMsgHandler extends Handler {

        public ModelMsgHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            handleMsg(msg);
        }
    }

    public abstract void handleMsg(Message message);
}
