package ty.youngstudy.com.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.netease.nim.uikit.common.http.NimHttpClient;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import ty.youngstudy.com.Bmob.Person;
import ty.youngstudy.com.R;
import ty.youngstudy.com.manager.UserManager;
import ty.youngstudy.com.ui.activity.base.BaseActivity;

public class RegisterActivity extends BaseActivity {

    private static final String TAG = "RegisterActivity";

    private EditText edt_user_name;
    private EditText edt_user_pwd;
    private EditText edt_user_nick;
    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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
        edt_user_name = (EditText) findViewById(R.id.edt_user_name);
        edt_user_pwd = (EditText) findViewById(R.id.edt_user_pwd);
        edt_user_nick = (EditText) findViewById(R.id.edt_user_nick);
        btn_register = (Button) findViewById(R.id.btn_register_id);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogMaker.showProgressDialog(RegisterActivity.this, "注册中", false);
                final String name = edt_user_name.getText().toString();
                String pwd = edt_user_pwd.getText().toString();
                final String nick = edt_user_nick.getText().toString();
                Person person = new Person();
                person.setUsername(name);
                person.setPassword(pwd);
                person.setUser_nick(nick);
                person.signUp(new SaveListener<Person>() {
                    @Override
                    public void done(final Person person, BmobException e) {
                        Log.d(TAG,person.getUsername()+","+person.getUser_nick());
                        if (e == null) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    UserManager.getInstance().createYX(name, nick, new NimHttpClient.NimHttpCallback() {
                                        @Override
                                        public void onResponse(String response, int code, Throwable e) {
                                            Log.d(TAG,"res = "+response);
                                            if (code != 200 || e != null) {
                                                Log.e(TAG, "register failed : code = " + code + ", errorMsg = "
                                                        + (e != null ? e.getMessage() : "null"));
                                                DialogMaker.dismissProgressDialog();
                                                return;
                                            }
                                            Log.d(TAG,"res = "+response);
                                            try {
                                                JSONObject object = new JSONObject(response);
                                                String accid = object.getJSONObject("info").optString("accid");
                                                String token = object.getJSONObject("info").optString("token");

                                                if (accid != null && token != null) {
                                                    person.setYx_account(accid);
                                                    person.setYx_token(token);
                                                    person.update(new UpdateListener() {
                                                        @Override
                                                        public void done(BmobException ex) {
                                                            if (ex == null) {
                                                                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                                                DialogMaker.dismissProgressDialog();
                                                                (RegisterActivity.this).finish();
                                                            } else {
                                                                Toast.makeText(RegisterActivity.this, "注册失败 ="+ex.toString(), Toast.LENGTH_SHORT).show();

                                                            }
                                                        }
                                                    });
                                                }
                                            } catch (JSONException exp) {
                                                showToast("exp = "+exp.toString());
                                                exp.printStackTrace();
                                            }
                                        }
                                    });
                                }
                            }).start();
                        }else {
                            DialogMaker.dismissProgressDialog();
                            Toast.makeText(RegisterActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
