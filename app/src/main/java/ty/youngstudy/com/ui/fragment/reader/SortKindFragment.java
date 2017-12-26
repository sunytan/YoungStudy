
package ty.youngstudy.com.ui.fragment.reader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import ty.youngstudy.com.R;
import ty.youngstudy.com.adapter.SortKindAdapter;
import ty.youngstudy.com.bean.Novels;
import ty.youngstudy.com.reader.NovelTotleInfo;
import ty.youngstudy.com.reader.manager.DataQueryManager;
import ty.youngstudy.com.ui.activity.reader.OneKindNovelActivity;
import ty.youngstudy.com.ui.fragment.base.BaseListFragment;

/**
 * Created by edz on 2017/8/1.
 */

public class SortKindFragment extends BaseListFragment {

    private final static String TAG = "SortKindFragment";
    private Activity mActivity;
    private SortKindAdapter novelListAdapter;
    public PublishSubject<Novels> subject;
    private ArrayList<Novels> mData = new ArrayList<Novels>();
    private ArrayList<Novels> mCacheData = new ArrayList<Novels>();
    private String[] kind = new String[12];
    private static ListView myView;
    String[] url = new String[12];
    public Handler handler;

    private static LruCache<String,ArrayList> cache = new LruCache<>(20);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        handler = new Handler();
        Message message = new Message();
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        subject = PublishSubject.create();
        subject.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<Novels>());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("data", mData);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.i("tanyang", "onViewCreated");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.i("tanyang", "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        Log.i("tanyang", "bundle = " + savedInstanceState);
        ArrayList<Novels> result = null;
        url = getResources().getStringArray(R.array.sort_urls);
        kind = getResources().getStringArray(R.array.sort_novel_kind);
        if (savedInstanceState != null) {
            result = savedInstanceState.getParcelableArrayList("data");
        }
        if (result != null && result.size() > 0) {
            mData = result;
        } else {
            mCacheData = cache.get("novel");
            if (mCacheData == null || mCacheData.size() == 0) {
                mSwipeRefresh.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        mSwipeRefresh.setRefreshing(true);
                    }
                });
                // 加载页面数据
                DataQueryManager.instance().loadNovelFromUrl(subject, url, kind);
            }else {
                mData.clear();
                mData.addAll(mCacheData);
            }
        }
        novelListAdapter = new SortKindAdapter(mActivity, mData);
        setListAdapter(novelListAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //刷新过程中不可以点击Item
        if (NovelTotleInfo.getInstance().getUpdate()) return;

        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(getActivity(), OneKindNovelActivity.class);
        Bundle bundle = new Bundle();
        Novels data = mData.get(position);
        if (data.getNovels() == null || data.getNovels().size() <= 0) {
            Toast.makeText(getActivity(),"此类小说暂无资源",Toast.LENGTH_LONG).show();
            return;
        }
        bundle.putParcelable("novels",mData.get(position));
        //bundle.putParcelableArrayList("novel", mData.get(position));
        bundle.putString("currentUrl", mData.get(position).getCurrentUrl());
        bundle.putString("nextUrl", mData.get(position).getNextUrl());
        intent.putExtra("data", bundle);
        startActivity(intent);
    }

    @Override
    protected void pullRefreshData() {
        super.pullRefreshData();
        NovelTotleInfo.getInstance().setUpdate(true);
        mData.clear();
        novelListAdapter.notifyDataSetChanged();
        subject = PublishSubject.create();
        subject.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<Novels>());
        DataQueryManager.instance().loadNovelFromUrl(subject,url,kind);
    }

    class MyObserver<T> implements Observer<Novels> {
        private Disposable disposable;
        @Override
        public void onSubscribe(Disposable d) {
            disposable = d;
        }

        @Override
        public void onNext(Novels value) {
            Log.i("tanyang","onNext");
            mData.add(value);
            novelListAdapter.notifyDataSetChanged();
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {
            Log.i("tanyang","onComplete");
            mSwipeRefresh.setRefreshing(false);
            cache.put("novel",mData);
            NovelTotleInfo.getInstance().setUpdate(false);
            disposable.dispose();
        }
    };
}
