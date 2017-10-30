
package ty.youngstudy.com.ui.activity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.litepal.LitePal;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import butterknife.OnClick;
import ty.youngstudy.com.R;
import ty.youngstudy.com.ui.activity.base.BaseActivity;
import ty.youngstudy.com.ui.view.layout.BalloonRelativeLayout;
import ty.youngstudy.com.ui.view.layout.CustomVideoView;

public class LoginActivity extends BaseActivity
        implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private ArrayList<LoginInfo> loginAccount = new ArrayList<LoginInfo>();

    @OnClick(R.id.btn_login_id)
    public void login(){
        readyGoThenKill(MainActivity.class);
    }
    @OnClick(R.id.tv_registry_id)
    public void register(){
        readyGo(RegisterActivity.class);
    }

    @OnClick(R.id.tv_forgetpwd_id)
    public void forget(){

    }

    @OnClick(R.id.tv_registry_id)
    public void registry(){

    }

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

    }

    private BalloonRelativeLayout mBalloonRelativeLayout;
    private CustomVideoView mVideoView;
    private int TIME = 100;// 这里默认每隔100毫秒添加一个气泡

    Handler mHandler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {//handler自带定时器实现
            try {
                mHandler.postDelayed(this, TIME);
                mBalloonRelativeLayout.addBalloon();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getFirstStart()){
            readyGoThenKill(FirstActivity.class);
        }

        if (getLoginInfo(0)){
            readyGoThenKill(MainActivity.class);
        }
        //取消title
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        mVideoView = (CustomVideoView) findViewById(R.id.loginVideoView_id);
        mBalloonRelativeLayout = (BalloonRelativeLayout) findViewById(R.id.balloonRelativeLayout_id);
        initVideoView();
    }

    private void initVideoView() {//设置屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.mqr));//设置相关的监听
        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnCompletionListener(this);
    }// 播放准备


    @Override
    public void onPrepared(MediaPlayer mp) {//开始播放
        mVideoView.start();
        mHandler.postDelayed(runnable, TIME);
    }//播放结束

    @Override
    public void onCompletion(MediaPlayer mp) {//开始播放
        mVideoView.start();
    }

    private boolean getLoginInfo(int index){
        SharedPreferences sp = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
        Set<String> loginInfo = new HashSet<String>();
        sp.getStringSet("login"+"_"+index,loginInfo);
        return false;
    }

    class LoginInfo{
        private String userName;
        private String userPwd;
        private String hasLogin;
    }
}
