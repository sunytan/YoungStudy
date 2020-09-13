package com.young.study.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import com.young.study.R;

/**
 * Created by edz on 2017/8/8.
 */

public abstract class BaseMvpFragment<PresenterType extends Presenter> extends Fragment {

    private PresenterType mPresenter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = PresenterBuilder.fromViewClass(this.getClass());
        mPresenter.onCreate(this,savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment_layout,null);
    }

    public abstract String getModelName();

    public final void postViewMsgToPresenter(ViewEventMessage eventMessage){
        EventBus.getDefault().post(eventMessage);
    }

    public abstract void onEvent(ViewEventMessage eventMessage);
}
