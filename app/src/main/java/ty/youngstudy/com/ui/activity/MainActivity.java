package ty.youngstudy.com.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import ty.youngstudy.com.R;
import ty.youngstudy.com.ui.activity.base.BaseActivity;
import ty.youngstudy.com.adapter.FragmentAdapter;
import ty.youngstudy.com.ui.activity.reader.NovelMainActivity;
import ty.youngstudy.com.ui.fragment.TabFragment;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static long DOUBLE_CLICK_TIME = 0L;
    private TabLayout mTabLayout;
    private ViewPager frg_viewPager;

//    @BindArray(R.array.tab_char)
//    String[] tabCharList;

//    @BindView(R.id.tab_main_id)
//    TabLayout mTabLayout;
//
//    @BindView(R.id.frg_viewPager_id)
//    ViewPager frg_viewPager;

    String[] tabCharList = new String[] {"微信","发现","朋友","我的"};
    int[] tabDrawList = new int[]{R.drawable.selector_tab_weixin, R.drawable.selector_tab_find,
            R.drawable.selector_tab_friend, R.drawable.selector_tab_me};

    private ArrayList<Fragment> mFragmentList;
    private ArrayList<String> mTitleList;
    private FragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getFirstStart()) {
            readyGoThenKill(FirstActivity.class);
        }
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mTabLayout = (TabLayout) findViewById(R.id.tab_main1_id);
        frg_viewPager = (ViewPager) findViewById(R.id.frg_viewPager_id);
        mTitleList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            mTitleList.add(tabCharList[i]);
        }

        mFragmentList = new ArrayList<>();

        for (int i = 0; i < mTitleList.size(); i++) {
            mFragmentList.add(TabFragment.newInstance(i));
        }

        adapter = new FragmentAdapter(getSupportFragmentManager(),mFragmentList,mTitleList);
        frg_viewPager.setAdapter(adapter);
        frg_viewPager.setCurrentItem(0);
        /* 初始化TabView */
        initTab();

/*        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public View getLoadingView() {
        return ButterKnife.findById(this,R.id.home_snacker_id);
    }

    private void initTab() {
        mTabLayout.setupWithViewPager(frg_viewPager);
        mTabLayout.setSelectedTabIndicatorHeight(0);
        for (int i = 0; i < mTitleList.size(); i++) {
            TabLayout.Tab itemTab = mTabLayout.getTabAt(i);
            if (itemTab != null) {
                itemTab.setCustomView(R.layout.tab_item_layout);
                TextView tv_tab = (TextView) itemTab.getCustomView().findViewById(R.id.tv_tab_find_id);
                tv_tab.setText(mTitleList.get(i));
                ImageView iv_tab = (ImageView) itemTab.getCustomView().findViewById(R.id.iv_tab_find_id);
                iv_tab.setImageResource(tabDrawList[i]);
            }
        }
        mTabLayout.getTabAt(0).getCustomView().setSelected(true);
    }

    @Override
    public boolean getFirstStart() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sp.getBoolean("first_start", true);
    }

    @Override
    public void initViewAndEvents() {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if ((System.currentTimeMillis() - DOUBLE_CLICK_TIME) > 2000) {
                Snackbar.make(getLoadingView(),"请再次点击返回键",Snackbar.LENGTH_LONG).setAction("Action",null).show();
                DOUBLE_CLICK_TIME = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_novel) {
            // Handle the novel action
            readyGo(NovelMainActivity.class);
        } else if (id == R.id.nav_gallery) {
            Intent slideIntent = new Intent("android.intent.action.slide");
            slideIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(slideIntent);
        } else if (id == R.id.nav_slideshow) {
            readyGo(LoginActivity.class);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
