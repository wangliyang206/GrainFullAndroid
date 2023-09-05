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

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.mvp.model.entity.CategoryBean;

import butterknife.BindView;

/**
 * ================================================
 * 展示 {@link BaseHolder} 的用法
 * <p>
 * Created by JessYan on 9/4/16 12:56
 * ================================================
 */
public class CategoryLeftItemHolder extends BaseHolder<CategoryBean> {
    @BindView(R.id.categoryItemView)
    LinearLayout categoryItemView;

    @BindView(R.id.selectedIcon)
    ImageView imviIcon;
    @BindView(R.id.categoryName)
    TextView txviName;

    /**
     * 用于加载图片的管理类, 默认使用 Glide, 使用策略模式, 可替换框架
     */
    private ImageLoader mImageLoader;

    public CategoryLeftItemHolder(View itemView) {
        super(itemView);

        //可以在任何可以拿到 Context 的地方, 拿到 AppComponent, 从而得到用 Dagger 管理的单例对象
        AppComponent mAppComponent = ArmsUtils.obtainAppComponentFromContext(itemView.getContext());
        mImageLoader = mAppComponent.imageLoader();
    }

    @Override
    public void setData(CategoryBean data, int position) {
        txviName.setText(CommonUtils.isEmptyReturnStr(data.getName()));

        if (data.isSelect()) {
            categoryItemView.setBackgroundColor(Color.WHITE);
            txviName.setTextColor(Color.parseColor("#181818"));
            txviName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else {
            switch (data.getFillet()) {
                case "down":
                    categoryItemView.setBackgroundResource(R.drawable.shape_corner_bottom_right);
                    break;
                case "up":
                    categoryItemView.setBackgroundResource(R.drawable.shape_corner_top_right);
                    break;
                default:
                    categoryItemView.setBackgroundColor(Color.parseColor("#F2F2F2"));
                    break;
            }

            txviName.setTextColor(Color.parseColor("#2D2D2D"));
            txviName.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }

    }

    @Override
    protected void onRelease() {
        this.imviIcon = null;
        this.txviName = null;

        this.mImageLoader = null;
    }
}
