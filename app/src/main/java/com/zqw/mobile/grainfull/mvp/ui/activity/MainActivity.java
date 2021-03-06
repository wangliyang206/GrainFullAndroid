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
 * Description:??????
 * ================================================
 */
public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {
    /*--------------------------------????????????--------------------------------*/
    @BindView(R.id.bottom_navigation)
    BottomNavigationView mNavigation;                                                               // ????????????
    /*--------------------------------????????????--------------------------------*/
    private static final String POSITION = "position";
    private static final String SELECT_ITEM = "bottomNavigationSelectItem";
    private int position;                                                                           // ????????????tab??????
    private long firstClickTime = 0;                                                                // ?????????????????? - ??????????????????

    // ??????
    private static final int FRAGMENT_HOME = 0;
    // ??????
    private static final int FRAGMENT_STATISTICS = 1;
    // ??????
    private static final int FRAGMENT_REGISTER = 2;

    @Inject
    ImageLoader mImageLoader;                                                                       // ??????????????????
    @Inject
    AccountManager mAccountManager;                                                                 // ????????????

    // ??????(??????)
    HomeFragment mTabHome;
    // ??????
    SpecialEffectsFragment mTabSpecialEffects;

    // ??????Menu ??? ?????? ??????
    private AccountHeader headerResult = null;
    // ??????Menu ??? ?????? ??????
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
     * ??????????????????
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

        // ?????????
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

            // ?????? recreate ????????????
            showFragment(savedInstanceState.getInt(POSITION));
            mNavigation.setSelectedItemId(savedInstanceState.getInt(SELECT_ITEM));
        } else {
            showFragment(FRAGMENT_HOME);
        }

        // ??????????????????
        initModulePermissions();

        // ?????????Menu??????
        initMaterialDrawer(savedInstanceState);

        // ?????????????????????
        if (mPresenter != null) {
            mPresenter.initPresenter();
        }
    }

    /**
     * ??????????????????
     */
    private void initModulePermissions() {

    }

    /**
     * ?????????Menu
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
                .withFullscreen(true)                                                               // ??????
                .withItemAnimator(new AlphaCrossFadeAnimator())
                .withAccountHeader(headerResult)                                                    // ????????????????????????
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
                            // ?????? ???????????? ????????????Drawer
                            return true;
                        } else
                            onTabSelected(drawerItem.getIdentifier());
                    }

                    return false;
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)                                                  // ????????????????????????????????????
                .build();

        // ????????????
        if (Build.VERSION.SDK_INT >= 19) {
            result.getDrawerLayout().setFitsSystemWindows(false);
        }

        //only set the active selection or active profile if we do not recreate the activity
        if (savedInstanceState == null) {
            // set the selection to the item with the identifier 11
//            result.setSelection(1, false);
            // ????????????
            result.setSelection(Constant.MAIN_BASICINFO, false);

            //set the active profile
            headerResult.setActiveProfile(profile);
        }

    }

    /**
     * ????????????(Fragment)
     */
    private void onTabSelected(long position) {
        // ????????????????????????????????????
        RxUtils.startDelayed((long) 0.5, this, () -> {
            if (position == Constant.MAIN_AVATAR) {                                                 // ??????

            } else if (position == Constant.MAIN_BASICINFO) {                                       // ????????????
//                ActivityUtils.startActivity(BasicInfoActivity.class);
            } else if (position == Constant.MAIN_SETTING) {                                         // ??????
                ActivityUtils.startActivity(SettingActivity.class);
            } else if (position == Constant.MAIN_ABOUT) {                                           // ??????
                ActivityUtils.startActivity(AboutActivity.class);
            }
        });
    }

    /**
     * ??????????????????
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
     * ??????Fragment
     */
    private void showFragment(int index) {
        this.position = index;

        // ????????????????????????Fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        hideFragment(ft);

        // ???????????????????????????Fragment
        switch (index) {
            case FRAGMENT_HOME:
                // ??????Fragment??????????????????????????????
                // ????????????????????????????????????????????????
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
     * ??????Fragment
     */
    private void hideFragment(FragmentTransaction ft) {
        // ????????????????????????????????????
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
     * ????????????    ??????
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
        // recreate ????????????????????? (??? Manifest ????????? Activity ??????,?????????????????????????????????????????????)
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
     * ?????????????????????????????????
     */
    @Override
    public void mainAskDialog(AppUpdate info) {
//        Q.show(this, new CheckUpdateOption.Builder()
//                .setAppName(info.getName())
//                .setFileName("/" + info.getFileName())
//                .setFilePath(Constant.APP_UPDATE_PATH)
////                .setImageUrl("http://imgsrc.baidu.com/imgad/pic/item/6c224f4a20a446233d216c4f9322720e0cf3d730.jpg")
//                .setImageResId(R.mipmap.icon_upgrade_logo)
//                .setIsForceUpdate(info.getForce() == 1)
//                .setNewAppSize(info.getNewAppSize())
//                .setNewAppUpdateDesc(info.getNewAppUpdateDesc())
//                .setNewAppUrl(info.getFilePath())
//                .setNewAppVersionName(info.getVerName())
//                .setNotificationSuccessContent("???????????????????????????")
//                .setNotificationFailureContent("?????????????????????????????????")
//                .setNotificationIconResId(R.mipmap.ic_launcher)
//                .setNotificationTitle(getString(R.string.app_name))
//                .build(), (view, imageUrl) -> {
//            // ????????????
//            view.setScaleType(ImageView.ScaleType.FIT_XY);
//            mImageLoader.loadImage(getActivity(),
//                    ImageConfigImpl
//                            .builder()
//                            .url(imageUrl)
//                            .imageView(view)
//                            .build());
//        });
    }

    /**
     * ??????Menu?????????
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
