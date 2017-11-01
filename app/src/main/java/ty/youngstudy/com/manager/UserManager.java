package ty.youngstudy.com.manager;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import ty.youngstudy.com.Bmob.Person;
import ty.youngstudy.com.MyApplication;

/**
 * Created by yang.tan on 2017/11/1.
 */

public class UserManager {

    private static String user_name;
    private static String user_pwd;
    private static String user_nick;
    private static BmobFile user_head;
    private static String user_email;
    private static String user_addr;

    private static Person person = null;


    // 登录借口封装
    public static void login(String name,String pwd,final UserListener listener){
        BmobUser.loginByAccount(name, pwd, new LogInListener<Person>() {
            @Override
            public void done(Person person, BmobException e) {
                if (e == null){
                    init();
                    if (listener != null) {
                        listener.onSuccess();
                    }
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











    public interface UserListener{
        void onSuccess();
        void onFailed(BmobException e);
    }



    public static boolean init(){
        person = BmobUser.getCurrentUser(Person.class);
        if (person == null) {
            return false;
        }
        user_name = person.getUsername();
        user_addr = person.getUser_addr();
        user_email = person.getEmail();
        user_head = person.getUser_headicon();
        user_nick = person.getUser_nick();
        return true;
    }

    public static void release(){
        person = null;
        user_name = null;
        user_nick = null;
        user_head = null;
        user_email = null;
        user_addr = null;
        user_pwd = null;
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
}
