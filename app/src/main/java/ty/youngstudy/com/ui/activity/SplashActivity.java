package ty.youngstudy.com.ui.activity;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.QueryListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ty.youngstudy.com.Bmob.Person;
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

    private boolean loginSuccess = false;

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
        /*BmobQuery query = new BmobQuery("_Article");
        query.addWhereEqualTo("title","splash3");
        query.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray jsonArray, BmobException e) {
                Log.d("splash = ",jsonArray.toString());
                List<ArticleBean> been = new Gson().fromJson(jsonArray.toString(),new TypeToken<List<ArticleBean>>() {}.getType());
                String content = been.get(0).getContent();
                String[] s = content.split(" ");
                String url = "";
                for (int i = 0; i < s.length; i++) {
                    if (s[i].contains("src=")){
                        url = s[i].substring(4);
                        break;
                    }
                }
                Log.d("splash url = ",url);
                *//*OkHttpClient client = new OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS).build();
                Request request = new Request.Builder().url(url).build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("tanyang","connect fail");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()){
                            final byte[] img = response.body().bytes();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("tanyang","set background");
                                    BitmapFactory.Options options = new BitmapFactory.Options();
                                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                                    options.inPurgeable = true;
                                    options.inInputShareable =true;
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(img,0,img.length,options);
                                    Drawable drawable = new BitmapDrawable(bitmap);
                                    Glide.with(SplashActivity.this).load(drawable).into(imageView);
                                    //imageView.setImageBitmap(bitmap);
                                    //viewGroup.setBackground();
                                }
                            });
                        }
                    }
                });*//*
            }
        });*/
        Glide.with(SplashActivity.this).load(IMG_URL).into(imageView);
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
        if (getFirstStart()) {
            mHandler.sendEmptyMessageDelayed(GO_FIRST,3000);
            return;
        }
        mHandler.sendEmptyMessageDelayed(GO_LOGIN,3000);
        if (UserManager.getInstance().init()){
             UserManager.getInstance().loginYX(UserManager.getYx_account(), UserManager.getYx_token(), new UserManager.UserListener() {
                  @Override
                  public void onSuccess() {
                       loginSuccess = true;
                  }
                  @Override
                  public void onFailed(BmobException e) {
                       loginSuccess = false;
                  }
             });
            /*UserManager.getUser_head().download(new DownloadFileListener() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        Log.d("splash", "downpath = " + s);
                    } else {
                        showToast("下载失败："+e.toString());
                    }
                }

                @Override
                public void onProgress(Integer integer, long l) {

                }
            });*/
        } else {
            loginSuccess = false;
        }
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
