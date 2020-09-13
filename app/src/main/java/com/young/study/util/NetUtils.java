package com.young.study.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.young.study.MyApplication;

/**
 * Created by edz on 2017/8/18.
 */

public class NetUtils {

    public static boolean netConnect(){
        Context context = MyApplication.getInstance().getContext();
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null){
            boolean isConnect = info.isConnected();
            NetworkInfo.State state = info.getState();
            String reason = info.getReason();
            NetworkInfo.DetailedState detailedState = info.getDetailedState();
            String extrainfo = info.getExtraInfo();
            String subtypeName = info.getSubtypeName();
            String typeName = info.getTypeName();
            boolean isFailover = info.isFailover();
            Log.i("tanyang","isConnect = "+isConnect+",state = "+state+",reason = "+reason
                +"\n"+"detailState = "+detailedState+",extrainfo = "+extrainfo+",subtypeName = "+subtypeName
                +"\n"+"typeName = "+typeName+",isFailover = "+isFailover);
        }
        return info != null && info.isAvailable();
    }

}
