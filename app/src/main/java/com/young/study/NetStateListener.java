
package com.young.study;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by edz on 2017/8/18.
 */

public class NetStateListener extends Thread {

    private ArrayList<NetStateCallBack> callBacks = new ArrayList<NetStateCallBack>();
    public static final String NAME = "NetStateListener";
    private ConnectivityManager connectivityManager;
    private boolean loopHasRun = false;
    private NetworkInfo info;
    private NetworkInfo.DetailedState detailedState = NetworkInfo.DetailedState.DISCONNECTED;
    private int currentState = -1;
    private final static int CONNECTED = 1;
    private final static int DISCONNECTED = 2;
    private final static int ERROR = 3;

    private boolean loop = true;

    private final byte[] bytes = new byte[0];

    public NetStateListener(String name) {
        super(name);
    }

    public void startListener(){
        connectivityManager = (ConnectivityManager) MyApplication.getInstance()
                .getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        start();
        //waitLoopReady();
        Log.i("tanyang","start");

    }

    public void subscribe(NetStateCallBack stateCallBack){
        callBacks.add(stateCallBack);
    }

    public void unSubscribe(NetStateCallBack stateCallBack){
        callBacks.remove(stateCallBack);
    }

//    @Override
//    protected void onLooperPrepared() {
//        super.onLooperPrepared();
//        Log.i("tanyang","onLooperPrepared1");
//        synchronized (bytes){
//            Log.i("tanyang","onLooperPrepared2");
//            bytes.notify();
//            loopHasRun = true;
//        }
//    }

    private void waitLoopReady(){
        synchronized (bytes){
            if (loopHasRun) {
                return;
            }
            try {
                Log.i("tanyang","waitLoopReady");
                bytes.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void run() {
        super.run();
        boolean loop = true;
        while (loop) {
            //loop = false;
            info = connectivityManager.getActiveNetworkInfo();
            if (info == null) {
                Log.i("tanyang","info is null");
                //if (currentState != DISCONNECTED) {
                    Log.i("tanyang","detailState = "+detailedState);
                    for (NetStateCallBack callBack : callBacks) {
                        callBack.onNetDisConnected();
                    }
                    currentState = DISCONNECTED;
                //}
            } else {
                detailedState = info.getDetailedState();
                if (detailedState != NetworkInfo.DetailedState.CONNECTED) {
                    //if (currentState != ERROR) {
                        Log.i("tanyang","detailState = "+detailedState);
                        for (NetStateCallBack callBack : callBacks) {
                            callBack.onNetError();
                        }
                        currentState = ERROR;
                    //}
                } else {
                    //if (currentState != CONNECTED) {
                        Log.i("tanyang","detailState = "+detailedState);
                        for (NetStateCallBack callBack : callBacks) {
                            callBack.onNetConnected();
                        }
                        currentState = CONNECTED;
                    //}
                }
            }
            try {
                Thread.currentThread().sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopListener(){
        loop = false;
    }
    public interface NetStateCallBack {
        public abstract void onNetDisConnected();
        public abstract void onNetConnected();
        public abstract void onNetError();
    }
}
