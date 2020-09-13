package com.young.study.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import com.young.study.bean.Novel;
import com.young.study.reader.DownLoadMessage;

/**
 * Created by edz on 2017/7/26.
 */

public class DownLoadText extends TextView implements View.OnClickListener
{
//    public DownLoadText(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        setOnClickListener(this);
//    }
//
//    public DownLoadText(Context context) {
//        this(context,null);
//    }

    public DownLoadText(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
        super(context,attrs);
        setOnClickListener(this);
    }

//    public DownLoadText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        this(context, attrs, defStyleAttr,0);
//    }


    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility != View.VISIBLE) {
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this);
            }
        } else {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
        }
    }


    @Subscribe(threadMode=ThreadMode.MAIN)
    public void onMsg(DownLoadMessage message){

    }

//    @Subscribe(threadMode=ThreadMode.MAIN)
//    public void onProgress(DownProgress progress){
//
//    }

    public void bintText(Novel novel){

    }

    @Override
    public void onClick(View view) {
        EventBus.getDefault().post(new DownLoadMessage());
    }
}
