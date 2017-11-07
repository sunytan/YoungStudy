package ty.youngstudy.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ty.youngstudy.com.R;
import ty.youngstudy.com.ui.fragment.base.BaseFragment;

/**
 * Created by edz on 2017/7/21.
 */


public class TabFragment extends BaseFragment {

    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment,null);
        textView = (TextView) view.findViewById(R.id.tab_frg_text_id);
        textView.setText(String.valueOf((char) getArguments().getInt("index")));
        return view;
    }

    public static Fragment newInstance(int index){
        Bundle extra = new Bundle();
        extra.putInt("index",'A' + index);
        Fragment tabFragment = new TabFragment();
        tabFragment.setArguments(extra);
        return tabFragment;
    }

    @Override
    public void setTargetFragment(Fragment fragment, int requestCode) {
        super.setTargetFragment(fragment, requestCode);
    }
}
