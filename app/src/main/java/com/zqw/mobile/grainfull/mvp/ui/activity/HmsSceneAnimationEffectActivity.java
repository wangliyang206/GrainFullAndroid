package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.huawei.hms.image.render.IBindCallBack;
import com.huawei.hms.image.render.ImageRender;
import com.huawei.hms.image.render.ImageRenderImpl;
import com.huawei.hms.image.render.RenderView;
import com.huawei.hms.image.render.ResultCode;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.di.component.DaggerHmsSceneAnimationEffectComponent;
import com.zqw.mobile.grainfull.mvp.contract.HmsSceneAnimationEffectContract;
import com.zqw.mobile.grainfull.mvp.presenter.HmsSceneAnimationEffectPresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.GridRadioGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.BindView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import timber.log.Timber;

/**
 * Description: 场景动效
 * <p>
 * Created on 2024/12/10 09:16
 *
 * @author 赤槿
 * module name is HmsSceneAnimationEffectActivity
 */
@RuntimePermissions
public class HmsSceneAnimationEffectActivity extends BaseActivity<HmsSceneAnimationEffectPresenter> implements HmsSceneAnimationEffectContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.view_hmssceneanimationeffect_content)
    FrameLayout contentView;

    @BindView(R.id.ragr_hmssceneanimationeffect_group)
    GridRadioGroup radioGroup;

    /*------------------------------------------------业务区域------------------------------------------------*/

    // imageRender object
    private ImageRenderImpl imageRenderAPI;
    // 本地动画效果路径
    private String sourcePath;
    private String hashCode;

    @Override
    protected void onDestroy() {
        // Destroy the view.
        if (null != imageRenderAPI) {
            imageRenderAPI.removeRenderView();
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Timber.i("onPause");
        if (null != imageRenderAPI) {
            imageRenderAPI.unBindRenderView(hashCode);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Timber.i("onResume");
        if (null != imageRenderAPI) {
            imageRenderAPI.bindRenderView(sourcePath, getAuthJson(), new IBindCallBack() {
                @Override
                public void onBind(RenderView renderView, int i) {
                    if (renderView != null) {
                        if (renderView.getResultCode() == ResultCode.SUCCEED) {
                            final View view = renderView.getView();
                            if (null != view) {
                                contentView.addView(view);
                                hashCode = String.valueOf(view.hashCode());
                            }
                        }
                    }
                }

                @Override
                public void onParseEnd() {

                }
            });
        }
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerHmsSceneAnimationEffectComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_hms_scene_animation_effect;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("场景动效");

        HmsSceneAnimationEffectActivityPermissionsDispatcher.initWithPermissionCheck(this);
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void init() {
        sourcePath = getFilesDir().getPath() + File.separator + "sources";

        if (!CommonUtils.createResourceDirs(sourcePath)) {
            Timber.e("创建目录失败，请检查权限");
        }

        if (!CommonUtils.copyAssetsFileToDirs(this, "ParticleView" + File.separator + "particle.png", sourcePath + File.separator + "particle.png")) {
            Timber.e( "Copy resource file fail, please check permission");
        }
        if (!CommonUtils.copyAssetsFileToDirs(this, "ParticleView" + File.separator + "background.jpg", sourcePath + File.separator + "background.jpg")) {
            Timber.e( "Copy resource file fail, please check permission");
        }
        if (!CommonUtils.copyAssetsFileToDirs(this, "ParticleView" + File.separator + "manifest.xml", sourcePath + File.separator + "manifest.xml")) {
            Timber.e( "Copy resource file fail, please check permission");
        }

        // 获取ImageRender对象。
        ImageRender.getInstance(this, new ImageRender.RenderCallBack() {
            @Override
            public void onSuccess(ImageRenderImpl imageRender) {
                Timber.i("API 初始化成功！");
                imageRenderAPI = imageRender;
                useImageRender();
            }

            @Override
            public void onFailure(int i) {
                Timber.e("API 初始化失败, errorCode = %s", i);
            }
        });

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String value = "";
            if (checkedId == R.id.radio_hmssceneanimationeffect_particleView) {
                value = "ParticleView";
            } else if (checkedId == R.id.radio_hmssceneanimationeffect_waterWallpaper) {
                value = "WaterWallpaper";
            } else if (checkedId == R.id.radio_hmssceneanimationeffect_particleScatter) {
                value = "ParticleScatter";
            } else if (checkedId == R.id.radio_hmssceneanimationeffect_rotateView) {
                value = "RotateView";
            } else if (checkedId == R.id.radio_hmssceneanimationeffect_dropPhysicalView) {
                value = "DropPhysicalView";
            }
            changeAnimation(value);
        });
    }

    /**
     * 需要图像渲染服务。
     */
    private void useImageRender() {
        if (imageRenderAPI == null) {
            Timber.e("init Remote失败，请检查套件版本");
            return;
        }
        addView();
    }

    /**
     * 更改动画
     *
     * @param animationName 动画名称
     */
    private void changeAnimation(String animationName) {
        if (!CommonUtils.copyAssetsFilesToDirs(this, animationName, sourcePath)) {
            Timber.e("复制文件失败，请检查权限");
            return;
        }
        if (imageRenderAPI == null) {
            Timber.e("initRemote失败，请检查套件版本");
            return;
        }
        if (contentView.getChildCount() > 0) {
            imageRenderAPI.removeRenderView();
            contentView.removeAllViews();
            addView();
        }
    }

    private void addView() {
        // Initialize the ImageRender object.
        int initResult = imageRenderAPI.doInit(sourcePath, getAuthJson());
        Timber.i("执行初始化结果 == %s", initResult);
        if (initResult == 0) {
            // Obtain the rendered view.
            RenderView renderView = imageRenderAPI.getRenderView();
            if (renderView.getResultCode() == ResultCode.SUCCEED) {
                View view = renderView.getView();
                if (null != view) {
                    // Add the rendered view to the layout.
                    contentView.addView(view);
                    hashCode = String.valueOf(view.hashCode());
                } else {
                    Timber.w("获取RenderView失败，视图为空");
                }
            } else if (renderView.getResultCode() == ResultCode.ERROR_GET_RENDER_VIEW_FAILURE) {
                Timber.w("获取渲染视图失败");
            } else if (renderView.getResultCode() == ResultCode.ERROR_XSD_CHECK_FAILURE) {
                Timber.w("获取RenderView失败，资源文件参数错误，请检查资源文件。");
            } else if (renderView.getResultCode() == ResultCode.ERROR_VIEW_PARSE_FAILURE) {
                Timber.w("获取RenderView失败，资源文件解析失败，请检查资源文件。");
            } else if (renderView.getResultCode() == ResultCode.ERROR_REMOTE) {
                Timber.w("获取RenderView失败，远程调用失败，请检查HMS服务");
            } else if (renderView.getResultCode() == ResultCode.ERROR_DOINIT) {
                Timber.w("获取RenderView失败，初始化失败，请重新初始化");
            }
        } else {
            Timber.w("初始化失败, errorCode == %s", initResult);
        }
    }


    /**
     * Add authentication parameters.
     *
     * @return JsonObject of Authentication parameters.
     */
    public static JSONObject getAuthJson() {
        JSONObject authJson = new JSONObject();
        try {
            authJson.put("projectId", "projectId-test");
            authJson.put("appId", "appId-test");
            authJson.put("authApiKey", "authApiKey-test");
            authJson.put("clientSecret", "clientSecret-test");
            authJson.put("clientId", "clientId-test");
            authJson.put("token", "token-test");
        } catch (JSONException e) {
            Timber.w("获取authJson失败，请检查auth信息");
        }
        return authJson;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        HmsSceneAnimationEffectActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

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
}