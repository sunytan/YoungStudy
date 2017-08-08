package ty.youngstudy.com.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import ty.youngstudy.com.bean.Novel;

/**
 * Created by edz on 2017/8/1.
 */

public class NovelListAdapter extends BaseAdapter {

    private List<Novel> novelList;

    @Override
    public int getCount() {
        return novelList.size();
    }

    @Override
    public Object getItem(int i) {
        return novelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
