
package ty.youngstudy.com.ui.activity.reader;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ty.youngstudy.com.ModelManager;
import ty.youngstudy.com.MyApplication;
import ty.youngstudy.com.NetStateListener;
import ty.youngstudy.com.R;
import ty.youngstudy.com.adapter.FragmentAdapter;
import ty.youngstudy.com.reader.ReaderModel;
import ty.youngstudy.com.ui.activity.base.BaseActivity;
import ty.youngstudy.com.ui.fragment.reader.LastUpdateFragment;
import ty.youngstudy.com.ui.fragment.reader.MyShelftFragment;
import ty.youngstudy.com.ui.fragment.reader.OnlineNovelFragment;
import ty.youngstudy.com.ui.fragment.reader.SortKindFragment;

public class NovelMainActivity extends BaseActivity implements NetStateListener.NetStateCallBack{

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private Fragment myShelftFragment;
    private Fragment sortKindFragment;
    private Fragment lastUpdateFragment;
    private Fragment onlineNovelFragment;
    private String[] tabCharList = new String[] {"我的书架","分类小说","最近更新","在线小说"};
    private List<String> titleList = new ArrayList<>();
    private List<Fragment> fragmentList;
    private FragmentAdapter fragmentAdapter;
    private LinearLayout linearLayout;
    private int[] img = {
            R.drawable.selector_tab_shelft, R.drawable.selector_tab_sort,
            R.drawable.selector_tab_last, R.drawable.selector_tab_online
    };

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
        mTabLayout = (TabLayout) findViewById(R.id.tab_main1_id);
        mViewPager = (ViewPager) findViewById(R.id.vp_novel_id);
        linearLayout = (LinearLayout) findViewById(R.id.net_not_connect);
        myShelftFragment = new MyShelftFragment();
        sortKindFragment = new SortKindFragment();
        lastUpdateFragment = new LastUpdateFragment();
        onlineNovelFragment = new OnlineNovelFragment();
        for (int i = 0; i < tabCharList.length; i++) {
            titleList.add(tabCharList[i]);
        }
        fragmentList = new ArrayList<>();
        fragmentList.add(myShelftFragment);
        fragmentList.add(sortKindFragment);
        fragmentList.add(lastUpdateFragment);
        fragmentList.add(onlineNovelFragment);
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(),fragmentList,titleList);
        mViewPager.setAdapter(fragmentAdapter);
        mViewPager.setCurrentItem(0);
        initTabView();

    }

    private void initTabView(){
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setSelectedTabIndicatorHeight(0);
        for (int i = 0; i < fragmentList.size(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(R.layout.tab_item_layout);
                TextView textView =(TextView) tab.getCustomView().findViewById(R.id.tv_tab_find_id);
                textView.setText(titleList.get(i));
                ImageView imageView = (ImageView) tab.getCustomView().findViewById(R.id.iv_tab_find_id);
                imageView.setImageResource(img[i]);
            }
        }
        mTabLayout.getTabAt(0).getCustomView().setSelected(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.novel_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_novel_exit){
            Toast.makeText(getApplicationContext(),"退出应用",Toast.LENGTH_SHORT).show();
            MyApplication.getInstance().exit();
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel_main);
        ReaderModel model = new ReaderModel(ReaderModel.NAME,MyApplication.getInstance().getContext());
        ModelManager.getInstance().add(model);
        MyApplication.getInstance().getNetStateListener().subscribe(this);
    }

    @Override
    public void onNetDisConnected() {
        Log.i("tanyang","onNetDisConnected");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                linearLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onNetConnected() {
        Log.i("tanyang","onNetConnected");
        if (linearLayout.getVisibility() == View.VISIBLE)
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    linearLayout.setVisibility(View.GONE);
                }
            });
    }

    @Override
    public void onNetError() {
        Log.i("tanyang", "onNetConnected");
        if (linearLayout.getVisibility() != View.VISIBLE)
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    linearLayout.setVisibility(View.VISIBLE);
                }
            });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().getNetStateListener().unSubscribe(this);
    }
}
