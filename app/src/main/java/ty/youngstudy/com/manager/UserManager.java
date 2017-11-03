package ty.youngstudy.com.manager;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.UpdateListener;
import okhttp3.OkHttpClient;
import ty.youngstudy.com.Bmob.Person;
import ty.youngstudy.com.MyApplication;
import ty.youngstudy.com.util.string.CheckSumBuilder;

/**
 * Created by yang.tan on 2017/11/1.
 */

public class UserManager {

    private static final String TAG = "UserManager";
    private static UserManager instance = new UserManager();


    private static String user_name;
    private static String user_pwd;
    private static String user_nick;
    private static BmobFile user_head;
    private static String user_email;
    private static String user_addr;
    private static String yx_account;
    private static String yx_token;

    private static Person myPerson = null;

    /**
     * 网易云信 app_key
     */
    private static final String YX_APP_KEY = "2e77d83708cbeae80b19a6505de83400";
    private static final String YX_APP_SECRET = "797a2d85a3e9";
    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded;charset=utf-8";
    private static final String url = "https://api.netease.im/nimserver/user/create.action";

    private static OkHttpClient okHttpClient = new OkHttpClient();
    // 登录借口封装
    public void login(String name,String pwd,final UserListener listener){
        BmobUser.loginByAccount(name, pwd, new LogInListener<Person>() {
            @Override
            public void done(final Person person, BmobException e) {
                if (e == null){
                    if (listener != null) {
                        listener.onSuccess();
                    }
                    person.setYx_account("18251821329");
                    person.setYx_token("a2363f050829e056a952230c42c5c66e");
                    person.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e!=null) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    release();
                    if (listener != null) {
                        listener.onFailed(e);
                    }
                }
            }
        });
    }

    // 退出登录当前账号封装
    public static void logOut(){
        BmobUser.logOut();
        release();
        MyApplication.getInstance().exit();
    }

    // 更新用户信息封装
    public static void update(){

    }


    public class Yunxin{
        public String account;
        public String token;
    }


    //创建网易云信账号和 token
    public Yunxin createYX(String name,String nick){
        Log.d(TAG,"name = "+name);
        Yunxin yunxin = new Yunxin();
        String nonce = String.valueOf(new Random(99999).nextInt());
        String currentTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilder.getCheckSum(YX_APP_SECRET,nonce,currentTime);

        String result = "";
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("AppKey", YX_APP_KEY);
        httpPost.addHeader("Nonce", nonce);
        httpPost.addHeader("CurTime", currentTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        Log.d(TAG,"name 1 = "+name);
        // 设置请求的参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("accid", name));
        nvps.add(new BasicNameValuePair("name",nick));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 打印执行结果
        try {
            // 执行请求
            HttpResponse response = httpClient.execute(httpPost);
            result = EntityUtils.toString(response.getEntity(), "utf-8");
            Log.d(TAG,"result = aaaa = " + result);
            JSONObject object = new JSONObject(result);
            String accid = object.getJSONObject("info").optString("accid");
            String token = object.getJSONObject("info").optString("token");
            yunxin.account = accid;
            yunxin.token = token;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG,"result = 11111"+result);

        return yunxin;
    }






    public interface UserListener{
        void onSuccess();
        void onFailed(BmobException e);
    }



    public static boolean init(){
        myPerson = BmobUser.getCurrentUser(Person.class);
        if (myPerson == null) {
            return false;
        }
        user_name = myPerson.getUsername();
        user_addr = myPerson.getUser_addr();
        user_email = myPerson.getEmail();
        user_head = myPerson.getUser_headicon();
        user_nick = myPerson.getUser_nick();
        return true;
    }

    public static void release(){
        myPerson = null;
        user_name = null;
        user_nick = null;
        user_head = null;
        user_email = null;
        user_addr = null;
        user_pwd = null;
    }

    public static UserManager getInstance() {
        return instance;
    }

    public static void setUser_name(String name){
        user_name = name;
    }

    public static String getUser_name() {
        return user_name;
    }

    public static void setUser_pwd(String user_pwd) {
        UserManager.user_pwd = user_pwd;
    }

    public static void setUser_nick(String user_nick) {
        UserManager.user_nick = user_nick;
    }

    public static String getUser_nick() {
        return user_nick;
    }

    public static void setUser_head(BmobFile user_head) {
        UserManager.user_head = user_head;
    }

    public static BmobFile getUser_head() {
        return user_head;
    }

    public static void setUser_email(String user_email) {
        UserManager.user_email = user_email;
    }

    public static String getUser_email() {
        return user_email;
    }

    public static void setUser_addr(String user_addr) {
        UserManager.user_addr = user_addr;
    }

    public static String getUser_addr() {
        return user_addr;
    }

    public static void setYx_account(String yx_account) {
        UserManager.yx_account = yx_account;
    }

    public static String getYx_account() {
        return yx_account;
    }

    public static void setYx_token(String yx_token) {
        UserManager.yx_token = yx_token;
    }

    public static String getYx_token() {
        return yx_token;
    }
}
