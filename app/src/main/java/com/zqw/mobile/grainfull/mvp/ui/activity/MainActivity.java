package com.zqw.mobile.grainfull.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.widget.ImageView;

import com.blankj.utilcode.util.ActivityUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.mikepenz.itemanimators.AlphaCrossFadeAnimator;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.qiangxi.checkupdatelibrary.CheckUpdateOption;
import com.qiangxi.checkupdatelibrary.Q;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.global.AccountManager;
import com.zqw.mobile.grainfull.app.global.Constant;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.app.utils.EventBusTags;
import com.zqw.mobile.grainfull.app.utils.RxUtils;
import com.zqw.mobile.grainfull.app.utils.icons.ZQWFont;
import com.zqw.mobile.grainfull.di.component.DaggerMainComponent;
import com.zqw.mobile.grainfull.mvp.contract.MainContract;
import com.zqw.mobile.grainfull.mvp.model.entity.AppUpdate;
import com.zqw.mobile.grainfull.mvp.model.entity.MainEvent;
import com.zqw.mobile.grainfull.mvp.presenter.MainPresenter;
import com.zqw.mobile.grainfull.mvp.ui.fragment.HomeFragment;
import com.zqw.mobile.grainfull.mvp.ui.fragment.SpecialEffectsFragment;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:首页
 * ================================================
 */
