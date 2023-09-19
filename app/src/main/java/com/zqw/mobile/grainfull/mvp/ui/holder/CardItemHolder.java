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

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.mvp.model.entity.HomeActionBarInfo;
import com.zqw.mobile.grainfull.mvp.ui.widget.RoundImageView;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;

/**
 * ================================================
 * 展示 {@link BaseHolder} 的用法
 * <p>
 * Created by JessYan on 9/4/16 12:56
 * ================================================
 */
public class CardItemHolder extends BaseHolder<HomeActionBarInfo> implements View.OnClickListener {
    @BindView(R.id.view_cardoverlapitemcard_avatar)
    RoundImageView imviPic;

    @BindView(R.id.txvi_cardoverlapitemcard_name)
    TextView txviName;

    /**
     * 用于加载图片的管理类, 默认使用 Glide, 使用策略模式, 可替换框架
     */
    private ImageLoader mImageLoader;

    public CardItemHolder(View itemView) {
        super(itemView);
        //可以在任何可以拿到 Context 的地方, 拿到 AppComponent, 从而得到用 Dagger 管理的单例对象
        AppComponent mAppComponent = ArmsUtils.obtainAppComponentFromContext(itemView.getContext());
        mImageLoader = mAppComponent.imageLoader();
    }

    @Override
    public void setData(@NotNull HomeActionBarInfo data, int position) {
        mImageLoader.loadImage(itemView.getContext(), ImageConfigImpl.builder().url(data.getUrl())
                .errorPic(R.drawable.img_avatar_01)
                .placeholder(R.drawable.img_avatar_01)
                .imageView(imviPic).build());
        txviName.setText(CommonUtils.isEmptyReturnStr(data.getName()));
    }

    @SuppressLint({"NewApi"})
    public CardItemHolder setAlpha(int viewId, float value) {
        if (Build.VERSION.SDK_INT >= 11) {
            itemView.findViewById(viewId).setAlpha(value);
        } else {
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0L);
            alpha.setFillAfter(true);
            itemView.findViewById(viewId).startAnimation(alpha);
        }

        return this;
    }

    @Override
    protected void onRelease() {
        this.imviPic = null;
        this.txviName = null;

        this.mImageLoader = null;
    }
}
