package com.young.study.ui.view.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by edz on 2017/7/24.
 */

public class CustomVideoView extends VideoView {

    public CustomVideoView(Context context) {
        this(context, null);
    }


    public CustomVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }
}
