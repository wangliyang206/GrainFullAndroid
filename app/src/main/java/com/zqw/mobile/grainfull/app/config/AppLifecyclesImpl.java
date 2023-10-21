/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zqw.mobile.grainfull.app.config;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;


import com.baidu.mapapi.SDKInitializer;
import com.blankj.utilcode.util.Utils;
import com.jess.arms.base.delegate.AppLifecycles;
import com.jess.arms.http.imageloader.glide.GlideArms;
import com.jess.arms.integration.cache.IntelligentCache;
import com.jess.arms.utils.ArmsUtils;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.commonsdk.UMConfigure;
import com.zqw.mobile.grainfull.BuildConfig;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.global.Constant;
import com.zqw.mobile.grainfull.app.service.LocationService;
import com.zqw.mobile.grainfull.app.service.MyLocationListener;
import com.zqw.mobile.grainfull.app.utils.FileLoggingTree;

import org.jetbrains.annotations.NotNull;

import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * ================================================
 * 展示 {@link AppLifecycles} 的用法
 * <p>
 * Created by JessYan on 04/09/2017 17:12
 * ================================================
 */
public class AppLifecyclesImpl implements AppLifecycles {

    @Override
    public void attachBaseContext(@NonNull Context base) {
        // 这里比 onCreate 先执行,常用于 MultiDex 初始化,插件化框架的初始化
        MultiDex.install(base);
    }

    @Override
    public void onCreate(@NonNull Application application) {
        // 初始化工具类
        Utils.init(application);

        initTimber();

        initBugly(application);

        // 初始化百度地图
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(application);

        // 初始化定位
        initLocation(application);

        initLeakCanary(application);

        // 初始化MaterialDrawer
        initMaterialDrawer();

        // 初始化友盟统计
        initUM(application);
    }

    @Override
    public void onTerminate(@NonNull Application application) {

    }

    /**
     * 初始化log
     */
    private void initTimber() {
        if (BuildConfig.DEBUG) {
            // 测试环境
            // Timber 是一个日志框架容器,外部使用统一的Api,内部可以动态的切换成任何日志框架(打印策略)进行日志打印
            // 并且支持添加多个日志框架(打印策略),做到外部调用一次 Api,内部却可以做到同时使用多个策略
            // 比如添加三个策略,一个打印日志,一个将日志保存本地,一个将日志上传服务器
            Timber.plant(new Timber.DebugTree() {
                @Override
                protected String createStackElementTag(@NotNull StackTraceElement element) {
                    return super.createStackElementTag(element) + ":" + element.getLineNumber();
                }
            });
            // 如果你想将框架切换为 Logger 来打印日志,请使用下面的代码,如想切换为其他日志框架请根据下面的方式扩展
//                    Logger.addLogAdapter(new AndroidLogAdapter());
//                    Timber.plant(new Timber.DebugTree() {
//                        @Override
//                        protected void log(int priority, String tag, String message, Throwable t) {
//                            Logger.log(priority, tag, message, t);
//                        }
//                    });
            ButterKnife.setDebug(true);

        } else {
            // 正式环境
            Timber.plant(new FileLoggingTree());
        }

    }

    /**
     * 初始化Bugly
     */
    private void initBugly(Application application) {
        // 初始化腾讯Bugly SDK
        CrashReport.initCrashReport(application, BuildConfig.BUGLY_APP_ID, BuildConfig.DEBUG);
    }

    /**
     * 初始始化定位
     */
    private void initLocation(Application application) {
        // 验证一下经纬度，如果没有经纬度则需要初始一个默认经纬度
//        AccountManager accountManager = new AccountManager(application);
//        if (TextUtils.isEmpty(accountManager.getProvince())) {
//            accountManager.updateLocation(38.031693, 114.540032, 72.81403f, "河北省", "石家庄市", "裕华区", "中国河北省石家庄市裕华区体育南大街227号");
//        }

        LocationService locationService = new LocationService(application);
        // 配置定位信息
//        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        // 定位回调监听
        locationService.registerListener(new MyLocationListener(application));
        // 开启定位
//        locationService.start();

        // 存储到内存中
        ArmsUtils.obtainAppComponentFromContext(application).extras().put(IntelligentCache.getKeyOfKeep(LocationService.class.getName()), locationService);
    }

    /**
     * leakCanary内存泄露检查
     */
    private void initLeakCanary(Application application) {
        //LeakCanary v2.0+ 版本会自动完成框架的初始化, 以及对 Activity#onDestroy、Fragment#onDestroy、Fragment#onDestroyView 的监听
        //原理和 AndroidAutoSize 一致, 所以注释掉下面 v1.0 的初始化代码
        //使用 IntelligentCache.KEY_KEEP 作为 key 的前缀, 可以使储存的数据永久存储在内存中
        //否则存储在 LRU 算法的存储空间中, 前提是 extras 使用的是 IntelligentCache (框架默认使用)
//        ArmsUtils.obtainAppComponentFromContext(application).extras()
//                .put(IntelligentCache.getKeyOfKeep(RefWatcher.class.getName())
//                        , BuildConfig.USE_CANARY ? LeakCanary.install(application) : RefWatcher.DISABLED);
    }

    /**
     * 初始化MaterialDrawer
     */
    private void initMaterialDrawer() {

        //initialize and create the image loader logic
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder, String tag) {
                GlideArms.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                GlideArms.with(imageView.getContext()).clear(imageView);
            }

            @Override
            public Drawable placeholder(Context ctx, String tag) {
                //define different placeholders for different imageView targets
                //default tags are accessible via the DrawerImageLoader.Tags
                //custom ones can be checked via string. see the CustomUrlBasePrimaryDrawerItem LINE 111
                if (DrawerImageLoader.Tags.PROFILE.name().equals(tag)) {
                    return DrawerUIUtils.getPlaceHolder(ctx);
                } else if (DrawerImageLoader.Tags.ACCOUNT_HEADER.name().equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(com.mikepenz.materialdrawer.R.color.primary).sizeDp(56);
                } else if ("customUrlItem".equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(R.color.md_red_500).sizeDp(56);
                }

                //we use the default one for
                //DrawerImageLoader.Tags.PROFILE_DRAWER_ITEM.name()

                return super.placeholder(ctx, tag);
            }
        });
    }

    /**
     * 初始化友盟统计
     */
    private void initUM(@NonNull Application application) {
        //设置LOG开关，默认为false
        UMConfigure.setLogEnabled(true);
        // 友盟统计的隐私政策，未通过不可以初始化友盟统计。
        UMConfigure.preInit(application.getApplicationContext(),
                BuildConfig.DEBUG ? application.getString(R.string.um_app_key_debug) : application.getString(R.string.um_app_key),
                Constant.UM_CHANNEL);
    }
}
