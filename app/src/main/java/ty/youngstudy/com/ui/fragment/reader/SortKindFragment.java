package ty.youngstudy.com.ui.fragment.reader;

import android.app.Activity;
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
import ty.youngstudy.com.ui.fragment.base.BaseListFragment;


/**
 * Created by edz on 2017/8/1.
 */

public class SortKindFragment extends BaseListFragment {

    private final static String TAG = "SortKindFragment";
    private Activity mActivity;
    private SortKindAdapter novelListAdapter;

    private List<Novels> mData;

    private static ListView myView;

    //private String mNextUrl;

    public static SortKindFragment newInstance(String title) {
        SortKindFragment fragment = new SortKindFragment();
        Bundle bundle = new Bundle();
        //bundle.putString("title", title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public ListView getListView() {
        myView = super.getListView();
        return myView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (myView != null) {
            ViewGroup viewGroup = (ViewGroup) myView.getParent();
            if (viewGroup != null) viewGroup.removeView(myView);
            return myView;
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        mData = new ArrayList<Novels>();
        novelListAdapter = new SortKindAdapter(mActivity,mData);
        setListAdapter(novelListAdapter);

        if(bundle != null) {
            List<Novels> result = bundle.getParcelableArrayList("data");
            if(result != null && result.size() > 0) {
                mData.addAll(result);
                System.out.println("use bundle");
                return;
            }
        }
        mSwipeRefresh.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                mSwipeRefresh.setRefreshing(true);
            }
        });

        String[] url = getResources().getStringArray(R.array.sort_urls);//bundle.getString("url");
        //加载页面数据
        loadLastUpdataData(url);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

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

        new AsyncTask<Void,Void,List<Novels>>(){
            @Override
            protected List<Novels> doInBackground(Void... voids) {
                List<Novels> data = new ArrayList<Novels>();
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
            protected void onPostExecute(List<Novels> novels) {
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
