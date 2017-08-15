package ty.youngstudy.com.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import ty.youngstudy.com.ui.activity.base.BaseActivity;

/**
 * Created by edz on 2017/8/8.
 */

public abstract class BaseMvpActivity<PresenterType extends Presenter> extends BaseActivity {

    private PresenterType mPresenter;

    @Override
    public View getLoadingView() {
        return null;
    }

    @Override
    public boolean getFirstStart() {
        return false;
    }

    @Override
    public void initViewAndEvents() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = PresenterBuilder.fromViewClass(this.getClass());
        mPresenter.onCreate(this,savedInstanceState);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter == null) {
            mPresenter.onDestroy();
        }
        EventBus.getDefault().unregister(this);
    }

    public abstract String getModelName();

    public abstract void onEvent(ViewEventMessage eventMessage);

    public final void postViewMsgToPresenter(ViewEventMessage eventMessage){
        EventBus.getDefault().post(eventMessage);
    }
}
