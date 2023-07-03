package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.ChineseNumUtils;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.di.component.DaggerCalculateDistanceComponent;
import com.zqw.mobile.grainfull.mvp.contract.CalculateDistanceContract;
import com.zqw.mobile.grainfull.mvp.presenter.CalculateDistancePresenter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Description:计算经纬度距离
 * <p>
 * Created on 2023/06/30 14:36
 *
 * @author 赤槿
 * module name is CalculateDistanceActivity
 */
public class CalculateDistanceActivity extends BaseActivity<CalculateDistancePresenter> implements CalculateDistanceContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_calculate_distance)
    LinearLayout contentLayout;                                                                     // 主布局

    @BindView(R.id.ragr_calculatedistance_unit)
    RadioGroup groupUnit;                                                                           // 选择单位
    @BindView(R.id.edit_calculatedistance_num)
    EditText editNum;                                                                               // 保留位数

    @BindView(R.id.edit_calculatedistance_input)
    EditText editInput;                                                                             // 输入坐标
    @BindView(R.id.txvi_calculatedistance_result)
    TextView txviResult;                                                                            // 结果

    @BindView(R.id.imvi_cleanup)
    ImageView imviCleanUp;                                                                          // 清空

    /*------------------------------------------------业务区域------------------------------------------------*/
    private List<Map<String, Double>> mList = new ArrayList<>();
    // 显示结果
    private StringBuilder showResult = new StringBuilder();

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCalculateDistanceComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_calculate_distance;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("计算经纬度距离");

        editInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(editInput.getText().toString())) {
                    imviCleanUp.setVisibility(View.GONE);
                } else {
                    imviCleanUp.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @OnClick({
            R.id.btn_calculatedistance_ok,                                                          // 计算
            R.id.imvi_cleanup,                                                                      // 清空
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_calculatedistance_ok:                                                     // 计算
                onSubmit();
                break;
            case R.id.imvi_cleanup:                                                                 // 清空
                editInput.setText("");
                break;
        }
    }

    /**
     * 提交
     */
    private void onSubmit() {
        String mInput = editInput.getText().toString();
        String num = editNum.getText().toString();

        boolean isKilometers = groupUnit.getCheckedRadioButtonId() == R.id.radio_calculatedistance_qm;

        if (TextUtils.isEmpty(mInput)) {
            showMessage("请输入需要计算的经纬度！");
            return;
        }

        String[] split = mInput.trim().split(",");
        if (split.length < 4 || split.length % 2 != 0) {
            showMessage("缺少经纬度，无法实现计算！");
            return;
        }

        // 数据拆分再组合想要的类型
        mList.clear();
        showResult.setLength(0);

        // 这一步先将要比对计算的数据进行分组。
        // input 存储格式为：“经度1,纬度1,经度2,纬度2,经度3,纬度3”；
        // 想要的结果为：item[0] = 经度1,纬度1,经度2,纬度2；item[1] = 经度2,纬度2,经度3,纬度3；以此类推……
        do {
            mInput = onFormat(mInput);
        } while (mInput.contains(","));

        Timber.i("##### =%s", mList.toString());
        int item = 0;
        // 开始计算
        for (Map<String, Double> map : mList) {
            item++;
            showResult.append("第");
            showResult.append(ChineseNumUtils.numberToChinese(false, String.valueOf(item)));
            // 得到的结果为米
            double distance = DistanceUtil.getDistance(new LatLng(map.get("starLatitude"), map.get("starLongitude")), new LatLng(map.get("endLatitude"), map.get("endLongitude")));
            if (isKilometers) {
                distance = distance / 1000;
            }
            showResult.append("组之间的距离为" + CommonUtils.round(distance, Integer.parseInt(num)));
            showResult.append(isKilometers ? "km；" : "m；");
            showResult.append("\n");
        }

        txviResult.setText(showResult.toString());
    }

    /**
     * 分割字符串
     * input 存储格式为：“经度1,纬度1,经度2,纬度2,经度3,纬度3”；
     * 将input分割成5份，示例：
     * 下标0##### = 经度1；
     * 下标1##### = 纬度1；
     * 下标2##### = 经度2；
     * 下标3##### = 纬度2；
     * 下标4##### = 经度3,纬度3
     */
    private String onFormat(String input) {
        // 计数
        int count = 0;
        Map<String, Double> map = null;
        for (String value : input.split(",", 5)) {
            Timber.i("##### =%s", value);
            if (count == 0) {
                map = new LinkedHashMap<>();
                map.put("starLongitude", Double.parseDouble(value));
                mList.add(map);
            } else if (count == 1) {
                map.put("starLatitude", Double.parseDouble(value));
            } else if (count == 2) {
                map.put("endLongitude", Double.parseDouble(value));
            } else if (count == 3) {
                map.put("endLatitude", Double.parseDouble(value));
            } else {
                // 这里是第五组分割数据，但这里不能直接返回。
                // 这里要把第三和第四组数据一起返回。
                // 所以这里需要重新格式化一次。
//                return value;
                return input.split(",", 3)[2];
            }
            count++;
        }
        return "";
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