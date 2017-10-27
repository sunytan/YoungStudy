package ty.youngstudy.com.database;

import org.litepal.crud.DataSupport;

/**
 * Created by edz on 2017/10/27.
 */

public class UserInfo extends DataSupport {
    private String userName;
    private String userPwd;
    private byte[] userHead;
    private String userAge;


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserHead(byte[] userHead) {
        this.userHead = userHead;
    }

    public byte[] getUserHead() {
        return userHead;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getUserAge() {
        return userAge;
    }
}
