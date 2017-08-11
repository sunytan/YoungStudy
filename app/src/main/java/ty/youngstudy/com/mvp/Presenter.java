package ty.youngstudy.com.mvp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;

import ty.youngstudy.com.BaseModel;
import ty.youngstudy.com.ModelManager;

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
        modelType = (ModelType)ModelManager.getInstance().getModel(name);
        ((BaseModel)modelType).setModelHandler(handler);
        EventBus.getDefault().register(this);
    }


    public void onDestroy(){
        EventBus.getDefault().unregister(this);
    }

    public ViewType getView() {
        return this.view;
    }

    public ModelType getModel(){
        return this.modelType;
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
            super.handleMessage(msg);
            handleMsg(msg);
        }
    }


    public abstract void handleMsg(Message message);

    /**
     * 处理View层传过来的消息
     * @param eventMessage
     */
    public abstract void onEvent(ViewEventMessage eventMessage);
    /**
     * 发送Presenter的消息给View层
     *
     * @param eventMessage 事件
     */
    public final void postPresenterMsgToView(PresenterEventMessage eventMessage){
        EventBus.getDefault().post(eventMessage);
    }
}
