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
package com.zqw.mobile.grainfull.app.global;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.jess.arms.base.delegate.AppLifecycles;
import com.jess.arms.di.module.GlobalConfigModule;
import com.jess.arms.http.imageloader.glide.GlideImageLoaderStrategy;
import com.jess.arms.http.log.FormatPrinter;
import com.jess.arms.http.log.RequestInterceptor;
import com.jess.arms.integration.ConfigModule;
import com.zqw.mobile.grainfull.BuildConfig;
import com.zqw.mobile.grainfull.app.config.ActivityLifecycleCallbacksImpl;
import com.zqw.mobile.grainfull.app.config.AppLifecyclesImpl;
import com.zqw.mobile.grainfull.app.config.FragmentLifecycleCallbacksImpl;
import com.zqw.mobile.grainfull.app.config.GlobalHttpHandlerImpl;
import com.zqw.mobile.grainfull.app.config.ResponseErrorListenerImpl;
import com.zqw.mobile.grainfull.app.config.SSLSocketClient;

import java.util.List;
import java.util.concurrent.TimeUnit;

import me.jessyan.progressmanager.ProgressManager;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.MediaType;
import okhttp3.Request;
import timber.log.Timber;

/**
 * ================================================
 * App 的全局配置信息在此配置, 需要将此实现类声明到 AndroidManifest 中
 * ConfigModule 的实现类可以有无数多个, 在 Application 中只是注册回调, 并不会影响性能 (多个 ConfigModule 在多 Module 环境下尤为受用)
 * ConfigModule 接口的实现类对象是通过反射生成的, 这里会有些性能损耗
 *
 * @see com.jess.arms.base.delegate.AppDelegate
 * @see com.jess.arms.integration.ManifestParser
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki">请配合官方 Wiki 文档学习本框架</a>
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki/UpdateLog">更新日志, 升级必看!</a>
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki/Issues">常见 Issues, 踩坑必看!</a>
 * @see <a href="https://github.com/JessYanCoding/ArmsComponent/wiki">MVPArms 官方组件化方案 ArmsComponent, 进阶指南!</a>
 * Created by JessYan on 12/04/2017 17:25
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public final class GlobalConfiguration implements ConfigModule {
//    public static String sDomain = Api.APP_DOMAIN;

    /**
     * 使用builder可以为框架配置一些配置信息
     */
    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlobalConfigModule.Builder builder) {

        //Release 时, 让框架不再打印 Http 请求和响应的信息
        if (!BuildConfig.DEBUG) {

            //让框架不再打印 Http 请求和响应的信息
            builder.printHttpLogLevel(RequestInterceptor.Level.NONE);
        }

        //配置服务请求地址
        builder.baseurl(BuildConfig.SERVER_URL_VALUE)
                //强烈建议自己自定义图片加载逻辑, 因为 arms-imageloader-glide 提供的 GlideImageLoaderStrategy 并不能满足复杂的需求
                //请参考 https://github.com/JessYanCoding/MVPArms/wiki#3.4
                .imageLoaderStrategy(new GlideImageLoaderStrategy())

                //想支持多 BaseUrl, 以及运行时动态切换任意一个 BaseUrl, 请使用 https://github.com/JessYanCoding/RetrofitUrlManager
                //如果 BaseUrl 在 App 启动时不能确定, 需要请求服务器接口动态获取, 请使用以下代码
                //以下方式是 Arms 框架自带的切换 BaseUrl 的方式, 在整个 App 生命周期内只能切换一次, 若需要无限次的切换 BaseUrl, 以及各种复杂的应用场景还是需要使用 RetrofitUrlManager 框架
                //以下代码只是配置, 还要使用 Okhttp (AppComponent 中提供) 请求服务器获取到正确的 BaseUrl 后赋值给 GlobalConfiguration.sDomain
                //切记整个过程必须在第一次调用 Retrofit 接口之前完成, 如果已经调用过 Retrofit 接口, 此种方式将不能切换 BaseUrl
//                .baseurl(new BaseUrl() {
//                    @Override
//                    public HttpUrl url() {
//                        return HttpUrl.parse(sDomain);
//                    }
//                })

                //可根据当前项目的情况以及环境为框架某些部件提供自定义的缓存策略, 具有强大的扩展性
//                .cacheFactory(new Cache.Factory() {
//                    @NonNull
//                    @Override
//                    public Cache build(CacheType type) {
//                        switch (type.getCacheTypeId()){
//                            case CacheType.EXTRAS_TYPE_ID:
//                                return new IntelligentCache(500);
//                            case CacheType.CACHE_SERVICE_CACHE_TYPE_ID:
//                                return new Cache(type.calculateCacheSize(context));//自定义 Cache
//                            default:
//                                return new LruCache(200);
//                        }
//                    }
//                })

                //若觉得框架默认的打印格式并不能满足自己的需求, 可自行扩展自己理想的打印格式 (以下只是简单实现)
                .formatPrinter(new FormatPrinter() {
                    @Override
                    public void printJsonRequest(Request request, String bodyString) {
                        Timber.i("请求地址：%s", request.url());
                        Timber.i("请求参数：%s", bodyString);
                    }

                    @Override
                    public void printFileRequest(Request request) {
                        Timber.i("请求地址：%s", request.url().toString());
                    }

                    @Override
                    public void printJsonResponse(long chainMs, boolean isSuccessful, int code,
                                                  String headers, MediaType contentType, String bodyString,
                                                  List<String> segments, String message, String responseUrl) {
                        Timber.i("响应地址：%s", responseUrl);
                        Timber.i("响应参数：%s", bodyString);
                    }

                    @Override
                    public void printFileResponse(long chainMs, boolean isSuccessful, int code, String headers,
                                                  List<String> segments, String message, String responseUrl) {
                        Timber.i("响应地址：%s", responseUrl);
                    }
                })


                //可以自定义一个单例的线程池供全局使用
//                .executorService(Executors.newCachedThreadPool())

                //这里提供一个全局处理 Http 请求和响应结果的处理类, 可以比客户端提前一步拿到服务器返回的结果, 可以做一些操作, 比如 Token 超时后, 重新获取 Token
                .globalHttpHandler(new GlobalHttpHandlerImpl(context))
                //用来处理 RxJava 中发生的所有错误, RxJava 中发生的每个错误都会回调此接口
                //RxJava 必须要使用 ErrorHandleSubscriber (默认实现 Subscriber 的 onError 方法), 此监听才生效
                .responseErrorListener(new ResponseErrorListenerImpl())
                //这里可以自己自定义配置 Gson 的参数
                .gsonConfiguration((context1, gsonBuilder) -> {
                    gsonBuilder
                            //支持序列化值为 null 的参数
                            .serializeNulls()
                            //支持将序列化 key 为 Object 的 Map, 默认只能序列化 key 为 String 的 Map
                            .enableComplexMapKeySerialization();
                    //解决接口数据中的null改为默认值“”
//                            .registerTypeAdapter(String.class, new StringNullAdapter());
                })
                //这里可以自己自定义配置 Retrofit 的参数, 甚至您可以替换框架配置好的 OkHttpClient 对象 (但是不建议这样做, 这样做您将损失框架提供的很多功能)
                .retrofitConfiguration((context1, retrofitBuilder) -> {
                    //比如使用 FastJson 替代 Gson
//                    retrofitBuilder.addConverterFactory(FastJsonConverterFactory.create());
//                    retrofitBuilder.addConverterFactory(GsonConverterFactory.create());
//                    retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
                })
                //这里可以自己自定义配置 Okhttp 的参数
                .okhttpConfiguration((context1, okhttpBuilder) -> {
                    //支持 Https,详情请百度
                    okhttpBuilder.sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), SSLSocketClient.getTrustManager());
                    okhttpBuilder.hostnameVerifier(SSLSocketClient.getHostnameVerifier());
                    //请求超时
                    okhttpBuilder.connectTimeout(40, TimeUnit.SECONDS);
                    //写入超时
                    okhttpBuilder.writeTimeout(30, TimeUnit.SECONDS);
                    //读取超时
                    okhttpBuilder.readTimeout(30, TimeUnit.SECONDS);
                    //使用一行代码监听 Retrofit／Okhttp 上传下载进度监听,以及 Glide 加载进度监听 详细使用方法查看 https://github.com/JessYanCoding/ProgressManager
                    ProgressManager.getInstance().with(okhttpBuilder);
                    //让 Retrofit 同时支持多个 BaseUrl 以及动态改变 BaseUrl. 详细使用请方法查看 https://github.com/JessYanCoding/RetrofitUrlManager
                    RetrofitUrlManager.getInstance().with(okhttpBuilder);

                })
                //这里可以自己自定义配置 RxCache 的参数
                .rxCacheConfiguration((context1, rxCacheBuilder) -> {
                    //这里可以自己自定义配置 RxCache 的参数
                    rxCacheBuilder.useExpiredDataIfLoaderNotAvailable(true);
                    //想自定义 RxCache 的缓存文件夹或者解析方式, 如改成 FastJson, 请 return rxCacheBuilder.persistence(cacheDirectory, new FastJsonSpeaker());
                    //否则请 return null;
                    return null;
                }).cacheFile(context.getExternalCacheDir());
    }

    @Override
    public void injectAppLifecycle(@NonNull Context context, @NonNull List<AppLifecycles> lifecycles) {
        //AppLifecycles 中的所有方法都会在基类 Application 的对应生命周期中被调用, 所以在对应的方法中可以扩展一些自己需要的逻辑
        //可以根据不同的逻辑添加多个实现类
        lifecycles.add(new AppLifecyclesImpl());
    }

    @Override
    public void injectActivityLifecycle(@NonNull Context context, @NonNull List<Application.ActivityLifecycleCallbacks> lifecycles) {
        //ActivityLifecycleCallbacks 中的所有方法都会在 Activity (包括三方库) 的对应生命周期中被调用, 所以在对应的方法中可以扩展一些自己需要的逻辑
        //可以根据不同的逻辑添加多个实现类
        lifecycles.add(new ActivityLifecycleCallbacksImpl());
    }

    @Override
    public void injectFragmentLifecycle(@NonNull Context context, @NonNull List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {
        //FragmentLifecycleCallbacks 中的所有方法都会在 Fragment (包括三方库) 的对应生命周期中被调用, 所以在对应的方法中可以扩展一些自己需要的逻辑
        //可以根据不同的逻辑添加多个实现类
        lifecycles.add(new FragmentLifecycleCallbacksImpl());
    }
}
