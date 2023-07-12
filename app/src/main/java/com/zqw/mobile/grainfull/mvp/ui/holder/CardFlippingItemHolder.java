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
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.baidu.idl.face.platform.utils.DensityUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.model.entity.CardFlipping;

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
public class CardFlippingItemHolder extends BaseHolder<CardFlipping> implements View.OnClickListener {
    @BindView(R.id.cardflipping_item_layout)
    RelativeLayout mLayout;

    @BindView(R.id.imvi_cardflippingitem_bg)
    ImageView imviBg;
    @BindView(R.id.imvi_cardflippingitem_content)
    ImageView imviContent;

    private AppComponent mAppComponent;
    /**
     * 用于加载图片的管理类, 默认使用 Glide, 使用策略模式, 可替换框架
     */
    private ImageLoader mImageLoader;
    // 控件高度
    private int viewHeight;

    public void initView(int viewHeight) {
        this.viewHeight = viewHeight;
    }

    public CardFlippingItemHolder(View itemView) {
        super(itemView);
        //可以在任何可以拿到 Context 的地方, 拿到 AppComponent, 从而得到用 Dagger 管理的单例对象
        mAppComponent = ArmsUtils.obtainAppComponentFromContext(itemView.getContext());
        mImageLoader = mAppComponent.imageLoader();
    }

    @Override
    public void setData(@NotNull CardFlipping info, int position) {
//        setLayoutMargin();
        mLayout.setVisibility(View.VISIBLE);
        imviBg.setVisibility(View.VISIBLE);

        // 判断当前是否显示
        if (info.isDisplayFront()) {
            // 当前状态为：显示正面
            imviBg.setImageResource(info.getImageBg());
            imviContent.setVisibility(View.VISIBLE);
            imviContent.setImageResource(info.getImageContent());
        } else {
            // 当前状态为：显示反面
            imviBg.setImageResource(R.mipmap.tw_card);
            imviContent.setVisibility(View.GONE);

            // 这里使用了ExplosionField(爆炸散落动画)，如果刷新重置View，需要手动恢复。
            mLayout.animate().setDuration(150).setStartDelay(150).scaleX(1.0f).scaleY(1.0f).alpha(1.0f).start();
        }
    }

    /**
     * 动态设置Margin
     */
    private void setLayoutMargin() {
        ConstraintLayout.LayoutParams layoutParam = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

        // 拿到屏幕平均分成四份的宽度
        int mViewWidth = DensityUtils.getDisplayWidth(itemView.getContext()) / 4;
        // 自定义宽度 = 平均宽度 - 间隔
        layoutParam.width = mViewWidth - ConvertUtils.dp2px(10);
        // 定义高度
        layoutParam.height = DensityUtils.dip2px(itemView.getContext(), 100);

//        int mViewHeight = viewHeight / 4;
//        layoutParam.height = mViewHeight - ConvertUtils.dp2px(10);

        // 定义间隔
        layoutParam.setMargins(0, ConvertUtils.dp2px(10), 0, 0);

        mLayout.setLayoutParams(layoutParam);
    }

    @Override
    protected void onRelease() {
        this.mLayout = null;
        this.imviBg = null;
        this.imviContent = null;
    }
}
