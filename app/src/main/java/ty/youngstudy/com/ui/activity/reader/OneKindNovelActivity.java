package ty.youngstudy.com.ui.activity.reader;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import ty.youngstudy.com.R;
import ty.youngstudy.com.adapter.NovelListAdapter;
import ty.youngstudy.com.bean.Novel;
import ty.youngstudy.com.mvp.BaseMvpActivity;
import ty.youngstudy.com.mvp.RequirePresenter;
import ty.youngstudy.com.mvp.ViewEventMessage;
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
    private NovelListAdapter novelListAdapter;
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
        novelListAdapter = new NovelListAdapter(this,listNovel);
        listView.setAdapter(novelListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.novel_onekind_layout);
        Bundle bundle = getIntent().getBundleExtra("data");
        if (bundle != null) {
            ArrayList<Novel> result = bundle.getParcelableArrayList("novel");
            listNovel.addAll(result);
            Log.i("tanyang","bundle = "+listNovel.get(0).getName());
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
