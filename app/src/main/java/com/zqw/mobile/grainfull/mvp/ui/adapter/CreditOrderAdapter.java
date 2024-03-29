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
package com.zqw.mobile.grainfull.mvp.ui.adapter;

import android.view.View;

import androidx.annotation.NonNull;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.model.entity.HomeOrderInfo;
import com.zqw.mobile.grainfull.mvp.ui.holder.CreditOrderItemHolder;
import com.zqw.mobile.grainfull.mvp.ui.holder.HomeBottomHolder;

import java.util.List;


/**
 * ================================================
 * 展示 {@link DefaultAdapter} 的用法
 * <p>
 * Created by JessYan on 09/04/2016 12:57
 * ================================================
 */
public class CreditOrderAdapter extends DefaultAdapter<HomeOrderInfo> {
    // 内容
    private final int COMTENT_VIEW_TYPE = 0;
    // 底部
    private final int BOTTOM_VIEW_TYPE = 1;

    public CreditOrderAdapter(List<HomeOrderInfo> infos) {
        super(infos);
    }

    // 是否显示底部
    private boolean isShowBottom = false;

    public void setData(List<HomeOrderInfo> list){
        mInfos = list;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() > 0 ? super.getItemCount() + 1 : 0;
    }

    @Override
    public int getItemViewType(int position) {
        int bottom = getItemCount() - 1;

        if (position == bottom) {
            return BOTTOM_VIEW_TYPE;
        } else {
            return COMTENT_VIEW_TYPE;
        }
    }

    @NonNull
    @Override
    public BaseHolder<HomeOrderInfo> getHolder(@NonNull View v, int viewType) {
        if (viewType == BOTTOM_VIEW_TYPE) {
            return new HomeBottomHolder(v);
        } else {
            return new CreditOrderItemHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(BaseHolder<HomeOrderInfo> holder, int position) {
        if (holder instanceof HomeBottomHolder) {
            ((HomeBottomHolder) holder).initView(isShowBottom);
        } else {
            holder.setData(mInfos.get(position), position);
        }
    }

    @Override
    public int getLayoutId(int viewType) {
        if (viewType == BOTTOM_VIEW_TYPE) {
            return R.layout.common_bottom_item_layout;
        } else {
            return R.layout.creditorder_item_layout;
        }
    }
}
