package com.young.study.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by edz on 2017/8/18.
 */

public class NetChangeReceiver extends BroadcastReceiver {

    public static ArrayList<NetChangeCallback> listener = new ArrayList<NetChangeCallback>();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == ConnectivityManager.CONNECTIVITY_ACTION){
            Log.i("tanyang","receiver netChange");
            Log.i("tanyang","intent = "+intent);
            for (NetChangeCallback callback:listener) {
                callback.onNetChange();
            }
        }
    }

    public static void subscribe(NetChangeCallback netChangeCallback){
        listener.add(netChangeCallback);
    }

    public void unSubscribe(NetChangeCallback netChangeCallback){
        listener.remove(netChangeCallback);
    }

    public interface NetChangeCallback{
        void onNetChange();
    }
}
