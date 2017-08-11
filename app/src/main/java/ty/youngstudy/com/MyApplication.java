package ty.youngstudy.com;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import ty.youngstudy.com.ttzw.SourceSelector;

/**
 * Created by edz on 2017/8/1.
 */

public class MyApplication extends Application {

    private static MyApplication instance;
    private List<Activity> activityList = new LinkedList<>();
    private static final String TAG = "MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"onCreate");
        SourceSelector.init();
    }


    public synchronized static MyApplication getInstance(){
        if (instance == null) {
            instance = new MyApplication();
        }
        return instance;
    }

    public void addActivity(Activity activity){
        activityList.add(activity);
    }

    public void exit(){
        try {
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
