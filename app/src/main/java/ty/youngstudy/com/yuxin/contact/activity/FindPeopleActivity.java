package ty.youngstudy.com.yuxin.contact.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.cache.NimUserInfoCache;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.model.ToolBarOptions;
import com.netease.nim.uikit.session.SessionCustomization;
import com.netease.nim.uikit.session.constant.Extras;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import ty.youngstudy.com.ui.activity.base.BaseActivity;

/**
 * Created by edz on 2017/12/28.
 */

public class FindPeopleActivity extends BaseActivity {

    private final static String TAG = "FindPeopleActivity";

    private EditText edtAccount;
    private ImageView imgSearch;

    public static void start(Context context, String contactId, SessionCustomization customization) {
        Intent intent = new Intent();
        intent.putExtra(Extras.EXTRA_ACCOUNT, contactId);
        intent.putExtra(Extras.EXTRA_CUSTOMIZATION, customization);
        intent.setClass(context, FindPeopleActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        context.startActivity(intent);
    }

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, FindPeopleActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nim_find_people_activity);
        ToolBarOptions options = new ToolBarOptions();
        options.titleString = "查找好友";
        setToolBar(R.id.toolbar, options);
        handleIntent();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void handleIntent() {

    }

    @Override
    public void initViewAndEvents() {
        findViews();
    }

    private void findViews(){
        edtAccount = (EditText) findViewById(R.id.edt_search_people);
        imgSearch = (ImageView) findViewById(R.id.img_search_people);
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = edtAccount.getText().toString();
                if (account != null) {
                    DialogMaker.showProgressDialog(FindPeopleActivity.this,"查找中...");
                    NimUserInfoCache.getInstance().getUserInfoFromRemote(account, new RequestCallback<NimUserInfo>() {
                        @Override
                        public void onSuccess(NimUserInfo nimUserInfo) {
                            DialogMaker.dismissProgressDialog();
                            if (nimUserInfo == null){
                                Toast.makeText(FindPeopleActivity.this,"账号不存在",Toast.LENGTH_SHORT).show();
                            }else {
                                UserProfileActivity.start(FindPeopleActivity.this, nimUserInfo.getAccount());
                                Log.d(TAG, "success + useinfo = " + nimUserInfo.getAccount());
                            }
                        }

                        @Override
                        public void onFailed(int i) {
                            DialogMaker.dismissProgressDialog();
                            if (i == 408) {
                                Toast.makeText(FindPeopleActivity.this,"网络未连接 code = "+i,Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(FindPeopleActivity.this,"查询失败 code = "+i,Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onException(Throwable throwable) {
                            DialogMaker.dismissProgressDialog();
                            Toast.makeText(FindPeopleActivity.this,"找不到 exp = "+throwable.toString(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }



    @Override
    public void onNavigateUpClicked() {
        super.onNavigateUpClicked();
    }
}
