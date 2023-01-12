package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.blankj.utilcode.util.ClipboardUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerEquipmentInfoComponent;
import com.zqw.mobile.grainfull.mvp.contract.EquipmentInfoContract;
import com.zqw.mobile.grainfull.mvp.presenter.EquipmentInfoPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:查看设备信息
 * <p>
 * Created on 2023/01/12 09:54
 *
 * @author 赤槿
 * module name is EquipmentInfoActivity
 */
public class EquipmentInfoActivity extends BaseActivity<EquipmentInfoPresenter> implements EquipmentInfoContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_equipment_info)
    LinearLayout contentLayout;                                                                     // 主布局

    @BindView(R.id.txvi_equipmentinfo_imei)
    TextView txviImei;                                                                              // 设备唯一标识
    @BindView(R.id.txvi_equipmentinfo_brand)
    TextView txviBrand;                                                                             // 品牌
    @BindView(R.id.txvi_equipmentinfo_model)
    TextView txviModel;                                                                             // 型号
    @BindView(R.id.txvi_equipmentinfo_manufacturer)
    TextView txviManufacturer;                                                                      // 生产厂商
    @BindView(R.id.txvi_equipmentinfo_resolvingpower)
    TextView txviResolvingPower;                                                                    // 分辨率
    @BindView(R.id.txvi_equipmentinfo_sys_version)
    TextView txviSysVersion;                                                                        // Android 版本
    @BindView(R.id.txvi_equipmentinfo_sys_sdk)
    TextView txviSysSdk;                                                                            // Android SDK 版本


    @BindView(R.id.txvi_equipmentinfo_id)
    TextView txviId;                                                                                // ID
    @BindView(R.id.txvi_equipmentinfo_product)
    TextView txviProduct;                                                                           // 产品名称
    @BindView(R.id.txvi_equipmentinfo_device)
    TextView txviDevice;                                                                            // 设备
    @BindView(R.id.txvi_equipmentinfo_mainboard)
    TextView txviMainboard;                                                                         // 主板
    @BindView(R.id.txvi_equipmentinfo_cpu)
    TextView txviCpu;                                                                               // CPU
    @BindView(R.id.txvi_equipmentinfo_display)
    TextView txviDisplay;                                                                           // 显示屏
    @BindView(R.id.txvi_equipmentinfo_host)
    TextView txviHost;                                                                              // HOST

    @BindView(R.id.txvi_equipmentinfo_user)
    TextView txviUser;                                                                              // 用户名
    @BindView(R.id.txvi_equipmentinfo_serial)
    TextView txviSerial;                                                                            // 硬件序列号
    @BindView(R.id.txvi_equipmentinfo_fingerprint)
    TextView txviFingerprint;                                                                       // 硬件识别码
    @BindView(R.id.txvi_equipmentinfo_meid)
    TextView txviMeid;                                                                              // MEID
    @BindView(R.id.txvi_equipmentinfo_imsi)
    TextView txviImsi;                                                                              // IMSI
    @BindView(R.id.txvi_equipmentinfo_build)
    TextView txviBuild;                                                                             // Build标签
    @BindView(R.id.txvi_equipmentinfo_type)
    TextView txviType;                                                                              // TYPE
    @BindView(R.id.txvi_equipmentinfo_codename)
    TextView txviCodeName;                                                                          // 版本代号

    /*------------------------------------------------业务区域------------------------------------------------*/


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerEquipmentInfoComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_equipment_info;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("查看设备信息");

        /* 加载设备信息 */
        try {
            // 设备唯一标识
            txviImei.append(com.jess.arms.utils.DeviceUtils.getIMEI(getApplicationContext()));
        } catch (Exception ignored) {

        }

        // 品牌
        txviBrand.append(Build.BRAND);
        // 型号
        txviModel.append(Build.MODEL);
        // 生产厂商
        txviManufacturer.append(DeviceUtils.getManufacturer());
        // 分辨率
        txviResolvingPower.append(ArmsUtils.getScreenHeidth(getApplicationContext()) + "x" + ArmsUtils.getScreenWidth(getApplicationContext()));
        // Android 版本
        txviSysVersion.append(Build.VERSION.RELEASE);
        // Android SDK 版本
        txviSysSdk.append(String.valueOf(Build.VERSION.SDK_INT));


        // 设备ID
        txviId.append(Build.ID);
        // 产品名称
        txviProduct.append(Build.PRODUCT);
        // 设备
        txviDevice.append(Build.DEVICE);
        // 主板
        txviMainboard.append(Build.BOARD);
        // CPU
        txviCpu.append(Build.CPU_ABI);
        // DISPLAY(显示屏)
        txviDisplay.append(Build.DISPLAY);
        // HOST(主机)
        txviHost.append(Build.HOST);
        // 用户名
        txviUser.append(Build.USER);
        // 硬件序列号
        txviSerial.append(Build.SERIAL);
        // FINGERPRINT(硬件识别码)名义上是硬件指纹，实际上是随着系统升级而变的，编译版本、日期变了也会改变，并非真正的硬件参数
        txviFingerprint.append(Build.FINGERPRINT);
        // MEID
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        txviMeid.append(PhoneUtils.getMEID());
        try {
            // IMSI
            txviImsi.append(PhoneUtils.getIMSI());
        } catch (Exception ignored) {

        }
        // Build标签
        txviBuild.append(Build.TAGS);
        // TYPE
        txviType.append(Build.TYPE);
        // 版本代号
        txviCodeName.append(Build.VERSION.CODENAME);
    }

    @OnClick({
            R.id.txvi_equipmentinfo_imei,                                                           // 设备唯一标识
            R.id.txvi_equipmentinfo_brand,                                                          // 品牌
            R.id.txvi_equipmentinfo_model,                                                          // 型号
            R.id.txvi_equipmentinfo_manufacturer,                                                   // 生产厂商
            R.id.txvi_equipmentinfo_resolvingpower,                                                 // 分辨率
            R.id.txvi_equipmentinfo_sys_version,                                                    // Android 版本
            R.id.txvi_equipmentinfo_sys_sdk,                                                        // Android SDK 版本

            R.id.txvi_equipmentinfo_id,                                                             // 设备ID
            R.id.txvi_equipmentinfo_product,                                                        // 产品名称
            R.id.txvi_equipmentinfo_device,                                                         // 设备名称
            R.id.txvi_equipmentinfo_mainboard,                                                      // 主板
            R.id.txvi_equipmentinfo_cpu,                                                            // CPU
            R.id.txvi_equipmentinfo_display,                                                        // 显示屏
            R.id.txvi_equipmentinfo_host,                                                           // HOST
            R.id.txvi_equipmentinfo_user,                                                           // 用户名
            R.id.txvi_equipmentinfo_serial,                                                         // 硬件序列号
            R.id.txvi_equipmentinfo_fingerprint,                                                    // 硬件识别码
            R.id.txvi_equipmentinfo_meid,                                                           // MEID
            R.id.txvi_equipmentinfo_imsi,                                                           // IMSI
            R.id.txvi_equipmentinfo_build,                                                          // Build
            R.id.txvi_equipmentinfo_type,                                                           // TYPE
            R.id.txvi_equipmentinfo_codename,                                                       // 版本代号
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txvi_equipmentinfo_imei:                                                      // 设备唯一标识
                onCopy(txviImei.getText().toString());
                break;
            case R.id.txvi_equipmentinfo_brand:                                                     // 品牌
                onCopy(txviBrand.getText().toString());
                break;
            case R.id.txvi_equipmentinfo_model:                                                     // 型号
                onCopy(txviModel.getText().toString());
                break;
            case R.id.txvi_equipmentinfo_manufacturer:                                              // 生产厂商
                onCopy(txviManufacturer.getText().toString());
                break;
            case R.id.txvi_equipmentinfo_resolvingpower:                                            // 分辨率
                onCopy(txviResolvingPower.getText().toString());
                break;
            case R.id.txvi_equipmentinfo_sys_version:                                               // Android 版本
                onCopy(txviSysVersion.getText().toString());
                break;
            case R.id.txvi_equipmentinfo_sys_sdk:                                                   // Android SDK 版本
                onCopy(txviSysSdk.getText().toString());
                break;
            case R.id.txvi_equipmentinfo_id:                                                        // 设备ID
                onCopy(txviId.getText().toString());
                break;
            case R.id.txvi_equipmentinfo_product:                                                   // 产品名称
                onCopy(txviProduct.getText().toString());
                break;
            case R.id.txvi_equipmentinfo_device:                                                    // 设备名称
                onCopy(txviDevice.getText().toString());
                break;
            case R.id.txvi_equipmentinfo_mainboard:                                                 // 主板
                onCopy(txviMainboard.getText().toString());
                break;
            case R.id.txvi_equipmentinfo_cpu:                                                       // CPU
                onCopy(txviCpu.getText().toString());
                break;
            case R.id.txvi_equipmentinfo_display:                                                   // 显示屏
                onCopy(txviDisplay.getText().toString());
                break;
            case R.id.txvi_equipmentinfo_host:                                                      // HOST
                onCopy(txviHost.getText().toString());
                break;
            case R.id.txvi_equipmentinfo_user:                                                      // 用户名
                onCopy(txviUser.getText().toString());
                break;
            case R.id.txvi_equipmentinfo_serial:                                                    // 硬件序列号
                onCopy(txviSerial.getText().toString());
                break;
            case R.id.txvi_equipmentinfo_fingerprint:                                               // 硬件识别码
                onCopy(txviFingerprint.getText().toString());
                break;
            case R.id.txvi_equipmentinfo_meid:                                                      // MEID
                onCopy(txviMeid.getText().toString());
                break;
            case R.id.txvi_equipmentinfo_imsi:                                                      // IMSI
                onCopy(txviImsi.getText().toString());
                break;
            case R.id.txvi_equipmentinfo_build:                                                     // Build
                onCopy(txviBuild.getText().toString());
                break;
            case R.id.txvi_equipmentinfo_type:                                                      // TYPE
                onCopy(txviType.getText().toString());
                break;
            case R.id.txvi_equipmentinfo_codename:                                                  // 版本代号
                onCopy(txviCodeName.getText().toString());
                break;

        }
    }

    /**
     * 复制
     */
    private void onCopy(String val) {
        if (!TextUtils.isEmpty(val)) {
            ClipboardUtils.copyText(val);
            showMessage("复制成功！");
        }
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