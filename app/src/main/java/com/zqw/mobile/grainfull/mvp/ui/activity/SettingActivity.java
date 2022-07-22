package com.zqw.mobile.grainfull.mvp.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.ActivityUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.BuildConfig;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.dialog.CommTipsDialog;
import com.zqw.mobile.grainfull.app.global.AccountManager;
import com.zqw.mobile.grainfull.app.global.Constant;
import com.zqw.mobile.grainfull.app.utils.AppOperator;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.app.utils.DataCleanManager;
import com.zqw.mobile.grainfull.app.utils.EventBusTags;
import com.zqw.mobile.grainfull.di.component.DaggerSettingComponent;
import com.zqw.mobile.grainfull.mvp.contract.SettingContract;
import com.zqw.mobile.grainfull.mvp.model.entity.AppUpdate;
import com.zqw.mobile.grainfull.mvp.model.entity.MainEvent;
import com.zqw.mobile.grainfull.mvp.presenter.SettingPresenter;

import org.simple.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 设置
 */
public class SettingActivity extends BaseActivity<SettingPresenter> implements SettingContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.txvi_settingactivity_clear)
    TextView mClear;                                                                                // 清空缓存

    @BindView(R.id.txvi_settingactivity_version)
    TextView mVersion;                                                                              // 版本号

    @BindView(R.id.btn_settingactivity_out)
    Button mOutLogin;                                                                               // 退出登录
    /*------------------------------------------------业务信息------------------------------------------------*/

    @Inject
    ImageLoader mImageLoader;
    @Inject
    AccountManager mAccountManager;

    // 清空缓存提示
    private CommTipsDialog mClearCacheTips = null;

    // APP升级对话框
    private MaterialDialog mDialog;

    // 已经是最新版本
    private MaterialDialog mDialogTips = null;

    // 退出登录
    private MaterialDialog mOutLoginTips = null;

    @Override
    public void onDestroy() {
        if (mDialog != null) {
            this.mDialog.dismiss();
        }
        if (mDialogTips != null) {
            this.mDialogTips.dismiss();
        }
        if (mOutLoginTips != null) {
            this.mOutLoginTips.dismiss();
        }
        if (mClearCacheTips != null) {
            mClearCacheTips.dismiss();
        }
        super.onDestroy();
        this.mDialog = null;
        this.mDialogTips = null;
        this.mOutLoginTips = null;
        this.mClearCacheTips = null;

        this.mImageLoader = null;
        this.mAccountManager = null;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSettingComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_setting;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        // 设置标题
        setTitle("设置");

        // 设置缓存内容
        mClear.setText(DataCleanManager.getCacheSize(this));

        // 设置版本号
        mVersion.setText(BuildConfig.VERSION_NAME);

        // 初始化Dialog
        mClearCacheTips = new CommTipsDialog(this, "温馨提示", "是否清空缓存？", isVal -> {
            if (isVal) {
                clearAppCache(this, true);
                mClear.setText("0KB");
            }
        });

        // 升级提示
        mDialogTips = new MaterialDialog.Builder(this).title("更新提示").content("已经是最新版本！").positiveText("确定").build();

        // 退出登录
        mOutLoginTips = new MaterialDialog.Builder(this).title("温馨提示").content("确认退出当前账号？").positiveText("确定").negativeText("取消")
                .onPositive((dialog, which) -> {
                    // 清除缓存
                    mAccountManager.clearAccountInfo();
                    // 通知退出登录
                    EventBus.getDefault().post(new MainEvent(EventBusTags.LOGIN_SUCC_TAG), EventBusTags.HOME_TAG);
                    ActivityUtils.startActivity(LoginActivity.class);
                })
                .onNegative((dialog, which) -> {
                }).cancelable(false).build();

        // 初始化Loading对话框
        mDialog = new MaterialDialog.Builder(this).content("正在获取新版本信息，请稍候...").progress(true, 0).build();

        isLogin();
    }

    /**
     * 是否登录
     */
    private void isLogin() {
        if (mAccountManager.isLogin()) {
            mOutLogin.setVisibility(View.VISIBLE);
        } else {
            mOutLogin.setVisibility(View.GONE);
        }
    }

    /**
     * 清除app缓存
     */
    public void clearAppCache(final Context context, boolean showToast) {
        final Handler handler = showToast ? new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    showMessage("缓存清除成功");
                } else {
                    showMessage("缓存清除失败");
                }
            }
        } : null;

        DataCleanManager.clearImageAllCache(context);

        AppOperator.runOnThread(() -> {
            Message msg = new Message();
            try {
                DataCleanManager.cleanApplicationData(context,
                        Constant.IMAGE_PATH,
                        Constant.CACHE_PATH,
                        Constant.APP_UPDATE_PATH,
                        Constant.LOG_PATH,
                        Constant.VIDEO_PATH);

                msg.what = 1;
            } catch (Exception e) {
                msg.what = -1;
            }
            if (handler != null)
                handler.sendMessage(msg);
        });
    }

    @OnClick({
            R.id.lila_settingactivity_clear,                                                        // 清空缓存
            R.id.lila_settingactivity_version,                                                      // 版本号
            R.id.btn_settingactivity_out,                                                           // 退出登录
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lila_settingactivity_clear:                                                   // 清空缓存
                if (mClearCacheTips != null)
                    mClearCacheTips.show();
                break;
            case R.id.lila_settingactivity_version:                                                 // 版本号
                if (!CommonUtils.isDoubleClick()) {
                    if (mPresenter != null) {
                        mPresenter.checkUpdateManager();
                    }
                }
                break;
            case R.id.btn_settingactivity_out:                                                      // 退出登录
                if (mOutLoginTips != null) {
                    mOutLoginTips.show();
                }
                break;
        }
    }

    /**
     * 发现新版本提示是否更新
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
//                .setNotificationSuccessContent("下载成功，点击安装")
//                .setNotificationFailureContent("下载失败，点击重新下载")
//                .setNotificationIconResId(R.mipmap.ic_launcher)
//                .setNotificationTitle(getString(R.string.app_name))
//                .build(), (view, imageUrl) -> {
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
     * 已经是最新版本
     */
    @Override
    public void showLatestDialog() {
        if (mDialogTips != null) {
            mDialogTips.show();
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void showLoading() {
        if (mDialog != null)
            mDialog.show();
    }

    @Override
    public void hideLoading() {
        if (mDialog != null)
            mDialog.hide();
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

    }
}
