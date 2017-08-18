package ty.youngstudy.com;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

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
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"onCreate");
        SourceSelector.init();
        mContext = getApplicationContext();
        Log.i(TAG,"onCreate1");
        netStateListener = new NetStateListener(NetStateListener.NAME);
        Log.i(TAG,"onCreate2");
        netStateListener.startListener();
        Log.i(TAG,"onCreate3");
        Log.i(TAG,"mContext = "+mContext);
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
