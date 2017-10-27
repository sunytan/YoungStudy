package ty.youngstudy.com.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import ty.youngstudy.com.R;
import ty.youngstudy.com.ui.activity.base.BaseActivity;

public class SplashActivity extends BaseActivity {

    private final static int GO_HOME = 100;
    private final static int GO_LOGIN = GO_HOME+1;
    private final static int GO_FIRST = GO_HOME+2;

    private final static String IMG_URL = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505297314037&di=b11bfe2f90541a63175a89f8a941d67a&imgtype=0&src=http%3A%2F%2Fwww.5djpg.com%2Fuploads%2Fallimg%2F140609%2F1-1406091P426.jpg";

    private ImageView imageView;

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
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS).build();
        Request request = new Request.Builder().url(IMG_URL).build();
        Glide.with(SplashActivity.this).load(IMG_URL).into(imageView);
        /*client.newCall(request).enqueue(new Callback() {
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
        });*/
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

        mHandler.sendEmptyMessageDelayed(GO_LOGIN,3000);
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
