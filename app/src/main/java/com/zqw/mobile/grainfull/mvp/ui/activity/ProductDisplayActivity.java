package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.idl.face.platform.utils.DensityUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.umeng.analytics.MobclickAgent;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerProductDisplayComponent;
import com.zqw.mobile.grainfull.mvp.contract.ProductDisplayContract;
import com.zqw.mobile.grainfull.mvp.model.entity.GoodsEntity;
import com.zqw.mobile.grainfull.mvp.presenter.ProductDisplayPresenter;
import com.zqw.mobile.grainfull.mvp.ui.adapter.ProductDisplayAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Description:商品 - 横向滑动
 * <p>
 * Created on 2023/08/14 17:03
 *
 * @author 赤槿
 * module name is ProductDisplayActivity
 */
public class ProductDisplayActivity extends BaseActivity<ProductDisplayPresenter> implements ProductDisplayContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_product_display)
    LinearLayout contentLayout;                                                                     // 主布局

    @BindView(R.id.revi_productdisplay_content)
    RecyclerView mRecyclerView;

    /*------------------------------------------------业务区域------------------------------------------------*/
    @Inject
    LinearLayoutManager mLayoutManager;

    @Inject
    ProductDisplayAdapter mAdapter;                                                                 // 内容适配器

    @Inject
    List<GoodsEntity> mList;

    @Override
    protected void onDestroy() {
        // super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        DefaultAdapter.releaseAllHolder(mRecyclerView);
        super.onDestroy();

        this.mLayoutManager = null;
        this.mAdapter = null;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerProductDisplayComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_product_display;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("商品展示");

        // 友盟统计 - 自定义事件
        MobclickAgent.onEvent(getApplicationContext(), "product_display_open");
        initRecyclerView();
        loadData();
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
//        mAdapter.setOnItemClickListener(this);

        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.left = DensityUtils.dip2px(getApplicationContext(), 10);
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItemCount = mLayoutManager.findFirstVisibleItemPosition();
                //一个itme的宽度
                double w = (ArmsUtils.getScreenWidth(getApplicationContext()) - DensityUtils.dip2px(getApplicationContext(), 65)) / 4;
                //第一个view
                View viewItem = mLayoutManager.findViewByPosition(firstVisibleItemCount);
                if (viewItem != null) {
                    double left = (double) (viewItem.getLeft() * 1.0F) / w;
                    setScroll(firstVisibleItemCount, left);
                }
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 静止的时候 牵引效果
                if (newState == 0) {
                    double w = (ArmsUtils.getScreenWidth(getApplicationContext()) - DensityUtils.dip2px(getApplicationContext(), 16)) / 4;
                    int firstVisibleItemCount = mLayoutManager.findFirstVisibleItemPosition();
                    View findViewByPosition = mLayoutManager.findViewByPosition(firstVisibleItemCount);
                    if (findViewByPosition != null) {
                        int left = findViewByPosition.getLeft();
                        if (left == 0) {
                            return;
                        }

                        if (findViewByPosition.getLeft() * 1.0f / w > -0.5f) {
                            recyclerView.smoothScrollBy(left, 0);
                        } else {
                            recyclerView.smoothScrollBy((int) (w + left), 0);
                        }
                    }
                }
            }
        });
    }

    /**
     * 生成数据
     */
    private void loadData() {
        for (int i = 0; i < 20; i++) {
            GoodsEntity bean = new GoodsEntity();
            bean.labelPrice = "100.00";
            bean.oldlabelPrice = "200.00";
            bean.imgURL = "https://gss0.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/b21bb051f8198618eaddf0f44ced2e738bd4e62c.jpg";
            mList.add(bean);
        }

        mAdapter.notifyDataSetChanged();
    }

    /**
     * 设置滚动
     */
    private void setScroll(int firstVisibleItemCount, double left) {
        double mar = DensityUtils.dip2px(getApplicationContext(), 55);
        double mar1 = DensityUtils.dip2px(getApplicationContext(), 15);
        // 动态改变View的Margin
        setMargin(firstVisibleItemCount, mar);
        setMargin(firstVisibleItemCount + 1, mar);
        setMargin(firstVisibleItemCount + 2, mar);
        setMargin1(firstVisibleItemCount + 3, (int) mar1, (int) mar, left);
        setMargin2(firstVisibleItemCount + 4, (int) mar1);
    }


    /**
     * Item 1 - 3 设置 Margin
     */
    private void setMargin(int lastVisibleItemCount, double mar) {
        View viewItem = mLayoutManager.findViewByPosition(lastVisibleItemCount);
        if (viewItem != null) {
            LinearLayout layout = viewItem.findViewById(R.id.lila_productdisplayitemlayout_quickbuy);
            if (layout != null) {
                layout.setAlpha(0.0f);
            }
            TextView textView = viewItem.findViewById(R.id.txvi_productdisplayitemlayout_oldPrice);
            if (textView != null) {
                textView.setAlpha(1.0f);
            }
            RecyclerView.LayoutParams layoupar = (RecyclerView.LayoutParams) viewItem.getLayoutParams();
            double w = (ArmsUtils.getScreenWidth(getApplicationContext()) - DensityUtils.dip2px(getApplicationContext(), 65)) / 4;

            layoupar.width = (int) (w - DensityUtils.dip2px(getApplicationContext(), 5));
            layoupar.height = (int) (w * 113 / 70 - (DensityUtils.dip2px(getApplicationContext(), 10)));
            layoupar.topMargin = (int) mar;
            viewItem.setLayoutParams(layoupar);
        }
    }


    /**
     * Item 4 设置 Margin
     */
    private void setMargin1(int lastVisibleItemCount, int mar2, int mar1, double left) {
        View viewItem = mLayoutManager.findViewByPosition(lastVisibleItemCount);
        if (viewItem != null) {
            LinearLayout layout = viewItem.findViewById(R.id.lila_productdisplayitemlayout_quickbuy);
            if (layout != null) {
                layout.setAlpha(1.0f);
            }
            TextView textView = viewItem.findViewById(R.id.txvi_productdisplayitemlayout_oldPrice);
            if (textView != null) {
                textView.setAlpha(0f);
            }
            RecyclerView.LayoutParams layoupar = (RecyclerView.LayoutParams) viewItem.getLayoutParams();
            double w = (ArmsUtils.getScreenWidth(getApplicationContext()) - DensityUtils.dip2px(getApplicationContext(), 65)) / 4;
            layoupar.topMargin = (int) (mar2 - (mar2 - mar1) * Math.abs(left));
            layoupar.width = (int) (w - DensityUtils.dip2px(getApplicationContext(), 5));
            int w1 = layoupar.width;
            double w2 = w + DensityUtils.dip2px(getApplicationContext(), 15);
            layoupar.width = (int) (w2 - (w2 - w1) * Math.abs(left));
            double h1 = w * 113 / 70 - DensityUtils.dip2px(getApplicationContext(), 10);
            double h2 = w * 113 / 70 + DensityUtils.dip2px(getApplicationContext(), 30);
            layoupar.height = (int) (h2 - (h2 - h1) * Math.abs(left));
            viewItem.setLayoutParams(layoupar);
        }
    }

    /**
     * Item 5+ 设置 Margin
     */
    private void setMargin2(int lastVisibleItemCount, int mar) {
        View viewItem = mLayoutManager.findViewByPosition(lastVisibleItemCount);
        if (viewItem != null) {
            LinearLayout layout = viewItem.findViewById(R.id.lila_productdisplayitemlayout_quickbuy);
            if (layout != null) {
                layout.setAlpha(1.0f);
            }
            TextView textView = viewItem.findViewById(R.id.txvi_productdisplayitemlayout_oldPrice);
            if (textView != null) {
                textView.setAlpha(0f);
            }
            RecyclerView.LayoutParams layoupar = (RecyclerView.LayoutParams) viewItem.getLayoutParams();
            double w = (ArmsUtils.getScreenWidth(getApplicationContext()) - DensityUtils.dip2px(getApplicationContext(), 65)) / 4;
            layoupar.width = (int) (w + DensityUtils.dip2px(getApplicationContext(), 15));
            layoupar.height = (int) (w * 113 / 70 + DensityUtils.dip2px(getApplicationContext(), 30));
            layoupar.topMargin = mar;
            viewItem.setLayoutParams(layoupar);
        }
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