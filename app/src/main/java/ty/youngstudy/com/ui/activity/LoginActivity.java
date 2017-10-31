
package ty.youngstudy.com.ui.activity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import ty.youngstudy.com.R;
import ty.youngstudy.com.Bmob.Person;
import ty.youngstudy.com.ui.activity.base.BaseActivity;
import ty.youngstudy.com.ui.view.layout.BalloonRelativeLayout;
import ty.youngstudy.com.ui.view.layout.CustomVideoView;

public class LoginActivity extends BaseActivity
        implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private ArrayList<LoginInfo> loginAccount = new ArrayList<LoginInfo>();

    private EditText edt_login_name;
    private TextView edt_login_pwd;

    @BindView(R.id.img_name_delete)
    ImageView img_name_delete;

    @BindView(R.id.img_pwd_delete)
    ImageView img_pwd_delete;

    @OnClick(R.id.btn_login_id)
    public void login(){
        loginAccount();
    }
    @OnClick(R.id.tv_registry_id)
    public void register(){
        readyGo(RegisterActivity.class);
    }

    @OnClick(R.id.tv_forgetpwd_id)
    public void forget(){

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
        edt_login_name = (EditText) findViewById(R.id.edt_login_name);
        edt_login_pwd = (EditText) findViewById(R.id.edt_login_pwd);
        img_pwd_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_login_pwd.setText(null);
            }
        });
        img_name_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_login_name.setText(null);
            }
        });
        edt_login_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (v.getText().length() > 0) {
                    img_name_delete.setVisibility(View.VISIBLE);
                } else {
                    img_name_delete.setVisibility(View.GONE);
                }
                return false;
            }
        });
        edt_login_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (((EditText)v).getText().toString().length() > 0){
                        img_name_delete.setVisibility(View.VISIBLE);
                    }
                } else {
                    img_name_delete.setVisibility(View.GONE);
                }
            }
        });
        edt_login_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    img_name_delete.setVisibility(View.VISIBLE);
                } else {
                    img_name_delete.setVisibility(View.GONE);
                }
            }
        });
        edt_login_pwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (((EditText)v).getText().toString().length() > 0){
                        img_pwd_delete.setVisibility(View.VISIBLE);
                    }
                } else {
                    img_pwd_delete.setVisibility(View.GONE);
                }
            }
        });
        edt_login_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    img_pwd_delete.setVisibility(View.VISIBLE);
                } else {
                    img_pwd_delete.setVisibility(View.GONE);
                }
        }
        });
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


    private void loginAccount(){
        String name = edt_login_name.getText().toString();
        String pwd = edt_login_pwd.getText().toString();
        BmobUser.loginByAccount(name, pwd, new LogInListener<Person>() {
            @Override
            public void done(Person person, BmobException e) {
                if (e == null) {
                    showToast("登录成功");
                    readyGoThenKill(MainActivity.class);
                } else {
                    showToast(e.toString());
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getFirstStart()){
            readyGoThenKill(FirstActivity.class);
        }

        if (getLoginInfo()){
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
        Intent intent = getIntent();
        Log.d("intentName = ",intent+"");
        if (intent != null) {
            Bundle bd = intent.getExtras();
            if (bd != null){
                edt_login_name.setText(bd.getString("userName",""));
            }
        }
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

    private boolean getLoginInfo(){
        Person bmobuser = BmobUser.getCurrentUser(Person.class);
        if (bmobuser != null) {
            return true;
        }
        return false;
    }

    class LoginInfo{
        private String userName;
        private String userPwd;
        private String hasLogin;
    }
}
