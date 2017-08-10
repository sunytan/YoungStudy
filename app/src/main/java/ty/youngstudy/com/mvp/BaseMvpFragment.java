package ty.youngstudy.com.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by edz on 2017/8/8.
 */

public abstract class BaseMvpFragment<PresenterType extends Presenter> extends Fragment {

    private PresenterType mPresenter;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = PresenterBuilder.fromViewClass(this.getClass());
        mPresenter.onCreate(view,savedInstanceState);
    }
}
