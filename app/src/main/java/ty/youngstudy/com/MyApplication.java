package ty.youngstudy.com;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by edz on 2017/8/1.
 */

public class MyApplication extends Application {

    private static MyApplication instance;
    private List<Activity> activityList = new LinkedList<>();


    @Override
    public void onCreate() {
        super.onCreate();
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
