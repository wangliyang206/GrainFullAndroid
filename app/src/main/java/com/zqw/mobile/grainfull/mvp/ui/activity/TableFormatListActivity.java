package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerTableFormatListComponent;
import com.zqw.mobile.grainfull.mvp.contract.TableFormatListContract;
import com.zqw.mobile.grainfull.mvp.model.entity.CoinInfo;
import com.zqw.mobile.grainfull.mvp.presenter.TableFormatListPresenter;
import com.zqw.mobile.grainfull.mvp.ui.adapter.CoinAdapter;
import com.zqw.mobile.grainfull.mvp.ui.widget.hrecyclerview.CommonViewHolder;
import com.zqw.mobile.grainfull.mvp.ui.widget.hrecyclerview.HRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Description: 表格式列表
 * <p>
 * Created on 2024/09/20 11:28
 *
 * @author 赤槿
 * module name is TableFormatListActivity
 */
public class TableFormatListActivity extends BaseActivity<TableFormatListPresenter> implements TableFormatListContract.View {

    /*------------------------------------------控件信息------------------------------------------*/
    @BindView(R.id.view_hrecyclerview_layout)
    HRecyclerView hRecyclerView;

    /*------------------------------------------业务信息------------------------------------------*/
    private ArrayList<CoinInfo> mDataModels;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerTableFormatListComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_table_format_list;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("表格式列表");

        mDataModels = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            CoinInfo coinInfo = new CoinInfo();
            coinInfo.name = "USDT";
            coinInfo.priceLast = "20.0";
            coinInfo.riseRate24 = "0.2";
            coinInfo.vol24 = "10020";
            coinInfo.close = "22.2";
            coinInfo.open = "40.0";
            coinInfo.bid = "33.2";
            coinInfo.ask = "19.0";
            coinInfo.amountPercent = "33.3%";
            mDataModels.add(coinInfo);
        }

        hRecyclerView.setHeaderListData(getResources().getStringArray(R.array.right_title_name));

        CoinAdapter adapter = new CoinAdapter(this, mDataModels, R.layout.item_layout, new CommonViewHolder.onItemCommonClickListener() {
            @Override
            public void onItemClickListener(int position) {
                Toast.makeText(TableFormatListActivity.this, "position--->" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClickListener(int position) {

            }
        });

        hRecyclerView.setAdapter(adapter);
    }

    public Activity getActivity() {
        return this;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }
}