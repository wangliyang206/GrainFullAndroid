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
package com.zqw.mobile.grainfull.mvp.ui.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.blankj.utilcode.util.ConvertUtils;
import com.jess.arms.base.BaseHolder;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.mvp.model.entity.SingleDuration;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;

/**
 * ================================================
 * 展示 {@link BaseHolder} 的用法
 * <p>
 * Created by JessYan on 9/4/16 12:56
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class SingleDurationItemHolder extends BaseHolder<SingleDuration> implements View.OnClickListener {
    @BindView(R.id.singleduration_item_layout)
    LinearLayout mLayout;

    @BindView(R.id.txvi_singledurationitem_name)
    TextView txviName;
    @BindView(R.id.txvi_singledurationitem_value)
    TextView txviValue;
    @BindView(R.id.txvi_singledurationitem_percent)
    TextView txviPercent;

    public SingleDurationItemHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(@NotNull SingleDuration data, int position) {
//        if (position == 0) {
//            setLayoutMargin(false);
//        } else {
//            setLayoutMargin(true);
//        }

        // 时长格式为：601-1800，单位秒；这里需要转换成文字
        String[] value = data.getName().split("-");

        String mStart = CommonUtils.timeConversion(false, Integer.parseInt(value[0]));
        txviName.setText(mStart);

        if (value.length == 2) {
            txviName.append("-");
            txviName.append(CommonUtils.timeConversion(false, Integer.parseInt(value[1])));
        }

        txviValue.setText(String.valueOf(data.getValue()));
        txviPercent.setText(data.getPercent() + "%");
    }

    /**
     * 动态设置Margin
     */
    private void setLayoutMargin(boolean isSet) {
        ConstraintLayout.LayoutParams layoutParam = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        if (isSet)
            layoutParam.setMargins(0, ConvertUtils.dp2px(10), 0, 0);
        else
            layoutParam.setMargins(0, 0, 0, 0);

        mLayout.setLayoutParams(layoutParam);
    }

    @Override
    protected void onRelease() {
        this.mLayout = null;
        this.txviName = null;
        this.txviValue = null;
        this.txviPercent = null;
    }
}
