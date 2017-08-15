package ty.youngstudy.com.ui.fragment.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import ty.youngstudy.com.R;

/**
 * Created by edz on 2017/7/25.
 */

public abstract class BaseListFragment extends ListFragment {

    private View rootView;
    protected SwipeRefreshLayout mSwipeRefresh;

    protected boolean isReloadMore;
    protected boolean isLoading;

    public BaseListFragment(){
        setArguments(new Bundle());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView != null) {
            return rootView;
        }
        rootView = inflater.inflate(R.layout.list_fragment_layout,null);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.fresh_id);
        mSwipeRefresh.setSize(SwipeRefreshLayout.DEFAULT);
        mSwipeRefresh.setColorSchemeResources(android.R.color.holo_red_light,android.R.color.holo_green_light,android.R.color.holo_blue_light);
        mSwipeRefresh.setBackgroundColor(/*getResources().getColor(R.color.swipe_backgroud_color)*/Color.WHITE);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                Log.i("tanyang","onRefresh");
                pullRefreshData();
            }
        });

        getListView().setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.i("tanyang","onScrollStateChanged");
//				if(scrollState == SCROLL_STATE_IDLE) {
                if(isReloadMore && !isLoading) {
                    isLoading = true;
                    reloadMore();
                }
//				}
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.i("tanyang","onScroll");
//				Log.v("tanyang", "firstVisibleItem = " + firstVisibleItem + ",visibleItemCount = " + visibleItemCount + ",totalItemCount = " + totalItemCount);

                if(isReloadMore)
                    return;
                if(firstVisibleItem + visibleItemCount >= totalItemCount - getReloadSpace() && totalItemCount > visibleItemCount) {
                    isReloadMore = true;
                }
            }

        });
    }

    protected void pullRefreshData() {
        mSwipeRefresh.setRefreshing(true);
    }

    protected void reloadMore() {
        isReloadMore = false;
    }

    protected int getReloadSpace() {
        return 0;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
