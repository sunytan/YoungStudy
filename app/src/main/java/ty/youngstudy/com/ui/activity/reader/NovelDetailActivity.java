package ty.youngstudy.com.ui.activity.reader;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ty.youngstudy.com.R;
import ty.youngstudy.com.bean.Novel;
import ty.youngstudy.com.mvp.BaseMvpActivity;
import ty.youngstudy.com.mvp.PresenterEventMessage;
import ty.youngstudy.com.mvp.RequirePresenter;
import ty.youngstudy.com.mvp.ViewEventMessage;
import ty.youngstudy.com.reader.NovelDetail;
import ty.youngstudy.com.reader.NovelInfoModel;
import ty.youngstudy.com.reader.manager.NovelManager;
import ty.youngstudy.com.reader.message.NovelInfoPresenter;
import ty.youngstudy.com.widget.LoadingView;

/**
 * Created by edz on 2017/8/21.
 */

@RequirePresenter(NovelInfoPresenter.class)
public class NovelDetailActivity extends BaseMvpActivity<NovelInfoPresenter> implements View.OnClickListener {

    private static final String TAG = "NovelDetailActivity";

    private TextView mNovelName;
    private TextView mNovelDetail;
    private TextView mAuthor;
    private ImageView mThumb;
    private TextView mUpdateTime;
    private TextView mChapter;
    private Toolbar toolbar;

    private Button mStartRead;
    private Button mAddBookShelt;
    private Button mDownload;

    private View mViewChapters;

    private LoadingView mLoadView;
    private View mContentView;

    private int downloadStatus;

    private NovelDetail novelDetail;

    private Activity mActivity;

    private final int RETRY_COUNT = 3;


    @Override
    public void initViewAndEvents() {
        super.initViewAndEvents();
        toolbar = (Toolbar) findViewById(R.id.novel_bar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mNovelDetail = (TextView) findViewById(R.id.detail);
        mNovelName = (TextView) findViewById(R.id.name);
        mAuthor = (TextView) findViewById(R.id.author);
        mUpdateTime = (TextView) findViewById(R.id.update_time);
        mChapter = (TextView) findViewById(R.id.last_chapter);

        mThumb = (ImageView) findViewById(R.id.thumb);
        mStartRead = (Button) findViewById(R.id.read);
        mDownload = (Button) findViewById(R.id.download);
        mAddBookShelt = (Button) findViewById(R.id.addtoshelft);
        mStartRead.setOnClickListener(this);
        mDownload.setOnClickListener(this);
        mAddBookShelt.setOnClickListener(this);

        mViewChapters = findViewById(R.id.chapters);
        mViewChapters.setOnClickListener(this);

        mContentView = findViewById(R.id.content);
        mLoadView = (LoadingView) findViewById(R.id.loadView);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel_detail);
        mContentView.setVisibility(View.GONE);
        mLoadView.setVisibility(View.VISIBLE);
        Novel novel = getIntent().getParcelableExtra("novel");
        ViewEventMessage eventMessage = new ViewEventMessage();
        eventMessage.setNovel(novel);
        eventMessage.setEventType("get_detail_info");
        postViewMsgToPresenter(eventMessage);
    }


    public void showNovelInfo(@NonNull Novel novel){
        Log.d(TAG,"novel = "+novel.toString());
        mNovelName.setText(novel.getName());
        mAuthor.setText(novel.getAuthor());
        mChapter.setText(novel.getLastUpdateChapter());
        mUpdateTime.setText(novel.getLastUpdateTime());
        mNovelDetail.setText(novel.getBrief());
        Glide.with(this).load(novel.getThumb()).into(mThumb);
        if (isInBookShelft(novel.getId())){
            mAddBookShelt.setText(R.string.removeshelft);
        } else {
            mAddBookShelt.setText(R.string.addshelft);
        }
    }

    public boolean isInBookShelft(int bookId){

        return false;
    }

    @Override
    public String getModelName() {
        return NovelInfoModel.NAME;
    }

    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PresenterEventMessage presenterEventMessage) {
        if (presenterEventMessage != null) {
            novelDetail = presenterEventMessage.getNovelDetail();
            if (novelDetail != null) {
                Novel novel = novelDetail.getNovel();
                mLoadView.setVisibility(View.GONE);
                mContentView.setVisibility(View.VISIBLE);
                showNovelInfo(novel);
                NovelManager.getInstance().setCurrentNovel(novel);
                NovelManager.getInstance().setChapterList(novelDetail.getChapters());
                showNovelInfo(novel);
            }

        }
    }

    @Override
    public void onClick(View view) {
        if (view == mViewChapters) {
            readyGo(NovelChapterActivity.class);
        }
    }
}
