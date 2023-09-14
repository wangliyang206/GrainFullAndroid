package com.baidu.aip.asrwakeup3.uiasr.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.baidu.aip.asrwakeup3.core.inputstream.InFileStream;
import com.baidu.aip.asrwakeup3.core.util.MyLogger;
import com.baidu.aip.asrwakeup3.uiasr.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by fujiayi on 2017/6/20.
 */

public abstract class ActivityCommon extends AppCompatActivity {
    protected TextView txtLog;
    protected Button btn;
    protected Button setting;
    protected TextView txtResult;

    protected Handler handler;

    protected final int layout;
    private final int textId;

    protected int textViewLines = 0; // 防止textView中文本过多

    public ActivityCommon(int textId) {
        this(textId, R.layout.common);
    }

    public ActivityCommon(int textId, int layout) {
        super();
        this.textId = textId;
        this.layout = layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setStrictMode();
        InFileStream.setContext(this);
        setContentView(layout);
        initView();
        handler = new Handler() {

            /*
             * @param msg
             */
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                handleMsg(msg);
            }

        };
        MyLogger.setHandler(handler);
        initPermission();
    }

    protected void handleMsg(Message msg) {
        if (txtLog != null && msg.obj != null) {
            textViewLines++;
            if (textViewLines > 100) {
                textViewLines = 0;
                txtLog.setText("");
            }
            txtLog.append(msg.obj.toString() + "\n");
        }
    }

    protected void initView() {
        txtResult = findViewById(R.id.txtResult);
        txtLog = findViewById(R.id.txtLog);
        btn = findViewById(R.id.btn);
        setting = findViewById(R.id.imvi_layouthomemine_mine_setting);

        try {
            InputStream is = getResources().openRawResource(textId);
            byte[] bytes = new byte[is.available()];
            is.read(bytes);
            String descText = new String(bytes);
            txtLog.setText(descText);
        } catch (IOException e) {
            e.printStackTrace();
        }
        txtLog.append("\n");
    }

    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String[] permissions = {
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                /* 下面是蓝牙用的，可以不申请
                Manifest.permission.BROADCAST_STICKY,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN
                */
        };

        ArrayList<String> toApplyList = new ArrayList<>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.

            }
        }
        String[] tmpList = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
    }

    private void setStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());

    }
}
