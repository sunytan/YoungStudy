package ty.youngstudy.com.ui.activity.reader;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.htmlparser.util.ParserException;

import java.util.ArrayList;

import butterknife.BindView;
import io.reactivex.subjects.PublishSubject;
import ty.youngstudy.com.R;
import ty.youngstudy.com.adapter.NovelListAdapter;
import ty.youngstudy.com.bean.Novel;
import ty.youngstudy.com.bean.Novels;
import ty.youngstudy.com.mvp.BaseMvpActivity;
import ty.youngstudy.com.mvp.RequirePresenter;
import ty.youngstudy.com.mvp.ViewEventMessage;
import ty.youngstudy.com.reader.DataQueryManager;
import ty.youngstudy.com.reader.ReaderPresenter;

/**
 * Created by Administrator on 2017/8/12.
 */
@RequirePresenter(ReaderPresenter.class)
public class OneKindNovelActivity extends BaseMvpActivity<ReaderPresenter> {

    private final static String TAG = "OneKindNovelActivity";
    private final static String NAME = "ReaderModel";
    private ArrayList<Novel> listNovel = new ArrayList<Novel>();
    private ListView listView;
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
            //Log.i("tanyang","bundle = "+listNovel.get(0).getName());
            //listNovel = bundle.getParcelableArrayList("data");
        }
//        readerModel = new ReaderModel(TAG);
//        ModelManager.getInstance().add(readerModel);
//        readerModel.startModel();
    }

    @Override
    public String getModelName() {
        return NAME;
    }

    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ViewEventMessage eventMessage) {

    }

    @Override
    protected void onResume() {
        super.onResume();
//        Novel novel = new Novel();
//        novel.setName("11111");
//        novel.setAuthor("2222");
//        novel.setBrief("333333");
//        listNovel.add(novel);
        //novelListAdapter.notifyDataSetChanged();
    }

}
