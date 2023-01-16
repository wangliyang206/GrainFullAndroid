# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#指定代码的压缩级别(代码混淆的压缩比例，值在0-7之间)
-optimizationpasses 5

-allowaccessmodification

#不做预校验的操作
-dontpreverify

#混淆后类名都为小写
-dontusemixedcaseclassnames

#指定不去忽略非公共的库的类
-dontskipnonpubliclibraryclasses

#指定不去忽略非公共的库的类的成员
-dontskipnonpubliclibraryclassmembers
-printmapping priguardMapping.txt
-optimizations !code/simplification/artithmetic,!field/*,!class/merging/*
# glide 的混淆代码
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
# banner 的混淆代码
-keep class com.youth.banner.** {
    *;
 }
#业务类库
-keep class com.zqw.mobile.grainfull.mvp.model.entity.** {*;}                                       #实体类不要混淆
-dontwarn com.zqw.mobile.grainfull.mvp.model.entity.**

-keep class com.jess.arms.cj.** {*;}                                                                #实体类不要混淆
-dontwarn com.jess.arms.cj.**

#loading-drawable
-keep class app.dinus.com.loadingdrawable.** {*;}                                                   #实体类不要混淆
-dontwarn app.dinus.com.loadingdrawable.**

-keep class com.jess.arms.base.BaseApplication {*;}
-keep class com.zqw.mobile.grainfull.app.global.GlobalConfiguration {*;}                            #类不要混淆
-keep class com.zqw.mobile.grainfull.app.global.AccountManager {*;}                                 #类不要混淆
-keep class com.zqw.mobile.grainfull.app.global.RequestMapper {*;}                                  #类不要混淆
-keep interface com.zqw.mobile.grainfull.app.global.Constant {*;}                                   #类不要混淆

#类库不要混淆
-keep class javax.annotation.**{*;}
-keep class android.annotation.** {*;}
-keep class com.google.zxing.** {*;}                                                                #二维码的jar包不要混淆
-keepclassmembers class javax.annotation.* { *; }

# 抑制警告
-ignorewarnings
#-libraryjars libs/zxing.jar                                                                        #二维码的jar包不要混淆
#-libraryjars libs/common-release.aar

#混淆时是否记录日志
-verbose
# 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.google.vending.licensing.ILicensingService
-dontnote com.google.vending.licensing.ILicensingService
-keep class * extends android.os.Bundle

-keep public class com.android.vending.licensing.ILicensingService
-dontnote com.android.vending.licensing.ILicensingService

-dontwarn com.google.android.maps.**

# 保持内部类
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod
#*;
-keepclassmembers class * implements java.io.Serializable {
        static final long serialVersionUID;
        private static final java.io.ObjectStreamField[] serialPersistentFields;
        private void writeObject(java.io.ObjectOutputStream);
        private void readObject(java.io.ObjectInputStream);
        java.lang.Object writeReplace();
        java.lang.Object readResolve();
}

-keepattributes *JavascriptInterface*
-keep class android.webkit.JavascriptInterface {*;}

# keep 使用 webview 的类
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
    public void *(android.webkit.webView, jav.lang.String);
}

#保留JavascriptInterface中的方法
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

#------------------  下方是android平台自带的排除项，这里不要动         ----------------

-keep public class * extends java.lang.annotation.Annotation {
  *;
}

-keep public class * extends android.app.Activity{
	public <fields>;
	public <methods>;
}
-keep public class * extends android.app.Application{
	public <fields>;
	public <methods>;
}
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

-keepclasseswithmembers class * {
	public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
	public <init>(android.content.Context, android.util.AttributeSet, int);
}

#------------------  下方是共性的排除项目         ----------------
# 方法名中含有“JNI”字符的，认定是Java Native Interface方法，自动排除
# 方法名中含有“JRI”字符的，认定是Java Reflection Interface方法，自动排除

-keepclasseswithmembers class * {
    ... *JNI*(...);
}

-keepclasseswithmembernames class * {
	... *JRI*(...);
}

-keep class **JNI* {*;}

################common###############
-keepattributes *Annotation*
-keep public class * implements com.jess.arms.integration.ConfigModule
 #实体类不参与混淆
-keep class com.jess.arms.widget.** { *; }                                                          #自定义控件不参与混淆
-keepnames class * implements java.io.Serializable                                                 #比如我们要向activity传递对象使用了Serializable接口的时候，这时候这个类及类里面#的所有内容都不能混淆
-keepattributes Signature

-keepclassmembers enum * {  # 使用enum类型时需要注意避免以下两个方法混淆，因为enum类的特殊性，以下两个方法会被反射调用，
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keep class **.R$* {
 *;
}

#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}

#保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

################友盟SDK###############
-keep class com.umeng.** {*;}
-keep class org.repackage.** {*;}
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep public class com.zqw.mobile.grainfull.R$*{
public static final int *;
}
################华为SDK###############
-ignorewarnings
-keep class com.huawei.agconnect.** {*;}

-ignorewarnings
-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-keep class com.huawei.hianalytics.**{*;}
-keep class com.huawei.updatesdk.**{*;}
-keep class com.huawei.hms.**{*;}

-dontwarn com.huawei.**
-keep class com.huawei.** {*;}
-dontwarn org.slf4j.**
-keep class org.slf4j.** {*;}
-dontwarn org.springframework.**
-keep class org.springframework.** {*;}
-dontwarn com.fasterxml.jackson.**
-keep class com.fasterxml.jackson.** {*;}

-keep class com.huawei.noah.bolttranslator.**{*;}

-dontwarn com.huawei.hisi.**
-keep class com.huawei.hisi.** {*;}
################support###############
-keep class android.support.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.annotation.**

-keep class android.support.v7.app.** { *; }
-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }

################Luban###############
-keep class top.zibin.luban.** {*;}
################alipay###############
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}

################autolayout###############
-keep class com.zhy.autolayout.** { *; }
-keep interface com.zhy.autolayout.** { *; }

################高德地图###############
-dontwarn com.amap.api.**
-keep class com.amap.api.**{*;}
################3D 地图 V5.0.0之前：################
-keep class com.amap.api.maps.**{*;}
-keep class com.autonavi.amap.mapcore.*{*;}
-keep class com.amap.api.trace.**{*;}

################3D 地图 V5.0.0之后：################
-keep class com.amap.api.maps.**{*;}
-keep class com.autonavi.**{*;}

################定位################
-dontwarn com.loc.**
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.loc.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}

################搜索################
-keep   class com.amap.api.services.**{*;}

################2D地图################
-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}

################导航################
-keep class com.amap.api.navi.**{*;}

################AboutLibraries###############
-keep class .R
-keep class **.R$* {
    <fields>;
}
-keepclasseswithmembers class **.R$* {
    public static final int define_*;
}
##-----------------------------------
############ 百度人脸采集 ########
##-----------------------------------
-keep class com.baidu.vis.unified.license.** {*;}
-keep class com.baidu.liantian.** {*;}
-keep class com.baidu.baidusec.** {*;}
-keep class com.baidu.idl.main.facesdk.** {*;}

##-----------------------------------
############ 百度语音合成 ########
##-----------------------------------
-dontwarn com.baidu.tts.**
-keep class com.baidu.tts.**{*;}
-keep interface com.baidu.tts.**{*;}
-keep class com.baidu.speechsynthesizer.**{*;}
-keep interface com.baidu.speechsynthesizer.**{*;}
-dontwarn com.baidu.speechsynthesizer.**
##-----------------------------------
############ 阿里云Sophix热修复 ########
##-----------------------------------

#基线包使用，生成mapping.txt
-printmapping mapping.txt
#生成的mapping.txt在app/buidl/outputs/mapping/release路径下，移动到/app路径下
#修复后的项目使用，保证混淆结果一致
#-applymapping mapping.txt
#hotfix
-keep class com.taobao.sophix.**{*;}
-keep class com.ta.utdid2.device.**{*;}
#防止inline
-dontoptimize
-keep class com.zqw.mobile.grainfull.app.global.SophixStubApplication {*;}          					 #类不要混淆
# 如果不使用android.support.annotation.Keep则需加上此行
-keep class com.zqw.mobile.grainfull.app.global.SophixStubApplication$RealApplicationStub
##----------------------------------------------
############ Gradle Retrolambda混淆规则 ########
##----------------------------------------------
-dontwarn java.lang.invoke.*
-dontwarn **$$Lambda$*

##-----------------------------------
############ RxJava混淆 ########
##-----------------------------------
-keep class rx.schedulers.Schedulers {
    public static <methods>;
}
-keep class rx.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class rx.schedulers.TestScheduler {
    public <methods>;
}
-keep class rx.schedulers.Schedulers {
    public static ** test();
}
-dontwarn rx.internal.util.unsafe.**
-keep class rx.internal.util.unsafe.** { *;}

##-----------------------------------
############ OkHttp混淆 ########
##-----------------------------------
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn com.squareup.okhttp3.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**

-dontwarn com.squareup.**
-keep public class org.codehaus.* { *; }
-keep public class java.nio.* { *; }

-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}
-keepclassmembers class * {
    native <methods>;
}

-keep class com.squareup.okhttp3.** { *; }
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *;}
##---------------------------------------
############ androidEventBus混淆 ########
##---------------------------------------
-keep class org.simple.** { *; }
-keep interface org.simple.** { *; }
-keepclassmembers class * {
    @org.simple.eventbus.Subscriber <methods>;
}

##-----------------------------------
############ Retrofit2混淆 ########
##-----------------------------------
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Exceptions

-keep class com.squareup.retrofit2.** { *; }
-dontwarn com.squareup.retrofit2.**

-dontnote retrofit2.Platform
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
-dontwarn retrofit2.Platform$Java8

##-----------------------------------
############ RxJava RxAndroid混淆 ########
##-----------------------------------
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

-dontwarn org.mockito.**
-dontwarn org.junit.**
-dontwarn org.robolectric.**

-keep class io.reactivex.** { *; }
-keep interface io.reactivex.** { *; }

-dontwarn io.reactivex.**
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

-keep class io.reactivex.schedulers.Schedulers {
    public static <methods>;
}
-keep class io.reactivex.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class io.reactivex.schedulers.TestScheduler {
    public <methods>;
}
-keep class io.reactivex.schedulers.Schedulers {
    public static ** test();
}
-keepclassmembers class io.reactivex.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class io.reactivex.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    long producerNode;
    long consumerNode;
}

-keepclassmembers class io.reactivex.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    io.reactivex.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class io.reactivex.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    io.reactivex.internal.util.atomic.LinkedQueueNode consumerNode;
}

-dontwarn io.reactivex.internal.util.unsafe.**

################espresso###############
-keep class android.support.test.espresso.** { *; }
-keep interface android.support.test.espresso.** { *; }

################annotation###############
-keep class android.support.annotation.** { *; }
-keep interface android.support.annotation.** { *; }

################RxLifeCycle#################
-keep class com.trello.rxlifecycle2.** { *; }
-keep interface com.trello.rxlifecycle2.** { *; }

################RxPermissions#################
-keep class com.tbruyelle.rxpermissions2.** { *; }
-keep interface com.tbruyelle.rxpermissions2.** { *; }

################RxCache#################
-dontwarn io.rx_cache2.internal.**
-keep class io.rx_cache2.internal.Record { *; }
-keep class io.rx_cache2.Source { *; }

-keep class io.victoralbertos.jolyglot.** { *; }
-keep interface io.victoralbertos.jolyglot.** { *; }

################RxErrorHandler#################
 -keep class me.jessyan.rxerrorhandler.** { *; }
 -keep interface me.jessyan.rxerrorhandler.** { *; }

################Canary#################
-dontwarn com.squareup.haha.guava.**
-dontwarn com.squareup.haha.perflib.**
-dontwarn com.squareup.haha.trove.**
-dontwarn com.squareup.leakcanary.**
-keep class com.squareup.haha.** { *; }
-keep class com.squareup.leakcanary.** { *; }

# Marshmallow removed Notification.setLatestEventInfo()
-dontwarn android.app.Notification

##-----------------------------------
############ ButterKnife混淆 ########
##-----------------------------------
-keep public class * implements butterknife.Unbinder { public <init>(...); }
-keep class butterknife.*
-keepclasseswithmembernames class * { @butterknife.* <methods>; }
-keepclasseswithmembernames class * { @butterknife.* <fields>; }

-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keep class butterknife.** { *; }

## ----------------------------------
##   ########## Json混淆    ##########
## ----------------------------------
-dontwarn com.alibaba.fastjson.**               #告诉编译器fastjson打包过程中不要提示警告
-keep class com.alibaba.fastjson.** {*;}         #fastjson包下的所有类不要混淆，包括类里面的方法

## ----------------------------------
##   ########## Gson混淆    ##########
## ----------------------------------
-keepattributes Signature-keepattributes

-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }

-dontwarn com.google.code.gson.**
-keep class com.google.code.gson.**{*;}

-dontwarn com.google.gson.**
-keep class com.google.gson.**{*;}

-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.google.** {
    <fields>;
    <methods>;
}
-dontwarn org.immutables.gson.**
-keep class org.immutables.gson.** { *;}

# 保留JsonObject不被混淆
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

# # -------------------------------------------
# #  ######## greenDao混淆  ##########
# # -------------------------------------------
-keep class org.greenrobot.greendao.**{*;}
-keep class de.greenrobot.dao.** {*;}
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
    public static java.lang.String TABLENAME;
}
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties

## ---------------------------------------
##   ########## EventBus混淆    ##########
## ---------------------------------------
-keep class de.greenrobot.event.**{*;}
-keepclassmembers class ** {
    public void onEvent*(**);
    public void onEventMainThread(**);
    public void onEventBackgroundThread(**);
    public void onEventAsync(**);
}

## ---------------------------------------
##   ########## SlidingMenu混淆 ##########
## ---------------------------------------
-dontwarn com.jeremyfeinstein.slidingmenu.lib.**
-keep class com.jeremyfeinstein.slidingmenu.lib.** { *; }

## ---------------------------------------
##   ########## TrayPreferences混淆 ##########
## ---------------------------------------
-dontwarn net.grandcentrix.tray.**
-keep class net.grandcentrix.tray.** {*;}

## ---------------------------------------
##   ########## UIL混淆 ##########
## ---------------------------------------
-keep class com.nostra13.universalimageloader.** {*;}				#imageLoader包下所有类及类里面的内容不要混淆
-keepclassmembers class com.nostra13.universalimageloader.** {*;}

## ---------------------------------------
##   ########## jpush 极光推送 混淆 ######
## ---------------------------------------
-dontoptimize
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.service.JPushMessageReceiver { *; }
-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }

-keep class com.zqw.mobile.grainfull.service.JPushReceiver {*;}	                				#类不要混淆
-keep class com.zqw.mobile.grainfull.service.JPushReceiver

-keep public class cn.jiguang.analytics.android.api.** {*;}
#==================gson && protobuf==========================
-dontwarn com.google.**
-keep class com.google.gson.** {*;}
-keep class com.google.protobuf.** {*;}
-keep interface com.google.protobuf.** { *;}

## ---------------------------------------
##   ########## 百度地图混淆 ##########
## ---------------------------------------
-keep class org.apache.**{*;}
-keeppackagenames com.baidu.**
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,LocalVariable*Table,*Annotation*,Synthetic,EnclosingMethod
-dontwarn com.baidu.navisdk.**
-keep class com.baidu.navisdk.** {*;}
-keep interface com.baidu.navisdk.** {*;}
-dontwarn com.baidu.navi.**
-keep interface com.baidu.** { *; }
-keep class com.baidu.**$** { *; }
-keep interface com.baidu.**$** { *; }
-keep class vi.com.gdi.** { *; }
-dontwarn com.google.protobuf.**
-dontwarn com.google.android.support.v4.**
-keep class com.google.android.support.v4.** { *; }
-keep interface com.google.android.support.v4.app.** { *; }
-keep public class * extends com.google.android.support.v4.**
-keep public class * extends com.google.android.support.v4.app.Fragment
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep class com.baidu.vi.** { *; }

-keep interface vi.com.gdi.** { *; }
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-keep class mapsdkvi.com.** {*;}
-keep class com.sinovoice.** {*;}
-keep class pvi.com.** {*;}
-dontwarn com.baidu.**
-dontwarn vi.com.**
-dontwarn pvi.com.**
-keep class com.baidu.sapi2.** {*;}
-keepattributes JavascriptInterface

-dontwarn org.litepal.**
-keep class org.litepal.** { *;}
-keep class cn.thinkit.libtmfe.test.JNI{
    public protected <fields>;
    public protected <methods>;
}
-keep public class android.net.http.SslError
-dontwarn android.net.http.SslError

-keep class * extends android.app.Activity
-keep class * extends android.app.Application
-keep class * extends android.app.Service
-keep class * extends android.content.BroadcastReceiver
-keep class * extends android.content.ContentProvider
-keep class * extends android.app.backup.BackupAgentHelper
-keep class * extends android.preference.Preference
-keep class * extends android.os.Bundle
## ---------------------------------------
##   ########## FaceBook混淆 ##########
## ---------------------------------------
-dontwarn com.facebook.**
-keep enum com.facebook.**
-keep public interface com.facebook.**
-keep class com.facebook.**

## ---------------------------------------
##   ########## Glide混淆    ##########
## ---------------------------------------
-keep public class * implements com.bumptech.glide.module.AppGlideModule
-keep public class * implements com.bumptech.glide.module.LibraryGlideModule

-keep class com.bumptech.glide.**

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

## ---------------------------------------
##   ########## Timber混淆 ##########
## ---------------------------------------
-keep class timber.log.**{*;}
-keep class timber.log.Timber {*;}					                                                #类不要混淆
-keepclassmembers class * extends Timber.Tree {
    *;
}
-dontwarn org.jetbrains.annotations.**
## ---------------------------------------
##   ########## Bugly混淆 ##########
## ---------------------------------------
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

# 保留源文件名及行号
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
# Keep setters in Views so that animations can still work.
# Setters for listeners can still be removed.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
    void set*(%);
    void set*(%, %);
    void set*(%, %, %, %);
    void set*(**[]);
    void set*(**Listener);

    % get*();
    **[] get*();
    **Listener get*();
}

###-------------------------------------以下是Google Analytics-------------------------------------###
-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-keep public class com.google.android.gms.analytics.** {
    public *;
}

## ---------------------------------------
##   ########## ActionBarSherlock混淆 ##########
## ---------------------------------------
-keep class com.actionbarsherlock.** { *; }
-keep interface com.actionbarsherlock.** { *; }

#如果引用了v4或者v7包
-dontwarn android.support.**
-dontnote android.support.**

##记录生成的日志数据,gradle build时在本项目根目录输出##

#apk 包内所有 class 的内部结构
-dump class_files.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt

########记录生成的日志数据，gradle build时 在本项目根目录输出-end######

####混淆保护自己项目的部分代码以及引用的第三方jar包library-end####

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
    *** get*();
}

#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclasseswithmembers class **.R$* {
    public static <fields>;
}

-keepclassmembers class * {
    void *(**On*Event);
    void *(**On*Listener);
}

-keep class com.tencent.mm.opensdk.** {
    *;
}

-keep class com.tencent.wxop.** {
    *;
}

-keep class com.tencent.mm.sdk.** {
    *;
}

-keep class com.sina.weibo.sdk.** { *; }