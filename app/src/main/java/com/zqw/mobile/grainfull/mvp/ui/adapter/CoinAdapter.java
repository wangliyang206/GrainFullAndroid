package com.zqw.mobile.grainfull.mvp.ui.adapter;

import android.content.Context;

import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.model.entity.CoinInfo;
import com.zqw.mobile.grainfull.mvp.ui.widget.hrecyclerview.CommonAdapter;
import com.zqw.mobile.grainfull.mvp.ui.widget.hrecyclerview.CommonViewHolder;

import java.util.List;

/**
 * Created by chawei on 2018/4/29.
 */

public class CoinAdapter extends CommonAdapter<CoinInfo> {

    private CommonViewHolder.onItemCommonClickListener commonClickListener;

    public CoinAdapter(Context context, List<CoinInfo> dataList, int layoutId, CommonViewHolder.onItemCommonClickListener listener) {
        super(context, dataList, layoutId);
        commonClickListener = listener;
    }

    @Override
    public void bindData(CommonViewHolder holder, CoinInfo data) {

        holder.setText(R.id.id_name, data.name)
                .setText(R.id.id_tv_price_last, data.priceLast)
                .setText(R.id.id_tv_rise_rate24, data.riseRate24)
                .setText(R.id.id_tv_vol24, data.vol24)
                .setText(R.id.id_tv_close, data.close)
                .setText(R.id.id_tv_open, data.open)
                .setText(R.id.id_tv_bid, data.bid)
                .setText(R.id.id_tv_ask, data.ask)
                .setText(R.id.id_tv_percent, data.amountPercent)
                .setCommonClickListener(commonClickListener);
    }
}
