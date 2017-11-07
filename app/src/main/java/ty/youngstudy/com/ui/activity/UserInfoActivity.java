package ty.youngstudy.com.ui.activity;

import android.os.Bundle;
import android.view.View;

import butterknife.OnClick;
import ty.youngstudy.com.R;
import ty.youngstudy.com.manager.UserManager;
import ty.youngstudy.com.ui.activity.base.BaseActivity;

public class UserInfoActivity extends BaseActivity {


    @OnClick(R.id.btn_exit_current)
    protected void exitCurrentAccount(){
        readyGoThenKill(LoginActivity.class);
        UserManager.getInstance().logOut();
    }

    @Override
    public View getLoadingView() {
        return null;
    }

    @Override
    public boolean getFirstStart() {
        return false;
    }

    @Override
    public void initViewAndEvents() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
    }
}
