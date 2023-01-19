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
import com.zqw.mobile.grainfull.mvp.model.entity.UmEvent;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;

/**
 * ================================================
 * 展示 {@link BaseHolder} 的用法
 * <p>
 * Created by JessYan on 9/4/16 12:56
 * ================================================
 */
public class UmEventItemHolder extends BaseHolder<UmEvent> implements View.OnClickListener {
    @BindView(R.id.umevent_item_layout)
    LinearLayout mLayout;

    @BindView(R.id.txvi_umeventitem_id)
    TextView txviId;
    @BindView(R.id.txvi_umeventitem_name)
    TextView txviName;
    @BindView(R.id.txvi_umeventitem_num)
    TextView txviValue;
    @BindView(R.id.txvi_umeventitem_select)
    TextView txviSelect;

    public UmEventItemHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(@NotNull UmEvent data, int position) {
//        if (position == 0) {
//            setLayoutMargin(false);
//        } else {
//            setLayoutMargin(true);
//        }

        txviId.setText(CommonUtils.isEmptyReturnStr(data.getName()));
        txviName.setText(CommonUtils.isEmptyReturnStr(data.getDisplayName()));
        txviValue.setText(String.valueOf(data.getCount()));
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
        this.txviId = null;
        this.txviName = null;
        this.txviValue = null;
        this.txviSelect = null;
    }
}
