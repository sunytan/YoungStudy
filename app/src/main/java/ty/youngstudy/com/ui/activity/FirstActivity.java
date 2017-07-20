
package ty.youngstudy.com.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;

import ty.youngstudy.com.R;
import ty.youngstudy.com.ui.activity.base.BaseActivity;

public class FirstActivity extends BaseActivity implements View.OnClickListener{

    private ViewPager viewPager = null;
    private ImageView imageView0 = null;
    private ImageView imageView1 = null;
    private ImageView imageView2 = null;
    private ImageView imageView3 = null;
    private AppCompatButton btn_welcome = null;
    private ArrayList<View> viewList = new ArrayList<View>();
    private ArrayList<String> pagetitle = new ArrayList<String>();

    @Override
    public boolean getFirstStart() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_first);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /* 初始化化 */
        initView();
        initEvent();
        initArray();
        /* 设置适配器 */
        viewPager.setAdapter(new MyPagerAdapter());
        /* 设置应用第一次启动为false */
        setAppStart();
    }

    /* 应用已经启动过来，下次不再进入这个界面 */
    private void setAppStart(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("first_start",false);
        editor.apply();
    }

    /* 初始化视图 */
    private void initView(){
        viewPager = (ViewPager) findViewById(R.id.viewpager_first);
        imageView0 = (ImageView) findViewById(R.id.image_first);
        imageView1 = (ImageView) findViewById(R.id.image_second);
        imageView2 = (ImageView) findViewById(R.id.image_third);
        imageView3 = (ImageView) findViewById(R.id.image_forth);
        btn_welcome = (AppCompatButton) findViewById(R.id.btn_welcome);
    }

    /* 初始化事件 */
    private void initEvent(){
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        btn_welcome.setOnClickListener(this);
    }

    /* 初始化数据 */
    private void initArray(){
        LayoutInflater li = LayoutInflater.from(this);
        View view1 = li.inflate(R.layout.first_pager,null);
        viewList.add(View.inflate(this,R.layout.first_pager,null));
        viewList.add(View.inflate(this,R.layout.second_pager,null));
        viewList.add(View.inflate(this,R.layout.third_pager,null));
        viewList.add(View.inflate(this,R.layout.forth_pager,null));
        pagetitle.add("first");
        pagetitle.add("second");
        pagetitle.add("third");
        pagetitle.add("forth");
    }

    /* 点击事件处理 */
    @Override
    public void onClick(View view) {
        if (view == btn_welcome) {
            Intent intent = new Intent(FirstActivity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void setButtonVisible(int position){
        if (position == 3) {
            btn_welcome.setVisibility(View.VISIBLE);
        } else {
            btn_welcome.setVisibility(View.INVISIBLE);
        }

    }

    /* 页面改变监听器 */
    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageSelected(int position) {
            switch (position){
                case 0:
                    imageView0.setImageDrawable(getResources().getDrawable(R.mipmap.page_now));
                    imageView1.setImageDrawable(getResources().getDrawable(R.mipmap.page));
                    break;
                case 1:
                    imageView1.setImageDrawable(getResources().getDrawable(R.mipmap.page_now));
                    imageView0.setImageDrawable(getResources().getDrawable(R.mipmap.page));
                    imageView2.setImageDrawable(getResources().getDrawable(R.mipmap.page));
                    break;
                case 2:
                    imageView2.setImageDrawable(getResources().getDrawable(R.mipmap.page_now));
                    imageView1.setImageDrawable(getResources().getDrawable(R.mipmap.page));
                    imageView3.setImageDrawable(getResources().getDrawable(R.mipmap.page));
                    break;
                case 3:
                    imageView3.setImageDrawable(getResources().getDrawable(R.mipmap.page_now));
                    imageView2.setImageDrawable(getResources().getDrawable(R.mipmap.page));
                    break;
                default:
                    break;
            }
            Log.i("tanyang","position = "+position);
            setButtonVisible(position);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /* 页面适配器 */
    private class MyPagerAdapter extends PagerAdapter{

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager)container).addView(viewList.get(position));
            return viewList.get(position);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager)container).removeView(viewList.get(position));
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pagetitle.get(position);
        }
    }
}
