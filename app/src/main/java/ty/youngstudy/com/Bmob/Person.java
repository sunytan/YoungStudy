package ty.youngstudy.com.Bmob;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by edz on 2017/10/30.
 */


public class Person extends BmobUser {
    private Integer user_age;
    private String user_nick;
    private String user_sex;
    private String user_addr;
    private BmobFile user_headicon;

    public void setUser_age(Integer user_age) {
        this.user_age = user_age;
    }

    public Integer getUser_age() {
        return user_age;
    }

    public void setUser_nick(String user_nick) {
        this.user_nick = user_nick;
    }

    public String getUser_nick() {
        return user_nick;
    }

    public void setUser_sex(String user_sex) {
        this.user_sex = user_sex;
    }

    public String getUser_sex() {
        return user_sex;
    }

    public void setUser_addr(String user_addr) {
        this.user_addr = user_addr;
    }

    public String getUser_addr() {
        return user_addr;
    }

    public void setUser_headicon(BmobFile user_headicon) {
        this.user_headicon = user_headicon;
    }

    public BmobFile getUser_headicon() {
        return user_headicon;
    }
}
