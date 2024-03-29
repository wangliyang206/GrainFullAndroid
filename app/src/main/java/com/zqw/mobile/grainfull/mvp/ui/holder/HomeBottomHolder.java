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

import androidx.annotation.NonNull;

import com.jess.arms.base.BaseHolder;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.model.entity.HomeOrderInfo;

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
public class HomeBottomHolder extends BaseHolder<HomeOrderInfo> {
    @BindView(R.id.common_bottom_item_layout)
    LinearLayout mLayout;

    public HomeBottomHolder(View itemView) {
        super(itemView);

    }

    public void initView(boolean isShowBottom) {
        mLayout.setVisibility(isShowBottom ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setData(@NonNull HomeOrderInfo info, int position) {

    }

    @Override
    protected void onRelease() {
        this.mLayout = null;
    }
}
