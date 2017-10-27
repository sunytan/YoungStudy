package ty.youngstudy.com.database;

/**
 * Created by edz on 2017/10/27.
 */

public class UserNameDuplicateException extends Exception {

    public UserNameDuplicateException() {
        super();
    }

    public UserNameDuplicateException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public UserNameDuplicateException(String detailMessage) {
        super(detailMessage);
    }

    public UserNameDuplicateException(Throwable throwable) {
        super(throwable);
    }

}
