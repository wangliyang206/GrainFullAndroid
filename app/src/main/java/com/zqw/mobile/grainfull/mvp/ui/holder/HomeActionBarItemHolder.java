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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;

import com.blankj.utilcode.util.ConvertUtils;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.mvp.model.entity.HomeActionBarInfo;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;

/**
 * ================================================
 * 展示 {@link BaseHolder} 的用法
 * <p>
 * Created by JessYan on 9/4/16 12:56
 * ================================================
 */
public class HomeActionBarItemHolder extends BaseHolder<HomeActionBarInfo> implements View.OnClickListener {
    @BindView(R.id.homeactionbar_item_layout)
    LinearLayout mLayout;

    @BindView(R.id.imvi_homeactionbaritemlayout_logo)
    ImageView imviLogo;
    @BindView(R.id.txvi_homeactionbaritemlayout_name)
    TextView txviName;

    public HomeActionBarItemHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(@NotNull HomeActionBarInfo info, int position) {
        GridLayoutManager.LayoutParams layoutParam = (GridLayoutManager.LayoutParams) mLayout.getLayoutParams();
        // 屏幕宽度
        int screenWidth = ArmsUtils.getScreenWidth(itemView.getContext()) - ConvertUtils.dp2px(26);
        layoutParam.width = screenWidth / 5;
        mLayout.setLayoutParams(layoutParam);

        imviLogo.setImageResource(info.getImage());
        txviName.setText(CommonUtils.isEmptyReturnStr(info.getName()));
    }

    @Override
    protected void onRelease() {
        this.mLayout = null;
        this.imviLogo = null;
        this.txviName = null;
    }
}
