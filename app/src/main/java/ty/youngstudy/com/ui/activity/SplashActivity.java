package ty.youngstudy.com.ui.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import ty.youngstudy.com.R;
import ty.youngstudy.com.ui.activity.base.BaseActivity;

public class SplashActivity extends BaseActivity {

    private final static int GO_HOME = 100;
    private final static int GO_LOGIN = GO_HOME+1;
    private final static int GO_FIRST = GO_HOME+2;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GO_FIRST:
                    readyGoThenKill(FirstActivity.class);
                    break;
                case GO_LOGIN:
                    readyGoThenKill(LoginActivity.class);
                    break;
                case GO_HOME:
                    readyGoThenKill(MainActivity.class);
                    break;
                default:
                    break;

            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.sendEmptyMessageDelayed(GO_LOGIN,2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(GO_LOGIN);
        mHandler.removeMessages(GO_FIRST);
        mHandler.removeMessages(GO_HOME);
    }

}
