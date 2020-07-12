package ty.youngstudy.com.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import ty.youngstudy.com.R;
import ty.youngstudy.com.adapter.FragmentAdapter;
import ty.youngstudy.com.database.UserInfo;
import ty.youngstudy.com.manager.UserManager;
import ty.youngstudy.com.ui.activity.base.BaseActivity;
import ty.youngstudy.com.ui.activity.reader.NovelMainActivity;
import ty.youngstudy.com.ui.fragment.TabFragment;
import ty.youngstudy.com.zxing.CaptureActivity;
import ty.youngstudy.com.zxing.Intents;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private static long DOUBLE_CLICK_TIME = 0L;
    private final static int SCAN_RESULT = 100;
    private TabLayout mTabLayout;
    private ViewPager frg_viewPager;
    private List<UserInfo> userInfos;
    private UserInfo userInfo;

    String[] tabCharList = new String[] {"聊天","发现","朋友","我的"};
    int[] tabDrawList = new int[]{R.drawable.selector_tab_weixin, R.drawable.selector_tab_find,
            R.drawable.selector_tab_friend, R.drawable.selector_tab_me};

    private ImageView circleImageView;
    private TextView tv_nav_Name;
    private TextView tv_nav_Email;
    private SearchView searchView;
    private RelativeLayout person_info_layout;
    private ArrayList<Fragment> mFragmentList;
    private ArrayList<String> mTitleList;
    private FragmentAdapter adapter;
    private List<LocalMedia> selectList;
    BitmapFactory.Options options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        mFragmentList.clear();
        /*for (int i = 0; i < mTitleList.size(); i++) {
            mFragmentList.add(TabFragment.newInstance(i));
        }*/
//        mFragmentList.add(new RecentContactsFragment());
        mFragmentList.add(TabFragment.newInstance(1));
//        mFragmentList.add(new ContactsFragment());
        mFragmentList.add(TabFragment.newInstance(3));

        adapter = new FragmentAdapter(getSupportFragmentManager(),mFragmentList,mTitleList);
        frg_viewPager.setAdapter(adapter);
        frg_viewPager.setCurrentItem(0);
        frg_viewPager.setOffscreenPageLimit(3);
        /* 初始化TabView */
        initTab();
        /*userInfos = DataSupport.where("userName = ?","tanyang").find(UserInfo.class);
        if (userInfos != null && userInfos.size()!=0){
            userInfo = userInfos.get(0);
        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        circleImageView.setOnClickListener(new View.OnClickListener() {
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
                        .cropCompressQuality(100)// 裁剪压缩质量 默认90 int
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
        return false;
    }

    @Override
    public void initViewAndEvents() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        circleImageView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.headerImgView);
        tv_nav_Name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_nav_name);
        tv_nav_Name.setText(UserManager.getInstance().getUser_nick());
        person_info_layout = (RelativeLayout) navigationView.getHeaderView(0).findViewById(R.id.nav_person_info_layout);
        person_info_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readyGo(UserInfoActivity.class);
            }
        });
        BmobFile head = UserManager.getInstance().getUser_head();
        if (head != null) {
            String url = UserManager.getInstance().getUser_head().getUrl();
            Log.d(TAG, "url = " + url);
            try {
                Glide.with(MainActivity.this).load(url).into(circleImageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
        switch (id){
            case R.id.search_btn:
                // TODO: 2017/12/29 进入联系搜索界面
                break;
            case R.id.action_find_people:
//                FindPeopleActivity.start(MainActivity.this);
                break;
            case R.id.scan:
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                intent.setAction(Intents.Scan.ACTION);
                intent.putExtra(Intents.Scan.CAMERA_ID,0);
                intent.putExtra(Intents.Scan.HEIGHT,500);
                intent.putExtra(Intents.Scan.WIDTH,500);
                startActivityForResult(intent,SCAN_RESULT);
                break;
            default:
                break;
        
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
    protected void onDestroy() {
        super.onDestroy();
        // 退出云信
//        NimUIKit.logout();
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
                        final String cutPath = media.getCompressPath();
                        Log.i("tanyang", "path :" + cutPath);
                        Bitmap bmp = BitmapFactory.decodeFile(cutPath,options);
                        circleImageView.setImageBitmap(bmp);
                        File file =new File(cutPath);
                        final BmobFile bmobFile = new BmobFile(file);
                        UserManager.getInstance().setUser_head(bmobFile);
                        UserManager.getInstance().upload(MainActivity.this, new UserManager.UserListener() {
                            @Override
                            public void onSuccess() {
                                final String url = bmobFile.getFileUrl();
                                String accid = UserManager.getInstance().getYx_account();
                                Log.d(TAG,"accid = "+accid);
                            }
                            @Override
                            public void onFailed(BmobException e) {

                            }
                        });
                    }
                    // 例如 LocalMedia 里面返回三种 path
                    // 1.media.getPath(); 为原图 path
                    // 2.media.getCutPath();为裁剪后 path，需判断 media.isCut();是否为 true
                    // 3.media.getCompressPath();为压缩后 path，需判断 media.isCompressed();是否为 true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    Log.i("tanyang", "onActivityResult:" + selectList.size());
                    break;
                case SCAN_RESULT:
                    Log.d(TAG,"结束");
                    String result = data.getStringExtra(Intents.Scan.RESULT);
                    String format = data.getStringExtra(Intents.Scan.RESULT_FORMAT);
                    Log.d(TAG,"result = "+result + ",,,format = "+format);

                    break;
            }
        }
    }
}
