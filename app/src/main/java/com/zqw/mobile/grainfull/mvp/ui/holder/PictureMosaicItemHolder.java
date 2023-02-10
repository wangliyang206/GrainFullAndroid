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

import com.blankj.utilcode.util.ConvertUtils;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;

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
public class PictureMosaicItemHolder extends BaseHolder<String> implements View.OnClickListener {
    @BindView(R.id.picturemosaic_item_layout)
    LinearLayout mLayout;

    @BindView(R.id.imvi_picturemosaicitemlayout_pic)
    ImageView imviPic;

    private AppComponent mAppComponent;
    /**
     * 用于加载图片的管理类, 默认使用 Glide, 使用策略模式, 可替换框架
     */
    private ImageLoader mImageLoader;

    // 展示类型：1代表纵图；2代表横图；3代表2列纵图；4代表3列纵图；
    private int type = 1;

    public PictureMosaicItemHolder(View itemView) {
        super(itemView);
        //可以在任何可以拿到 Context 的地方, 拿到 AppComponent, 从而得到用 Dagger 管理的单例对象
        mAppComponent = ArmsUtils.obtainAppComponentFromContext(itemView.getContext());
        mImageLoader = mAppComponent.imageLoader();
    }

    public void initView(int type) {
        this.type = type;
    }

    @Override
    public void setData(@NotNull String data, int position) {
        // 控制布局大小
        if (type == 1) {
            // 纵图
            LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ArmsUtils.dip2px(mLayout.getContext(), 220));
            if (position == 0) {
                mParams.setMargins(0, 0, 0, 0);
            } else {
                mParams.setMargins(0, ConvertUtils.dp2px(5), 0, 0);
            }
            mLayout.setLayoutParams(mParams);
        } else if (type == 2) {
            // 横图
            LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(ArmsUtils.dip2px(mLayout.getContext(), 90), ArmsUtils.dip2px(mLayout.getContext(), 90));
            if (position == 0) {
                mParams.setMargins(0, 0, 0, 0);
            } else {
                mParams.setMargins(ConvertUtils.dp2px(10), 0, 0, 0);
            }
            mLayout.setLayoutParams(mParams);
        } else if (type == 3) {
            // 2列纵图
            LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ArmsUtils.dip2px(mLayout.getContext(), 160));
            mParams.setMargins(ConvertUtils.dp2px(5), ConvertUtils.dp2px(5), ConvertUtils.dp2px(5), ConvertUtils.dp2px(5));
            mLayout.setLayoutParams(mParams);
        } else if (type == 4) {
            // 3列纵图
            LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ArmsUtils.dip2px(mLayout.getContext(), 90));
            mParams.setMargins(ConvertUtils.dp2px(5), ConvertUtils.dp2px(5), ConvertUtils.dp2px(5), ConvertUtils.dp2px(5));
            mLayout.setLayoutParams(mParams);
        }

        // 显示图片
        mImageLoader.loadImage(itemView.getContext(),
                ImageConfigImpl
                        .builder()
                        .placeholder(R.mipmap.mis_default_error)
                        .errorPic(R.mipmap.mis_default_error)
                        .url(data)
                        .imageView(imviPic)
                        .build());

    }

    @Override
    protected void onRelease() {
        //只要传入的 Context 为 Activity, Glide 就会自己做好生命周期的管理, 其实在上面的代码中传入的 Context 就是 Activity
        //所以在 onRelease 方法中不做 clear 也是可以的, 但是在这里想展示一下 clear 的用法
        mImageLoader.clear(mAppComponent.application(), ImageConfigImpl.builder()
                .imageViews(imviPic)
                .build());
        this.mLayout = null;
        this.imviPic = null;

        this.mAppComponent = null;
        this.mImageLoader = null;
    }
}
