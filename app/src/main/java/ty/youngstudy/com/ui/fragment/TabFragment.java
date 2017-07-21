package ty.youngstudy.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ty.youngstudy.com.R;
import ty.youngstudy.com.ui.fragment.base.BaseFragment;

/**
 * Created by edz on 2017/7/21.
 */


public class TabFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment,null);
        return view;
    }

    public static TabFragment newInstance(int index){
        Bundle extra = new Bundle();
        extra.putInt("index",'A' + index);
        TabFragment tabFragment = new TabFragment();
        return tabFragment;
    }
}
