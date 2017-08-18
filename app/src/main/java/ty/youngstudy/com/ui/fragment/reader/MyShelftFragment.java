package ty.youngstudy.com.ui.fragment.reader;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ty.youngstudy.com.adapter.BookItemAdapter;
import ty.youngstudy.com.bean.Novel;
import ty.youngstudy.com.ui.activity.reader.OneKindNovelActivity;
import ty.youngstudy.com.ui.fragment.base.BaseListFragment;

/**
 * Created by edz on 2017/7/25.
 */

public class MyShelftFragment extends BaseListFragment {
private BookItemAdapter bookItemAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        bookItemAdapter = new BookItemAdapter(LayoutInflater.from(getActivity()));
        setListAdapter(bookItemAdapter);
        Novel novel = new Novel();
        novel.setId(0);
        novel.setName("111111");
        Novel novel1 = new Novel();
        novel1.setId(1);
        novel1.setName("22222");
        List<Novel> novels = new ArrayList<>();
        novels.add(novel);
        novels.add(novel1);
        bookItemAdapter.changeData(novels);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        startActivity(new Intent(getActivity(), OneKindNovelActivity.class));
    }

    //    @Override
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(ViewEventMessage eventMessage) {
//        if (eventMessage == null) {
//            return;
//        }
//    }
    
}