public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {
    /*--------------------------------控件信息--------------------------------*/
    @BindView(R.id.bottom_navigation)
    BottomNavigationView mNavigation;                                                               // 底部导航
    /*--------------------------------业务信息--------------------------------*/
    private static final String POSITION = "position";
    private static final String SELECT_ITEM = "bottomNavigationSelectItem";
    private int position;                                                                           // 保存当前tab下标
    private long firstClickTime = 0;                                                                // 双击刷新功能 - 记录点击时间

    // 首页
    private static final int FRAGMENT_HOME = 0;
    // 统计
    private static final int FRAGMENT_STATISTICS = 1;
    // 其它
    private static final int FRAGMENT_REGISTER = 2;

    @Inject
    ImageLoader mImageLoader;                                                                       // 操作图片对象
    @Inject
    AccountManager mAccountManager;                                                                 // 账号对象

    // 首页(业务)
    HomeFragment mTabHome;
    // 特效
    SpecialEffectsFragment mTabSpecialEffects;

    // 侧滑Menu 之 头部 对象
    private AccountHeader headerResult = null;
    // 侧滑Menu 之 内容 对象
    private Drawer result = null;

    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        this.mTabHome = null;

        this.headerResult = null;
        this.result = null;
        this.mAccountManager = null;
    }

    /**
     * 状态栏目透明
     */
    @Override
    public int useStatusBarColor() {
        return -1;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerMainComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

        // 初始化
        mNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_business:
                    menuChecked(true);
                    showFragment(FRAGMENT_HOME);
                    doubleClick(FRAGMENT_HOME);
                    break;
                case R.id.action_statistics:
                    menuChecked(false);
                    showFragment(FRAGMENT_STATISTICS);
                    break;
                case R.id.action_incoming:
                    menuChecked(true);
                    showFragment(FRAGMENT_REGISTER);
                    break;
            }
            return true;
        });

        if (savedInstanceState != null) {
            mTabHome = (HomeFragment) getSupportFragmentManager().findFragmentByTag(HomeFragment.class.getName());

            // 恢复 recreate 前的位置
            showFragment(savedInstanceState.getInt(POSITION));
            mNavigation.setSelectedItemId(savedInstanceState.getInt(SELECT_ITEM));
        } else {
            showFragment(FRAGMENT_HOME);
        }

        // 控制模块权限
        initModulePermissions();

        // 初始化Menu侧滑
        initMaterialDrawer(savedInstanceState);

        // 初始化业务部分
        if (mPresenter != null) {
            mPresenter.initPresenter();
        }
    }

    /**
     * 控制模块权限
     */
    private void initModulePermissions() {

    }

    /**
     * 初始化Menu
     */
    private void initMaterialDrawer(@Nullable Bundle savedInstanceState) {
        final IProfile profile;
        // Create a few sample profile
        // NOTE you have to define the loader logic too. See the CustomApplication for more details
        if (TextUtils.isEmpty(mAccountManager.getPhotoUrl())) {
            profile = new ProfileDrawerItem().withName(mAccountManager.getUserName()).withEmail(mAccountManager.getAccount()).withIcon(R.drawable.profile6).withIdentifier(Constant.MAIN_AVATAR);
        } else {
            profile = new ProfileDrawerItem().withName(mAccountManager.getUserName()).withEmail(mAccountManager.getAccount()).withIcon(mAccountManager.getPhotoUrl()).withIdentifier(Constant.MAIN_AVATAR);
        }

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTextColor(Color.BLACK)
                .withTranslucentStatusBar(true)
                .withSelectionListEnabled(false)
                .addProfiles(
                        profile
                )
                .withOnAccountHeaderListener((view, profile1, current) -> {
                    //sample usage of the onProfileChanged listener
                    //if the clicked item has the identifier 1 add a new profile ;)
                    if (profile1 instanceof IDrawerItem) {
//                        onTabSelected(Constant.MAIN_AVATAR);
//                        showMessage(profile1.getName().getText(getActivity()));
                        return true;
                    }
                    //false if you have not consumed the event and it should close the drawer
                    return false;
                })
                .withSavedInstance(savedInstanceState)
                .build();

        IDrawerItem basicInfo = new PrimaryDrawerItem().withName(R.string.drawer_item_basicinfo).withIcon(ZQWFont.Icon.zqw_menu_basicinfo).withIdentifier(Constant.MAIN_BASICINFO).withSelectable(false);

        IDrawerItem myPromotionCode = null;
//            myPromotionCode = new PrimaryDrawerItem().withName(R.string.drawer_item_mypromotioncode).withIcon(ZQWFont.Icon.zqw_menu_promotioncode).withIdentifier(Constant.MAIN_MYPROMOTIONCODE).withSelectable(false);

        IDrawerItem line = null;
        if (basicInfo != null || myPromotionCode != null) {
            line = new DividerDrawerItem();
        }

        //Create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withHasStableIds(true)
                .withFullscreen(true)                                                               // 全屏
                .withItemAnimator(new AlphaCrossFadeAnimator())
                .withAccountHeader(headerResult)                                                    // 设置左侧头部标题
                .addDrawerItems(
                        basicInfo,
                        myPromotionCode,
                        line,
                        new SecondaryDrawerItem().withName(R.string.drawer_item_setting).withIcon(ZQWFont.Icon.zqw_menu_setting).withIdentifier(Constant.MAIN_SETTING).withSelectable(false),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_about).withIcon(ZQWFont.Icon.zqw_menu_about).withIdentifier(Constant.MAIN_ABOUT).withSelectable(false)

                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    //check if the drawerItem is set.
                    //there are different reasons for the drawerItem to be null
                    //--> click on the header
                    //--> click on the footer
                    //those items don't contain a drawerItem

                    if (drawerItem instanceof Nameable) {
                        if (drawerItem.getIdentifier() == Constant.MAIN_NIGHTMODE) {
                            // 切换 夜间模式 时不关闭Drawer
                            return true;
                        } else
                            onTabSelected(drawerItem.getIdentifier());
                    }

                    return false;
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)                                                  // 第一次启动时默认打开抽屉
                .build();

        // 全屏窗口
        if (Build.VERSION.SDK_INT >= 19) {
            result.getDrawerLayout().setFitsSystemWindows(false);
        }

        //only set the active selection or active profile if we do not recreate the activity
        if (savedInstanceState == null) {
            // set the selection to the item with the identifier 11
//            result.setSelection(1, false);
            // 默认选中
            result.setSelection(Constant.MAIN_BASICINFO, false);

            //set the active profile
            headerResult.setActiveProfile(profile);
        }

    }

    /**
     * 切换标签(Fragment)
     */
    private void onTabSelected(long position) {
        // 各别窗口采用延迟跳转方式
        RxUtils.startDelayed((long) 0.5, this, () -> {
            if (position == Constant.MAIN_AVATAR) {                                                 // 头像

            } else if (position == Constant.MAIN_BASICINFO) {                                       // 基本信息
//                ActivityUtils.startActivity(BasicInfoActivity.class);
            } else if (position == Constant.MAIN_SETTING) {                                         // 设置
                ActivityUtils.startActivity(SettingActivity.class);
            } else if (position == Constant.MAIN_ABOUT) {                                           // 关于
                ActivityUtils.startActivity(AboutActivity.class);
            }
        });
    }

    /**
     * 双击刷新界面
     */
    public void doubleClick(int index) {
        long secondClickTime = System.currentTimeMillis();
        if ((secondClickTime - firstClickTime < 500)) {
            if (index == FRAGMENT_HOME) {
                mTabHome.onDoubleClick();
            }
        } else {
            firstClickTime = secondClickTime;
        }
    }

    /**
     * 显示Fragment
     */
    private void showFragment(int index) {
        this.position = index;

        // 第一步，隐藏所有Fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        hideFragment(ft);

        // 第二步，显示指定的Fragment
        switch (index) {
            case FRAGMENT_HOME:
                // 如果Fragment为空，就新建一个实例
                // 如果不为空，就将它从栈中显示出来
                if (mTabHome == null) {
                    mTabHome = new HomeFragment();
                    ft.add(R.id.container, mTabHome, HomeFragment.class.getName());
                } else {
                    ft.show(mTabHome);
                }
                break;
            case FRAGMENT_STATISTICS:
//                if (mTabStatistics == null) {
//                    mTabStatistics = new StatisticsFragment();
//                    ft.add(R.id.container, mTabStatistics, StatisticsFragment.class.getName());
//                } else {
//                    ft.show(mTabStatistics);
//                }
                break;
            case FRAGMENT_REGISTER:
                if (mTabSpecialEffects == null) {
                    mTabSpecialEffects = new SpecialEffectsFragment();
                    ft.add(R.id.container, mTabSpecialEffects, SpecialEffectsFragment.class.getName());
                } else {
                    ft.show(mTabSpecialEffects);
                }
        }

        ft.commit();
    }

    /**
     * 隐藏Fragment
     */
    private void hideFragment(FragmentTransaction ft) {
        // 如果不为空，就先隐藏起来
        if (mTabHome != null) {
            ft.hide(mTabHome);
        }
//        if (mTabStatistics != null) {
//            ft.hide(mTabStatistics);
//        }
        if (mTabSpecialEffects != null) {
            ft.hide(mTabSpecialEffects);
        }
    }

    /**
     * 首页监听    回调
     */
    @Subscriber(tag = EventBusTags.HOME_TAG, mode = ThreadMode.POST)
    private void eventBusEvent(MainEvent mainEvent) {
        if (mainEvent.getCode() == EventBusTags.OPEN_MENU_TAG) {
            if (result != null)
                result.openDrawer();
        }

    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
        // recreate 时记录当前位置 (在 Manifest 已禁止 Activity 旋转,所以旋转屏幕并不会执行以下代码)
        outState.putInt(POSITION, position);
        outState.putInt(SELECT_ITEM, mNavigation.getSelectedItemId());
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            CommonUtils.exitSys(getApplicationContext());
        }
    }

    /**
     * 发现新版本提示是否更新
     */
    @Override
    public void mainAskDialog(AppUpdate info) {
        Q.show(this, new CheckUpdateOption.Builder()
                .setAppName(info.getName())
                .setFileName("/" + info.getFileName())
                .setFilePath(Constant.APP_UPDATE_PATH)
//                .setImageUrl("http://imgsrc.baidu.com/imgad/pic/item/6c224f4a20a446233d216c4f9322720e0cf3d730.jpg")
                .setImageResId(R.mipmap.icon_upgrade_logo)
                .setIsForceUpdate(info.getForce() == 1)
                .setNewAppSize(info.getNewAppSize())
                .setNewAppUpdateDesc(info.getNewAppUpdateDesc())
                .setNewAppUrl(info.getFilePath())
                .setNewAppVersionName(info.getVerName())
                .setNotificationSuccessContent("下载成功，点击安装")
                .setNotificationFailureContent("下载失败，点击重新下载")
                .setNotificationIconResId(R.mipmap.ic_launcher)
                .setNotificationTitle(getString(R.string.app_name))
                .build(), (view, imageUrl) -> {
            // 下载图片
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            mImageLoader.loadImage(getActivity(),
                    ImageConfigImpl
                            .builder()
                            .url(imageUrl)
                            .imageView(view)
                            .build());
        });
    }

    /**
     * 取消Menu选中项
     */
    private void menuChecked(boolean isValue) {
        if (isValue) {
            if (mNavigation != null) {
                mNavigation.getMenu().setGroupCheckable(R.id.group_item, false, true);
                mNavigation.getMenu().setGroupCheckable(0, true, true);
            }
        } else {
            if (mNavigation != null) {
                mNavigation.getMenu().setGroupCheckable(0, false, true);
                mNavigation.getMenu().setGroupCheckable(R.id.group_item, true, true);
            }

        }

    }
}
