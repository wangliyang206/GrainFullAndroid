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

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.mvp.model.entity.HomeOrderInfo;

import butterknife.BindView;

/**
 * ================================================
 * <p>
 * Created by JessYan on 9/4/16 12:56
 * ================================================
 */
public class CreditOrderItemHolder extends BaseHolder<HomeOrderInfo> {
    @BindView(R.id.view_creditorderitemlayout_use_data)
    ConstraintLayout viewUseData;

    /*-----------------------回收订单-----------------------*/
    @BindView(R.id.txvi_creditorderitemlayout_orderNo)
    TextView txviOrderNo;
    @BindView(R.id.txvi_creditorderitemlayout_status)
    TextView txviStatus;
    @BindView(R.id.txvi_creditorderitemlayout_orgname)
    TextView txviOrgName;
    @BindView(R.id.txvi_creditorderitemlayout_shopname)
    TextView txviShopName;
    @BindView(R.id.txvi_creditorderitemlayout_recycler)
    TextView txviRecycler;
    @BindView(R.id.txvi_creditorderitemlayout_weight)
    TextView txviWeight;
    @BindView(R.id.txvi_creditorderitemlayout_datetime)
    TextView txviDateTime;
    @BindView(R.id.txvi_creditorderitemlayout_money)
    TextView txviMoney;


    public CreditOrderItemHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(@NonNull HomeOrderInfo info, int position) {

        txviOrderNo.setText("订单号：" + CommonUtils.isEmptyReturnStr(info.getOrderNo()));
        txviStatus.setText(CommonUtils.isEmptyReturnStr(info.getStatusOrder()));
        txviOrgName.setText("回收商：" + CommonUtils.isEmptyReturnStr(info.getOrgName()));
        txviShopName.setText(CommonUtils.isEmptyReturnStr(info.getStoreName()));
        txviRecycler.setText("回收员：" + CommonUtils.isEmptyReturnStr(info.getRecycleName()));

        txviDateTime.setText("收货时间：" + CommonUtils.isEmptyReturnStr(info.getRecycleTime()));
        txviWeight.setText(info.getWeight() + "KG");

        if (info.getAmount() > 0)
            txviMoney.setText(info.getAmount() + "元");
        else
            txviMoney.setText("");

        viewUseData.setTag(info);
        viewUseData.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (!CommonUtils.isDoubleClick()) {
            HomeOrderInfo info = (HomeOrderInfo) view.getTag();
        }

    }

    /**
     * 在 Activity 的 onDestroy 中使用 {@link DefaultAdapter#releaseAllHolder(RecyclerView)} 方法 (super.onDestroy() 之前)
     * {@link BaseHolder#onRelease()} 才会被调用, 可以在此方法中释放一些资源
     */
    @Override
    protected void onRelease() {
        this.viewUseData = null;

        this.txviOrderNo = null;
        this.txviStatus = null;
        this.txviOrgName = null;
        this.txviShopName = null;
        this.txviRecycler = null;
        this.txviWeight = null;
        this.txviDateTime = null;
        this.txviMoney = null;
    }

}
