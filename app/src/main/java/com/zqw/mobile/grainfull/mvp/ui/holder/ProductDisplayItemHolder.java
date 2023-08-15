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

import androidx.appcompat.widget.AppCompatTextView;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.mvp.model.entity.GoodsEntity;

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
public class ProductDisplayItemHolder extends BaseHolder<GoodsEntity> implements View.OnClickListener {
    @BindView(R.id.picIv)
    ImageView imviPic;

    @BindView(R.id.txvi_productdisplayitemlayout_price)
    AppCompatTextView txviPrice;
    @BindView(R.id.txvi_productdisplayitemlayout_oldPrice)
    AppCompatTextView txviOldPrice;

    private AppComponent mAppComponent;
    /**
     * 用于加载图片的管理类, 默认使用 Glide, 使用策略模式, 可替换框架
     */
    private ImageLoader mImageLoader;

    public ProductDisplayItemHolder(View itemView) {
        super(itemView);
        //可以在任何可以拿到 Context 的地方, 拿到 AppComponent, 从而得到用 Dagger 管理的单例对象
        mAppComponent = ArmsUtils.obtainAppComponentFromContext(itemView.getContext());
        mImageLoader = mAppComponent.imageLoader();
    }

    @Override
    public void setData(@NotNull GoodsEntity data, int position) {
        mImageLoader.loadImage(itemView.getContext(), ImageConfigImpl.builder().url(data.imgURL)
                .errorPic(R.mipmap.mis_default_error)
                .placeholder(R.mipmap.mis_default_error)
                .imageView(imviPic).build());
        txviPrice.setText(CommonUtils.isEmptyReturnStr(data.getLabelPrice()));
        txviOldPrice.setText(CommonUtils.isEmptyReturnStr(data.getOldlabelPrice()));
    }

    @Override
    protected void onRelease() {
        this.imviPic = null;
        this.txviPrice = null;
        this.txviOldPrice = null;
    }
}
