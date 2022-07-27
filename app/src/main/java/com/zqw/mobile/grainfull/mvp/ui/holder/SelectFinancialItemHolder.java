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
import android.widget.TextView;

import com.jess.arms.base.BaseHolder;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;

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
public class SelectFinancialItemHolder extends BaseHolder<String> {

    @BindView(R.id.txvi_financialitemlayout_title)
    TextView mName;

    @BindView(R.id.view_financialitemlayout_line)
    View mLine;

    private int itemCount;

    public SelectFinancialItemHolder(View itemView, int itemCount) {
        super(itemView);
        this.itemCount = itemCount;
    }

    @Override
    public void setData(String data, int position) {
        // 控制分割线
        if ((itemCount - 1) == position) {
            mLine.setVisibility(View.GONE);
        } else {
            mLine.setVisibility(View.VISIBLE);
        }

        mName.setText(CommonUtils.isEmptyReturnStr(data));
    }

    @Override
    protected void onRelease() {
        this.mName = null;
        this.mLine = null;
    }
}
