package ty.youngstudy.com.ui.activity;

import java.io.File;
import java.util.List;

import org.json.JSONArray;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.QueryListener;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import ty.youngstudy.com.R;
import ty.youngstudy.com.Bmob.ArticleBean;
import ty.youngstudy.com.manager.UserManager;
import ty.youngstudy.com.ui.activity.base.BaseActivity;

public class SplashActivity extends BaseActivity {

    private final static int GO_HOME = 100;
    private final static int GO_LOGIN = GO_HOME+1;
    private final static int GO_FIRST = GO_HOME+2;

    private final static String IMG_URL = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505297314037&di=b11bfe2f90541a63175a89f8a941d67a&imgtype=0&src=http%3A%2F%2Fwww.5djpg.com%2Fuploads%2Fallimg%2F140609%2F1-1406091P426.jpg";
    private ImageView imageView;
    private String headName = "";
    private boolean loginSuccess = false;

    private static final String BMOB_CACHE_PATH = Environment.getDownloadCacheDirectory()+"/bmob/";
    private RxPermissions rxPermissions;

    /**
     * 基本权限管理
     */
    private final String[] BASIC_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    public View getLoadingView() {
        return null;
    }

    @Override
    public boolean getFirstStart() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sp.getBoolean("first_start", true);
    }

    @Override
    public void initViewAndEvents() {
        imageView = (ImageView) findViewById(R.id.splash_id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        rxPermissions = new RxPermissions(SplashActivity.this);
        final SharedPreferences sp1 = getSharedPreferences("UserInfo_sp", Context.MODE_PRIVATE);
        String headpath = sp1.getString("headfile","");
        File file = new File(headpath);
        if (file.exists()) {
            headName =file.getName();
            Glide.with(SplashActivity.this).asDrawable().load(file).into(imageView);
        }

        BmobQuery query = new BmobQuery("_Article");
        query.addWhereEqualTo("title","splash");
        query.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray jsonArray, BmobException e) {
                if (jsonArray == null)
                    return;
                Log.d("splash = ",jsonArray.toString());
                List<ArticleBean> been = new Gson().fromJson(jsonArray.toString(),new TypeToken<List<ArticleBean>>() {}.getType());
                final String fileName = been.get(0).getSplash().getFilename();
                String url = been.get(0).getSplash().getUrl();

                if (fileName.equals(headName)) {
                    Log.d("splash","相同的splash，不重新下载");
                } else {

                    Glide.with(SplashActivity.this).asDrawable().load(url).into(imageView);
                    BmobFile bmobFile = new BmobFile(fileName, null, url);
                    bmobFile.download(new DownloadFileListener() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                showToast("下载成功 s = " + s);
                                SharedPreferences.Editor editor = sp1.edit();
                                editor.putString("headfile", s);
                                editor.commit();
                            }
                        }

                        @Override
                        public void onProgress(Integer integer, long l) {

                        }
                    });
                }
            }
        });
    }


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GO_FIRST:
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("loginSuccess",loginSuccess);
                    readyGoThenKill(FirstActivity.class,bundle);
                    break;
                case GO_LOGIN:
                    if (!loginSuccess) {
                        readyGoThenKill(LoginActivity.class);
                    } else {
                        readyGoThenKill(MainActivity.class);
                    }
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
        rxPermissions.requestEach(BASIC_PERMISSIONS).subscribe(new Observer<Permission>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Permission permission) {
                if (!permission.granted){
                    Log.d("requestPermission",permission.name+" not granted");
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d("requestPermission","error = "+e.toString());
            }

            @Override
            public void onComplete() {
                if (getFirstStart()) {
                    mHandler.sendEmptyMessageDelayed(GO_FIRST,3000);
                    return;
                }
                mHandler.sendEmptyMessageDelayed(GO_LOGIN,3000);
                if (UserManager.getInstance().init()){
                    UserManager.getInstance().loginYX(UserManager.getInstance().getInstance().getYx_account(), UserManager.getInstance().getYx_token(), new UserManager.UserListener() {
                        @Override
                        public void onSuccess() {
                            loginSuccess = true;
                        }
                        @Override
                        public void onFailed(BmobException e) {
                            loginSuccess = false;
                        }
                    });
                } else {
                    loginSuccess = false;
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mHandler.removeMessages(GO_LOGIN);
        mHandler.removeMessages(GO_FIRST);
        mHandler.removeMessages(GO_HOME);
        super.onDestroy();
    }

}
