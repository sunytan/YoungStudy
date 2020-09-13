package com.young.study.ui.activity;

import android.os.Bundle;
import android.view.View;

import butterknife.OnClick;
import com.young.study.R;
import com.young.study.manager.UserManager;
import com.young.study.ui.activity.base.BaseActivity;

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
