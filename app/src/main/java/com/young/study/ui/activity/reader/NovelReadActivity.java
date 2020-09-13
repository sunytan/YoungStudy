package com.young.study.ui.activity.reader;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import com.young.study.DataHelper;
import com.young.study.R;
import com.young.study.mvp.BaseMvpActivity;
import com.young.study.mvp.PresenterEventMessage;
import com.young.study.mvp.RequirePresenter;
import com.young.study.mvp.ViewEventMessage;
import com.young.study.reader.Chapter;
import com.young.study.reader.OnReadStateChangeListener;
import com.young.study.reader.OverlappedWidget;
import com.young.study.reader.PageWidget;
import com.young.study.reader.ReaderPresenter;
import com.young.study.reader.constant.NovelConstant;
import com.young.study.reader.manager.BookShelftManager;
import com.young.study.reader.manager.NovelManager;
import com.young.study.reader.manager.SettingManager;
import com.young.study.reader.manager.ThemeManager;
import com.young.study.reader.view.BaseReadView;
import com.young.study.reader.view.CustomDialog;
import com.young.study.util.ScreenUtils;
import com.young.study.util.SharedPreferencesUtil;
import com.young.study.widget.LoadingView;


@RequirePresenter(ReaderPresenter.class)
public class NovelReadActivity extends BaseMvpActivity<ReaderPresenter> implements SeekBar.OnSeekBarChangeListener{

	private final static String NAME = "ReaderModel";
    private int chapterId = -1;


	@Override
	public String getModelName() {
		return NAME;
	}

	@Override
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEvent(PresenterEventMessage presenterEventMessage) {
        PresenterEventMessage.ChapterEvent chapterEvent = presenterEventMessage.getChapterEvent();
        if (chapterEvent != null) {
            int result = chapterEvent.getResult();
            if (result == 0) {
                hideLoading();
                onLoadFail(chapterId);
            } else {
                hideLoading();
                showChapterContent(chapterId);
            }
        }
	}

	private BaseReadView mPageWidget;
	private LoadingView mLoading;
	private ViewGroup flReadWidget;

	private IntentFilter intentFilter = new IntentFilter();
	private Receiver receiver = new Receiver();

	
	 private CustomDialog dialog;
	 
	 private static final String TAG = "NovelReadActivity";
	 
	 private int bookId = 0;
	 private boolean isInBookShelft;
	 
	 private View decodeView;
	 
	 private View topView;
	 private View buttomView;
	 
	 @BindView(R.id.progress)
	 SeekBar mBar;
	 @BindView(R.id.chapter)
	 TextView mChapterTextView;
	 
	 Handler mHandler = new Handler();
	 
	 public static final String CHAPTER_EXTRA = "chapter";
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
				&& Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			// 透明状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 透明导航栏
//			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		setContentView(R.layout.activity_chapter_content);

		bookId = NovelManager.getInstance().getCurrentNovel().getId();
		isInBookShelft = BookShelftManager.instance().isInbookShelft(bookId);
		if(!isInBookShelft) {
			findViewById(R.id.change_source).setVisibility(View.GONE);
		}
		
		Bundle bundle = getIntent().getExtras();
		if (bundle == null) {
			finish();
			return;
		}
        hideNavigationBar(this);
		flReadWidget = (ViewGroup) findViewById(R.id.page);
		topView = findViewById(R.id.topview);
		buttomView = findViewById(R.id.buttomview);
		mLoading = (LoadingView) findViewById(R.id.loadView);
		mLoading.setLoadingText("downloading");
		ThemeManager.setReaderTheme(SettingManager.getInstance().getReadTheme(), mLoading);
//		mPresenter.prepareChapterContent(NovelManager.getInstance().getChapterId());

		intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
		intentFilter.addAction(Intent.ACTION_TIME_TICK);

		if(NovelManager.getInstance().getChapterSize() == 0) {
			NovelManager.getInstance().setChapterList(DataHelper.queryNovelChapterList(bookId));
		}
		mBar.setMax(NovelManager.getInstance().getChapterSize() - 1);
		initPagerWidget(bookId +"");
		
		DataHelper.updateReadtime(bookId);
		
