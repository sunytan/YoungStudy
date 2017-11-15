package ty.youngstudy.com.ui.activity.reader;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import org.htmlparser.util.ParserException;

import java.util.ArrayList;

import butterknife.BindView;
import io.reactivex.subjects.PublishSubject;
import ty.youngstudy.com.R;
import ty.youngstudy.com.adapter.NovelListAdapter;
import ty.youngstudy.com.bean.Novel;
import ty.youngstudy.com.bean.Novels;
import ty.youngstudy.com.reader.manager.DataQueryManager;
import ty.youngstudy.com.ui.activity.base.BaseActivity;

/**
 * Created by Administrator on 2017/8/12.
 */

public class OneKindNovelActivity extends BaseActivity {

    private final static String TAG = "OneKindNovelActivity";
    private ArrayList<Novel> listNovel = new ArrayList<Novel>();
    private ListView listView;
    private Toolbar toolbar;
    private String nextUrl;
    private String currentUrl;
    private SwipeRefreshLayout fresh;
    private NovelListAdapter novelListAdapter;

    private boolean isReloadMore;
    private boolean reLoading;

    @BindView(R.id.fresh_onekind_id)
    SwipeRefreshLayout mSwipeRefresh;

    @Override
    public View getLoadingView() {
        return null;
    }

    @Override
    public boolean getFirstStart() {
        return false;
    }

    @Override
    public void initViewAndEvents() {
        toolbar = (Toolbar) findViewById(R.id.novel_bar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        listView = (ListView) findViewById(R.id.list_onekind);
        fresh = (SwipeRefreshLayout) findViewById(R.id.fresh_onekind_id);
        fresh.setSize(SwipeRefreshLayout.DEFAULT);
        fresh.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_blue_light,
                android.R.color.holo_green_light);
        fresh.setBackgroundColor(Color.WHITE);
        fresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //pullRefreshdata();
            }
        });
        novelListAdapter = new NovelListAdapter(this,listNovel);
        listView.setAdapter(novelListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("novel",listNovel.get(i));
                readyGo(NovelDetailActivity.class,bundle);
            }
        });
        setScrollChangeListener(listView);

    }

    private void pullRefreshdata(){
        fresh.setRefreshing(true);
    }

    private void loadOneKindNovel(final PublishSubject subject,final String url){
        new AsyncTask<Void,Void,Novels>(){
            @Override
            protected Novels doInBackground(Void... voids) {
                Novels data = null;
                int i = 0;
                while (i < 3) {
                    try {
                        data = DataQueryManager.instance().getSortKindNovels(url);
                        break;
                    } catch (ParserException e) {
                        e.printStackTrace();
                        i ++;
                    }
                }
                return data;
            }

            @Override
            protected void onPostExecute(Novels novel) {
                super.onPostExecute(novel);
                if (novel == null)
                    Log.i("tanyang","load reslt is null");
                fresh.setRefreshing(false);
                listNovel.addAll(novel.getNovels());
                novelListAdapter.notifyDataSetChanged();
                nextUrl = novel.getNextUrl();
                currentUrl = novel.getCurrentUrl();

                reLoading = false;
                isReloadMore = false;
            }
        }.execute();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void setScrollChangeListener(@NonNull ListView list){
        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (isReloadMore && !reLoading){
                    reLoading = true;
                    reloadMore();
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firtVisibleItem, int visibleItemCount, int totleItemCount) {
                if (reLoading)
                    return;
                if (firtVisibleItem + visibleItemCount >= totleItemCount && totleItemCount > visibleItemCount){
                    isReloadMore = true;
                }
            }
        });
    }

    private void reloadMore(){
        if (nextUrl != null){
            loadOneKindNovel(null,nextUrl);
        } else {
            reLoading = false;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.novel_onekind_layout);
        Bundle bundle = getIntent().getBundleExtra("data");
        if (bundle != null) {
            Novels result = bundle.getParcelable("novels");
            listNovel.addAll(result.getNovels());
            currentUrl = bundle.getString("currentUrl","");
            nextUrl = bundle.getString("nextUrl","");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

}
