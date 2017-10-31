package ty.youngstudy.com.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import ty.youngstudy.com.DataHelper;
import ty.youngstudy.com.R;
import ty.youngstudy.com.adapter.FragmentAdapter;
import ty.youngstudy.com.database.UserInfo;
import ty.youngstudy.com.ui.activity.base.BaseActivity;
import ty.youngstudy.com.ui.activity.reader.NovelMainActivity;
import ty.youngstudy.com.ui.fragment.TabFragment;
import ty.youngstudy.com.widget.RoundImageView;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static long DOUBLE_CLICK_TIME = 0L;
    private TabLayout mTabLayout;
    private ViewPager frg_viewPager;
    private List<UserInfo> userInfos;
    private UserInfo userInfo;

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

    private RoundImageView roundImageView;
    private RelativeLayout person_info_layout;
    private ArrayList<Fragment> mFragmentList;
    private ArrayList<String> mTitleList;
    private FragmentAdapter adapter;
    private List<LocalMedia> selectList;
    BitmapFactory.Options options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getFirstStart()) {
            readyGoThenKill(FirstActivity.class);
        }
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inSampleSize = 2;
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
        userInfos = DataSupport.where("userName = ?","tanyang").find(UserInfo.class);
        if (userInfos != null && userInfos.size()!=0){
            userInfo = userInfos.get(0);
        }

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

        roundImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(MainActivity.this)
                        .openGallery(PictureMimeType.ofAll())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                        .maxSelectNum(1)// 最大图片选择数量 int
                        .minSelectNum(0)// 最小选择数量 int
                        .imageSpanCount(4)// 每行显示个数 int
                        .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                        .previewImage(true)// 是否可预览图片 true or false
                        .previewVideo(true)// 是否可预览视频 true or false
                        .enablePreviewAudio(true) // 是否可播放音频 true or false
                        .isCamera(true)// 是否显示拍照按钮 true or false
                        .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                        .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                        .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                        .enableCrop(true)// 是否裁剪 true or false
                        .compress(true)// 是否压缩 true or false
                        .compressMode(PictureConfig.SYSTEM_COMPRESS_MODE)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                        .withAspectRatio(1,1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                        .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                        .isGif(true)// 是否显示gif图片 true or false
                        .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                        .circleDimmedLayer(true)// 是否圆形裁剪 true or false
                        .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                        .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                        .openClickSound(false)// 是否开启点击声音 true or false
                        //.selectionMedia()// 是否传入已选图片 List<LocalMedia> list
                        .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                        .cropCompressQuality(90)// 裁剪压缩质量 默认90 int
                        //.compressMaxKB(Luban.CUSTOM_GEAR)//压缩最大值kb compressGrade()为Luban.CUSTOM_GEAR有效 int
                        //.compressWH() // 压缩宽高比 compressGrade()为Luban.CUSTOM_GEAR有效  int
                        //.cropWH(80,80)// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                        //.rotateEnabled() // 裁剪是否可旋转图片 true or false
                        .scaleEnabled(false)// 裁剪是否可放大缩小图片 true or false
                        //.videoQuality()// 视频录制质量 0 or 1 int
                        .videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
                        .videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
                        //.recordVideoSecond()//视频秒数录制 默认60s int
                        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
            }
        });
        if (userInfo != null){
            byte[] head = userInfo.getUserHead();
            Bitmap bitmap = BitmapFactory.decodeByteArray(head,0,head.length,options);
            roundImageView.setImageBitmap(bitmap);
        }
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
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        roundImageView = (RoundImageView) navigationView.getHeaderView(0).findViewById(R.id.headerImgView);
        person_info_layout = (RelativeLayout) navigationView.getHeaderView(0).findViewById(R.id.nav_person_info_layout);
        person_info_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readyGo(UserInfoActivity.class);
            }
        });
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
        }
        else if (id == R.id.nav_gallery) {
            /*Intent slideIntent = new Intent("android.intent.action.slide");
            slideIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(slideIntent);*/
            //PictureSelector.create(MainActivity.this).openGallery(PictureMimeType.ofImage()).forResult(PictureConfig.CHOOSE_REQUEST);
            /*PictureSelector.create(MainActivity.this)
                    .openGallery(PictureMimeType.ofAll())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                    .maxSelectNum(9)// 最大图片选择数量 int
                    .minSelectNum(0)// 最小选择数量 int
                    .imageSpanCount(4)// 每行显示个数 int
                    .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                    .previewImage(true)// 是否可预览图片 true or false
                    .previewVideo(true)// 是否可预览视频 true or false
                    .enablePreviewAudio(true) // 是否可播放音频 true or false
                    .isCamera(true)// 是否显示拍照按钮 true or false
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                    .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                    .enableCrop(true)// 是否裁剪 true or false
                    .compress(false)// 是否压缩 true or false
                    .compressMode(PictureConfig.SYSTEM_COMPRESS_MODE)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                    .withAspectRatio(1,1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                    .isGif(true)// 是否显示gif图片 true or false
                    .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                    .circleDimmedLayer(true)// 是否圆形裁剪 true or false
                    .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                    .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                    .openClickSound(false)// 是否开启点击声音 true or false
                    //.selectionMedia()// 是否传入已选图片 List<LocalMedia> list
                    .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                    //.cropCompressQuality()// 裁剪压缩质量 默认90 int
                    .compressMaxKB(Luban.CUSTOM_GEAR)//压缩最大值kb compressGrade()为Luban.CUSTOM_GEAR有效 int
                    //.compressWH() // 压缩宽高比 compressGrade()为Luban.CUSTOM_GEAR有效  int
                    .cropWH(80,80)// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                    //.rotateEnabled() // 裁剪是否可旋转图片 true or false
                    .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                    //.videoQuality()// 视频录制质量 0 or 1 int
                    .videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
                    .videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
                    //.recordVideoSecond()//视频秒数录制 默认60s int
                    .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code*/
        } else if (id == R.id.nav_slideshow) {
            readyGo(LoginActivity.class);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        item.setChecked(false);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    if (selectList != null && selectList.size() > 0) {
                        LocalMedia media = selectList.get(0);
                        String cutPath = media.getCompressPath();
                        Log.i("tanyang", "path :" + cutPath);
                        Bitmap bmp = BitmapFactory.decodeFile(cutPath,options);
                        roundImageView.setImageBitmap(bmp);
                        DataHelper.updateUserHead("tanyang",bmp);
                    }
                    // 例如 LocalMedia 里面返回三种 path
                    // 1.media.getPath(); 为原图 path
                    // 2.media.getCutPath();为裁剪后 path，需判断 media.isCut();是否为 true
                    // 3.media.getCompressPath();为压缩后 path，需判断 media.isCompressed();是否为 true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    Log.i("tanyang", "onActivityResult:" + selectList.size());
                    break;
            }
        }
    }
}
