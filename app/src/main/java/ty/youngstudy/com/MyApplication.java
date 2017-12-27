package ty.youngstudy.com;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.netease.nim.uikit.contact.core.query.PinYin;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;

import org.litepal.LitePal;

import java.util.LinkedList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import ty.youngstudy.com.reader.manager.NovelManager;
import ty.youngstudy.com.ttzw.SourceSelector;
import ty.youngstudy.com.util.NovelFileUtils;
import ty.youngstudy.com.util.SharedPreferencesUtil;
import ty.youngstudy.com.util.SystemUtil;
import ty.youngstudy.com.yuxin.session.SessionHelper;

/**
 * Created by edz on 2017/8/1.
 */

public class MyApplication extends Application {

    final long maxMemory = Runtime.getRuntime().maxMemory();
    final long totalMemory = Runtime.getRuntime().totalMemory();
    final long freeMemory = Runtime.getRuntime().freeMemory();

    private final static MyApplication instance = new MyApplication();
    private List<Activity> activityList = new LinkedList<>();
    private static final String TAG = "tanyang";
    private static Context mContext = null;
    private static ty.youngstudy.com.NetStateListener netStateListener;

    /**
     * Bmob后端云Id：
     */
    //Application ID，SDK初始化必须用到此密钥
    private static final String BMOB_APP_ID = "b23a146c4c8ab7bc16f93676f6a9cccd";

    //REST API请求中HTTP头部信息必须附带密钥之一
    private static final String REST_API_KEY = "e666695b0e8b14c356de7e83cb6289fd";

    //Secret Key，是SDK安全密钥，不可泄漏，在云端逻辑测试云端代码时需要用到
    private static final String SECRET_KEY = "03ea21b040239f5d";

    //Master Key，超级权限Key。应用开发或调试的时候可以使用该密钥进行各种权限的操作，此密钥不可泄漏
    private static final String MASTER_KEY = "47dc03b70861073bdb411c5f7e02ad74";

    /**
     * 讯飞语音识别
     */
    private static final String IAT_APP_ID = "59f94725";

    /**
     * 网易云信 app_key
     */
    private static final String YX_APP_KEY = "2e77d83708cbeae80b19a6505de83400";
    private static final String YX_APP_SECRET = "797a2d85a3e9";

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        Log.d("tanyang memory","max = "+maxMemory+",,,totle = "+totalMemory + ",,,free = "+freeMemory);
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        int size = am.getMemoryClass();
        int largeSize = am.getLargeMemoryClass();
        Log.d("tanyang memory","size = "+size+",,,largeSize = "+largeSize);

        // 初始化数据库
        LitePal.initialize(mContext);
        LitePal.getDatabase();

        //初始化小说文件管理
        NovelFileUtils.init();
        SharedPreferencesUtil.init(mContext,"novel_sp",Context.MODE_PRIVATE);
        NovelManager.getInstance().init(mContext);


        //初始化Bmob后端云
        initBmob();
        //初始化IAT语音识别
        //initIat();

        //初始化云信即时通讯
        NIMClient.init(this,null,options());
        if (inMainProcess()) {
            PinYin.init(this);
            PinYin.validate();
            initYunXin();
            SessionHelper.init();
        }

        SourceSelector.init();
        netStateListener = new ty.youngstudy.com.NetStateListener(ty.youngstudy.com.NetStateListener.NAME);
        netStateListener.startListener();

        Log.i(TAG,"mContext = "+mContext);
    }

    private void initBmob(){
        //第一：默认初始化
        Bmob.initialize(mContext, BMOB_APP_ID);
        // 注:自v3.5.2开始，数据sdk内部缝合了统计sdk，开发者无需额外集成，传渠道参数即可，不传默认没开启数据统计功能
        //Bmob.initialize(this, "Your Application ID","bmob");

        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        .setApplicationId(BMOB_APP_ID)
        ////请求超时时间（单位为秒）：默认15s
        .setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        .setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        .setFileExpiration(2500)
        .build();
        Bmob.initialize(config);
    }

    private void initIat(){
        SpeechUtility.createUtility(mContext, SpeechConstant.APPID+"="+IAT_APP_ID+","
                +SpeechConstant.ENGINE_MODE+"="+SpeechConstant.MODE_MSC+","
                +SpeechConstant.ENGINE_TYPE+"="+SpeechConstant.TYPE_LOCAL+","
                +SpeechConstant.LIB_NAME+"="+"libmsc.so");
    }

    public boolean inMainProcess() {
        String packageName = getPackageName();
        String processName = SystemUtil.getProcessName(this);
        return packageName.equals(processName);
    }

    private LoginInfo getLoginInfo() {
        String account = "null";
        String token = "null";

        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            return new LoginInfo(account, token);
        } else {
            return null;
        }
    }

    private void initYunXin(){
        NimUIKit.init(this);
    }

    private SDKOptions options(){
        SDKOptions options = new SDKOptions();
        options.appKey = YX_APP_KEY;

        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
/*        config.notificationEntrance = WelcomeActivity.class; // 点击通知栏跳转到该Activity
        config.notificationSmallIconId = R.drawable.ic_stat_notify_msg;*/
        // 呼吸灯配置
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;
        // 通知铃声的uri字符串
        config.notificationSound = "android.resource://com.netease.nim.demo/raw/msg";
        options.statusBarNotificationConfig = config;

        // 配置保存图片，文件，log 等数据的目录
        // 如果 options 中没有设置这个值，SDK 会使用下面代码示例中的位置作为 SDK 的数据目录。
        // 该目录目前包含 log, file, image, audio, video, thumb 这6个目录。
        // 如果第三方 APP 需要缓存清理功能， 清理这个目录下面个子目录的内容即可。
        String sdkPath = Environment.getExternalStorageDirectory() + "/" + getPackageName() + "/nim";
        options.sdkStorageRootPath = sdkPath;

        // 配置是否需要预下载附件缩略图，默认为 true
        options.preloadAttach = true;

        // 配置附件缩略图的尺寸大小。表示向服务器请求缩略图文件的大小
        // 该值一般应根据屏幕尺寸来确定， 默认值为 Screen.width / 2
        options.thumbnailSize = ScreenUtil.screenWidth / 2;

        // 用户资料提供者, 目前主要用于提供用户资料，用于新消息通知栏中显示消息来源的头像和昵称
        options.userInfoProvider = new UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String account) {
                return null;
            }

            @Override
            public int getDefaultIconResId() {
                return R.drawable.nim_avatar_default;
            }

            @Override
            public Bitmap getTeamIcon(String tid) {
                return null;
            }

            @Override
            public Bitmap getAvatarForMessageNotifier(String account) {
                return null;
            }

            @Override
            public String getDisplayNameForMessageNotifier(String account, String sessionId,
                                                           SessionTypeEnum sessionType) {
                return null;
            }
        };
        return options;
    }


    public Context getContext() {
        return mContext;
    }

    public ty.youngstudy.com.NetStateListener getNetStateListener(){
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
