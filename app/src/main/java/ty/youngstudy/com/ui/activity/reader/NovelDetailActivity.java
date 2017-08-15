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
import java.util.List;

import butterknife.BindView;
import ty.youngstudy.com.BaseModel;
import ty.youngstudy.com.ModelManager;
import ty.youngstudy.com.R;
import ty.youngstudy.com.adapter.NovelListAdapter;
import ty.youngstudy.com.bean.Novel;
import ty.youngstudy.com.mvp.ViewEventMessage;

import ty.youngstudy.com.reader.ReaderModel;
import ty.youngstudy.com.ui.activity.base.BaseActivity;

/**
 * Created by Administrator on 2017/8/12.
 */

public class NovelDetailActivity extends BaseActivity {

    private final static String TAG = "NovelDetailActivity";
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
