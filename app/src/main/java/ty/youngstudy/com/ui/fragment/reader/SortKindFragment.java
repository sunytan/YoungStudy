package ty.youngstudy.com.ui.fragment.reader;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.htmlparser.util.ParserException;

import java.util.ArrayList;
import java.util.List;

import ty.youngstudy.com.R;
import ty.youngstudy.com.adapter.SortKindAdapter;
import ty.youngstudy.com.bean.Novels;
import ty.youngstudy.com.reader.DataQueryManager;
import ty.youngstudy.com.ui.activity.reader.NovelDetailActivity;
import ty.youngstudy.com.ui.activity.reader.NovelMainActivity;
import ty.youngstudy.com.ui.fragment.base.BaseListFragment;


/**
 * Created by edz on 2017/8/1.
 */

public class SortKindFragment extends BaseListFragment {

    private final static String TAG = "SortKindFragment";
    private Activity mActivity;
    private SortKindAdapter novelListAdapter;

    private ArrayList<Novels> mData = new ArrayList<Novels>();

    private static ListView myView;
    String[] url = new String[12];

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
//        Bundle bundle = new Bundle();
//        bundle.putParcelableArrayList("data",mData);
//        setArguments(bundle);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("data",mData);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("tanyang","onViewCreated");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.i("tanyang","onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        Log.i("tanyang","bundle = "+savedInstanceState);
        ArrayList<Novels> result = null;
        url = getResources().getStringArray(R.array.sort_urls);
        if (savedInstanceState != null) {
            result = savedInstanceState.getParcelableArrayList("data");
        }
        if (result != null && result.size() > 0) {
            mData = result;
        } else {
            mSwipeRefresh.post(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    mSwipeRefresh.setRefreshing(true);
                }
            });
            //加载页面数据
            loadLastUpdataData(url);
        }
        novelListAdapter = new SortKindAdapter(mActivity,mData);
        setListAdapter(novelListAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(getActivity(),NovelDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("novel",mData.get(position).getNovels());
        bundle.putString("url",mData.get(position).getCurrentUrl());
        intent.putExtra("data",bundle);
        startActivity(intent);
    }

    @Override
    protected void pullRefreshData() {
        super.pullRefreshData();

    }

    private void loadLastUpdataData(final String[] url){

        final String[] kind = getResources().getStringArray(R.array.sort_novel_kind);
//        for (int i = 0; i < kind.length; i++) {
//            Novels novels = new Novels();
//            novels.setKindName(kind[i]);
//            mData.add(novels);
//            novelListAdapter.notifyDataSetChanged();
//            mSwipeRefresh.setRefreshing(false);
//        }

        new AsyncTask<Void,Void,ArrayList<Novels>>(){
            @Override
            protected ArrayList<Novels> doInBackground(Void... voids) {
                ArrayList<Novels> data = new ArrayList<Novels>();
                int i = 0;
                while (i < 3) {
                    try {
                        //后台获取小说的种类
                        for (int j = 0; j < url.length; j++) {
                            data.add(DataQueryManager.instance().getSortKindNovels(url[j]));
                            data.get(j).setCurrentUrl(url[j]);
                            data.get(j).setKindName(kind[j]);
                        }
//                        data = new Novels();
//                        Novel novel = new Novel();
//                        List<Novel> novel111 = new ArrayList<Novel>();
//                        novel.setName("11111");
//                        novel.setKind("玄幻");
//                        novel111.add(novel);
//                        data.setNovels(novel111);
//                        data.setKindName(novel.getKind());
//                        DataQueryManager.instance().getSortKindNovels(url);
                        break;
                    } catch (ParserException e) {
                        e.printStackTrace();
                        i ++;
                    }
                }
                return data;
            }

            @Override
            protected void onPostExecute(ArrayList<Novels> novels) {
                if(isDetached()){
                    return;
                }
                if (novels == null || novels.size() == 0 /*|| !novels.isIsok()*/){
                    Log.i(TAG,"novels data not ok");
                } else {
                    mSwipeRefresh.setRefreshing(false);

                    mData.addAll(novels);
                    novelListAdapter.notifyDataSetChanged();
                    //mNextUrl = novels.getNextUrl();
                }
                isReloadMore = false;
                isLoading = false;
            }
        }.execute();
    }
}