		decodeView = getWindow().getDecorView();
		
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) topView.getLayoutParams();
        params.topMargin = ScreenUtils.getStatusBarHeight(this) ;
        topView.setLayoutParams(params);
        
        TextView name = (TextView) findViewById(R.id.name);
        name.setText(NovelManager.getInstance().getCurrentNovel().getName());
        
        mBar.setOnSeekBarChangeListener(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void initPagerWidget(String bookid) {
		if (SharedPreferencesUtil.getInstance().getInt(NovelConstant.FLIP_STYLE, 0) == 0) {
			Log.v(TAG, "new widget");
			mPageWidget = new PageWidget(this, bookid, NovelManager.getInstance().getChapterList(), new ReadListener());
		} else {
			mPageWidget = new OverlappedWidget(this, bookid, NovelManager.getInstance().getChapterList(), new ReadListener());
		}
		if (SharedPreferencesUtil.getInstance().getBoolean(NovelConstant.IS_NIGHT, false)) {
			mPageWidget.setTextColor(ContextCompat.getColor(this, R.color.chapter_content_night),
					ContextCompat.getColor(this, R.color.chapter_title_night));
		}
		flReadWidget.removeAllViews();
		flReadWidget.addView(mPageWidget);
		int chapter = getIntent().getIntExtra(CHAPTER_EXTRA, -1);
		if(chapter > 0)
			mPageWidget.setCurrentChapter(chapter);
		mPageWidget.init(SettingManager.getInstance().getReadTheme());
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.v(TAG, "onNewIntent");
		mPageWidget.jumpToChapter(NovelManager.getInstance().getChapterId());
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		registerReceiver(receiver, intentFilter);
	}

	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(receiver);
	}
	
	@OnClick(R.id.catalog)
	public void showCatlog(View v) {
		System.out.println("showCatlog");
		readyGo(NovelChapterActivity.class);
		//NovelChapterListActivity.startChapterListActivity(this);
	}
	
	@OnClick(R.id.back)
	public void back(View v) {
		onBackPressed();
	}
	
	@OnClick(R.id.detail)
	public void viewDetail(View v) {
		Bundle bundle = new Bundle();
		bundle.putParcelable("novel",NovelManager.getInstance().getCurrentNovel());
		readyGo(NovelDetailActivity.class,bundle);
		//NovelDetailActivity.startDetailActivity(this, NovelManager.getInstance().getCurrentNovel());
	}
	
	@OnClick(R.id.next_chapter)
	public void nextChapter(View v) {
		mPageWidget.jumpToChapter(NovelManager.getInstance().getChapterId() + 1);
//		mPageWidget.openLoadedChapter(NovelManager.getInstance().getChapterId() + 1);
	}
	
	@OnClick(R.id.pre_chapter)
	public void preChapter(View v) {
//		mPageWidget.openLoadedChapter(NovelManager.getInstance().getChapterId() - 1);
		mPageWidget.jumpToChapter(NovelManager.getInstance().getChapterId() - 1);
	}
	
	@OnClick(R.id.setting)
	public void setting(View v) {
		
	}
	
	@OnClick(R.id.cache)
	public void download(View v) {
		/*DownloadService.addToDownload(new DownloadTask(NovelManager.getInstance().getCurrentNovel(), NovelManager.getInstance().getChapterId(), -1, true));
		Toast.makeText(this, R.string.begin_download, Toast.LENGTH_SHORT).show();*/
	}
	
	@OnClick(R.id.change_source)
	public void changeSource(View v) {
		//SourceActivity.startChangeSourceActivity(this);
	}

	public void showLoading() {
//		getDialog().show();
		flReadWidget.setVisibility(View.GONE);
		mLoading.setVisibility(View.VISIBLE);
	}

	public void hideLoading() {
//		getDialog().hide();
		flReadWidget.setVisibility(View.VISIBLE);
		mLoading.setVisibility(View.GONE);
	}

	public void showChapterContent(int c) {
		Log.v(TAG, "showCurrent::mPageWidget.isPrepared = " + mPageWidget.isPrepared);
		int curTheme = SettingManager.getInstance().getReadTheme();
		if (!mPageWidget.isPrepared) {
			mPageWidget.setCurrentChapter(c);
			mPageWidget.init(curTheme);
		} else {
//			mPageWidget.jumpToChapter(c);
			mPageWidget.openLoadedChapter(c);
		}
	}

	public void onLoadFail(int chapter) {
		
	}

	public void prepareNext(String path) {

	}

	public void preparaPrev(String path) {

	}

	private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    public void hideNavigationBar(Activity context) {
        int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN; // hide status bar

        if( android.os.Build.VERSION.SDK_INT >= 19 ){
            uiFlags |= 0x00001000;    //SYSTEM_UI_FLAG_IMMERSIVE_STICKY: hide navigation bars - compatibility: building API level is lower thatn 19, use magic number directly for higher API target level
        } else {
            uiFlags |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }
        context.getWindow().getDecorView().setSystemUiVisibility(uiFlags);
    }

	class Receiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (mPageWidget != null) {
				if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
					int level = intent.getIntExtra("level", 0);
					mPageWidget.setBattery(100 - level);
				} else if (Intent.ACTION_TIME_TICK.equals(intent.getAction())) {
					mPageWidget.setTime(sdf.format(new Date()));
				}
			}
		}
	}

    private void sendViewMsg2Presenter(int chapter_id,String eventType){
        chapterId = chapter_id;
        ViewEventMessage eventMessage = new ViewEventMessage();
        eventMessage.setChapterId(chapterId);
        eventMessage.setEventType(eventType);
        postViewMsgToPresenter(eventMessage);
    }

	class ReadListener implements OnReadStateChangeListener {

		@Override
		public void onChapterChanged(int chapter) {
			mBar.setProgress(chapter - 1); //here crash once with null pointer
			System.out.println("onChapterChanged chapter =" + chapter);
			NovelManager.getInstance().setChapterId(chapter);
			//DownloadService.addToDownload(new DownloadTask(NovelManager.getInstance().getCurrentNovel(), chapter, 5));
			if(isInBookShelft) {
				DataHelper.setCurrentReadChapterPosition(bookId, chapter);
			}
		}

		@Override
		public void onPageChanged(int chapter, int page) {
			System.out.println("page=" + page);
		}

		@Override
		public void onLoadChapterFailure(int chapter) {
			System.out.println("onLoadChapterFailure chapter="+chapter);
			//mPresenter.prepareChapterContent(chapter);
            showLoading();
            sendViewMsg2Presenter(chapter,"load_chapter");

		}

		@Override
		public void onCenterClick() {
			System.out.println("onCenterClick");
			toggleReadBar();
		}

		@Override
		public void onFlip() {

		}

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_MENU:
			toggleReadBar();
			break;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
    public CustomDialog getDialog() {
        if (dialog == null) {
            dialog = CustomDialog.instance(this);
            dialog.setCancelable(true);
            dialog.setOnCancelListener(new OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					finish();
				}
			});
        }
        return dialog;
    }
    
    public void hideDialog() {
        if (dialog != null)
            dialog.hide();
    }

    public void showDialog() {
        getDialog().show();
    }

    public void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
