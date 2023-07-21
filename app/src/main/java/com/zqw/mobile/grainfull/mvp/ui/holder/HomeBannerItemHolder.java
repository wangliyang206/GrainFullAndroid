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

import androidx.constraintlayout.widget.ConstraintLayout;

import com.blankj.utilcode.util.ConvertUtils;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.mvp.model.entity.HomeContentInfo;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;

/**
 * ================================================
 * 展示 {@link BaseHolder} 的用法
 * <p>
 * Created by JessYan on 9/4/16 12:56
 * ================================================
 */
public class HomeBannerItemHolder extends BaseHolder<HomeContentInfo> implements View.OnClickListener {
    @BindView(R.id.homebanner_item_layout)
    LinearLayout mLayout;

    @BindView(R.id.txvi_homebanneritemlayout_title)
    TextView txviTitle;
    @BindView(R.id.imvi_homebanneritemlayout_logo)
    ImageView imviLogo;
    @BindView(R.id.txvi_homebanneritemlayout_name)
    TextView txviName;

    private AppComponent mAppComponent;
    /**
     * 用于加载图片的管理类, 默认使用 Glide, 使用策略模式, 可替换框架
     */
    private ImageLoader mImageLoader;

    public HomeBannerItemHolder(View itemView) {
        super(itemView);
        //可以在任何可以拿到 Context 的地方, 拿到 AppComponent, 从而得到用 Dagger 管理的单例对象
        mAppComponent = ArmsUtils.obtainAppComponentFromContext(itemView.getContext());
        mImageLoader = mAppComponent.imageLoader();
    }

    @Override
    public void setData(@NotNull HomeContentInfo info, int position) {
//        setLayoutMargin(position != 0);

        txviTitle.setText(CommonUtils.isEmptyReturnStr(info.getName()));
        txviName.setText(CommonUtils.isEmptyReturnStr(info.getName()));
        // 显示图片
        mImageLoader.loadImage(mLayout.getContext(), ImageConfigImpl.builder().url(info.getImage())
                .errorPic(R.mipmap.mis_default_error)
                .placeholder(R.mipmap.mis_default_error)
                .imageView(imviLogo).build());
    }

    /**
     * 动态设置Margin
     */
    private void setLayoutMargin(boolean isSet) {
        ConstraintLayout.LayoutParams layoutParam = new ConstraintLayout.LayoutParams(ConvertUtils.dp2px(90), ConvertUtils.dp2px(130));
        if (isSet)
            layoutParam.setMargins(ConvertUtils.dp2px(10), 0, 0, 0);
        else
            layoutParam.setMargins(0, 0, 0, 0);

        mLayout.setLayoutParams(layoutParam);
    }

    @Override
    protected void onRelease() {
        this.mLayout = null;
        this.imviLogo = null;
        this.txviName = null;

        this.mAppComponent = null;
        this.mImageLoader = null;
    }
}
