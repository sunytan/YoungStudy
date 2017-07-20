package ty.youngstudy.com.ui.activity.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import ty.youngstudy.com.ui.view.base.BaseView;

/**
 * Created by edz on 2017/7/20.
 */

public abstract class BaseActivity extends AppCompatActivity implements BaseView {
    public BaseActivity() {
        super();
    }

    @Override
    public void setTheme(@StyleRes int resid) {
        super.setTheme(resid);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    public abstract boolean getFirstStart();
}