//			Util.hideNavigationBar(this);
			hideReadBar();
		}
	}
	
    private synchronized void toggleReadBar() { // 切换工具栏 隐藏/显示 状态
        if (isVisible(topView)) {
            hideReadBar();
        } else {
            showReadBar();
        }
    }
    
    private synchronized void hideReadBar() {
    	gone(topView,buttomView);
//        hideStatusBar();
//        decodeView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
    	hideNavigationBar(this);
    }

    private synchronized void showReadBar() {
    	visible(topView,buttomView);
        showStatusBar();
        decodeView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        
    }
    
    protected void hideStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);

    }

    protected void showStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);

    }

    protected void gone(final View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.GONE);
                }
            }
        }
    }

    protected void visible(final View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.VISIBLE);
                }
            }
        }

    }
    
    protected boolean isVisible(View view) {
        return view.getVisibility() == View.VISIBLE;
    }
		
	
	public static void startNovelReadActivity(Context context,int chapter) {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putInt(CHAPTER_EXTRA, chapter);
		intent.putExtras(bundle);
		intent.setClass(context, NovelReadActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		mHandler.removeMessages(0);
		Chapter chapter = NovelManager.getInstance().getChapter(progress + 1);
		mChapterTextView.setText(chapter.getTitle());
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		mChapterTextView.setVisibility(View.VISIBLE);
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				mChapterTextView.setVisibility(View.GONE);
			}
		}, 1000);
		mPageWidget.jumpToChapter(seekBar.getProgress() + 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK) {
//			initPagerWidget(bookId + "");
			mPageWidget.jumpToChapter(NovelManager.getInstance().getChapterId());
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
}
