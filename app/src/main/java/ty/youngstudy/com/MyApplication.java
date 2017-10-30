package ty.youngstudy.com;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import org.litepal.LitePal;

import java.util.LinkedList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import ty.youngstudy.com.ttzw.SourceSelector;

/**
 * Created by edz on 2017/8/1.
 */

public class MyApplication extends Application {

    private static MyApplication instance = new MyApplication();
    private List<Activity> activityList = new LinkedList<>();
    private static final String TAG = "tanyang";
    private static Context mContext = null;
    private static NetStateListener netStateListener;

    //Application ID，SDK初始化必须用到此密钥
    public static final String APP_ID = "b23a146c4c8ab7bc16f93676f6a9cccd";

    //REST API请求中HTTP头部信息必须附带密钥之一
    public static final String REST_API_KEY = "e666695b0e8b14c356de7e83cb6289fd";

    //Secret Key，是SDK安全密钥，不可泄漏，在云端逻辑测试云端代码时需要用到
    public static final String SECRET_KEY = "03ea21b040239f5d";

    //Master Key，超级权限Key。应用开发或调试的时候可以使用该密钥进行各种权限的操作，此密钥不可泄漏
    public static final String MASTER_KEY = "47dc03b70861073bdb411c5f7e02ad74";

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化Bmob后端云
        initBmob();
        SourceSelector.init();
        mContext = getApplicationContext();
        netStateListener = new NetStateListener(NetStateListener.NAME);
        netStateListener.startListener();

        LitePal.initialize(mContext);
        LitePal.getDatabase();
        Log.i(TAG,"mContext = "+mContext);
    }

    private void initBmob(){
        //第一：默认初始化
        Bmob.initialize(this, APP_ID);
        // 注:自v3.5.2开始，数据sdk内部缝合了统计sdk，开发者无需额外集成，传渠道参数即可，不传默认没开启数据统计功能
        //Bmob.initialize(this, "Your Application ID","bmob");

        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        .setApplicationId(APP_ID)
        ////请求超时时间（单位为秒）：默认15s
        .setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        .setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        .setFileExpiration(2500)
        .build();
        Bmob.initialize(config);
    }

    public Context getContext() {
        return mContext;
    }

    public NetStateListener getNetStateListener(){
        return netStateListener;
    }

    public synchronized static MyApplication getInstance(){
        return instance;
    }

    public void addActivity(Activity activity){
        activityList.add(activity);
    }

    public void removeActivity(Activity activity){
        activityList.remove(activity);
    }

    public void exit(){
        try {
            MyApplication.getInstance().getNetStateListener().stopListener();
            for (Activity activity:activityList){
                activity.finish();
            }
        } catch (Exception e){

        } finally {

        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }
}
