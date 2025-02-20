package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.blankj.utilcode.util.ScreenUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.stx.xhb.androidx.XBanner;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.di.component.DaggerGoodsDetailComponent;
import com.zqw.mobile.grainfull.mvp.contract.GoodsDetailContract;
import com.zqw.mobile.grainfull.mvp.model.entity.BannerBean;
import com.zqw.mobile.grainfull.mvp.model.entity.DetailInfo;
import com.zqw.mobile.grainfull.mvp.model.entity.GoodsBean;
import com.zqw.mobile.grainfull.mvp.model.entity.GoodsInfo;
import com.zqw.mobile.grainfull.mvp.model.entity.TopBanner;
import com.zqw.mobile.grainfull.mvp.presenter.GoodsDetailPresenter;
import com.zqw.mobile.grainfull.mvp.ui.adapter.AppraiseListSectionAdapter;
import com.zqw.mobile.grainfull.mvp.ui.adapter.ColorThumbListAdapter;
import com.zqw.mobile.grainfull.mvp.ui.adapter.GoodsDesImgListAdapter;
import com.zqw.mobile.grainfull.mvp.ui.adapter.GoodsListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Description: 商品详情
 * <p>
 * Created on 2025/02/18 11:35
 *
 * @author 赤槿
 * module name is GoodsDetailActivity
 */
public class GoodsDetailActivity extends BaseActivity<GoodsDetailPresenter> implements GoodsDetailContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.scrollerLayout)
    NestedScrollView scrollerLayout;                                                                // 滑动布局
    @BindView(R.id.detailGoodsLayout)                                                               // 商品
    LinearLayoutCompat detailGoodsLayout;
    @BindView(R.id.goodsImgBanner)
    XBanner goodsImgBanner;
    @BindView(R.id.colorOptionTv)
    TextView colorOptionTv;
    @BindView(R.id.colorThumbRv)
    RecyclerView colorThumbRv;
    @BindView(R.id.originalPriceTv)
    TextView originalPriceTv;
    @BindView(R.id.goodsNameTv)
    TextView goodsNameTv;

    @BindView(R.id.detailAppraiseLayout)                                                            // 评价
    LinearLayout detailAppraiseLayout;
    @BindView(R.id.appraiseTxt)
    TextView appraiseTxt;
    @BindView(R.id.appraiseRecyclerView)
    RecyclerView appraiseRecyclerView;

    @BindView(R.id.detailDesLayout)                                                                 // 详情
    LinearLayout detailDesLayout;
    @BindView(R.id.hdzqTv)
    TextView hdzqTv;
    @BindView(R.id.hdzqIv)
    ImageView hdzqIv;
    @BindView(R.id.dnyxTv)
    TextView dnyxTv;
    @BindView(R.id.dnyxIv)
    ImageView dnyxIv;
    @BindView(R.id.spjsTv)
    TextView spjsTv;
    @BindView(R.id.goodsDesImgList)
    RecyclerView goodsDesImgList;

    @BindView(R.id.detailRecommend)                                                                 // 推荐
    LinearLayout detailRecommend;
    @BindView(R.id.storeGoodsTv)
    TextView storeGoodsTv;
    @BindView(R.id.storeGoodsRv)
    RecyclerView storeGoodsRv;

    @BindView(R.id.detailHeaderLayout)                                                              // 顶部
    LinearLayout detailHeaderLayout;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.detailHeaderTabLayout)
    TabLayout detailHeaderTabLayout;

    @BindView(R.id.detailCartLayout)
    RelativeLayout detailCartLayout;                                                                // 购物车

    @BindView(R.id.backTop)
    FloatingActionButton backTop;                                                                   // 内容
    /*------------------------------------------------业务区域------------------------------------------------*/
    @Inject
    ImageLoader mImageLoader;                                                                       // 图片加载器
    @Inject
    ColorThumbListAdapter colorThumbListAdapter;                                                    // 选择【颜色】适配器
    @Inject
    AppraiseListSectionAdapter appraiseListSectionAdapter;                                          // 评价列表 适配器
    @Inject
    GoodsDesImgListAdapter goodsDesImgListAdapter;                                                  // 商品详情 适配器
    @Inject
    List<GoodsBean> mGoodsList;
    @Inject
    GoodsListAdapter goodsListAdapter;                                                              // 推荐商品列表 适配器

    // 状态栏高度
    private int statusBarHeight;
    // Tab数据源
    private ArrayList<String> tabs = new ArrayList<>(Arrays.asList("商品", "评价", "详情", "推荐"));

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mImageLoader = null;
        this.colorThumbListAdapter = null;
        this.appraiseListSectionAdapter = null;
        this.goodsDesImgListAdapter = null;
        this.goodsListAdapter = null;

        if (tabs != null) {
            tabs.clear();
            tabs = null;
        }
    }


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerGoodsDetailComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_goods_detail;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("");

        init();
        initData();
    }

    private void init() {
        statusBarHeight = CommonUtils.getHeight(getApplicationContext());
        detailHeaderLayout.setPadding(0, statusBarHeight, 0, 0);
        detailAppraiseLayout.setPadding(0, statusBarHeight, 0, 0);
        detailHeaderTabLayout.removeAllTabs();
        detailHeaderTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab != null) {
                    scrollToPositionByTab(tab.getPosition());
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab != null) {
                    scrollToPositionByTab(tab.getPosition());
                }
            }
        });

        for (String v : tabs) {
            detailHeaderTabLayout.addTab(detailHeaderTabLayout.newTab().setText(v));
        }

        LinearLayoutCompat.LayoutParams lp = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        lp.height = ArmsUtils.getScreenWidth(getApplicationContext()) + statusBarHeight;
        goodsImgBanner.setLayoutParams(lp);
        goodsImgBanner.setPadding(0, statusBarHeight, 0, 0);

        // 初始化，选择商品 颜色
        colorThumbRv.setAdapter(colorThumbListAdapter);
        colorThumbRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        colorThumbListAdapter.setOnItemClickListener((adapter, view, position) -> {
            List<BannerBean> list = (List<BannerBean>) adapter.getData();
            BannerBean banner = list.get(position);
            boolean flag = banner.isSelect();

            for (int i = 0; i < list.size(); i++) {
                list.get(i).setSelect(false);
            }
            list.get(position).setSelect(true);

            if (!flag) {
                colorThumbListAdapter.setList(list);
                showGoodsImgBanner(banner.getImgList());
            }
        });

        // 初始化，评价列表
        appraiseRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        appraiseRecyclerView.setAdapter(appraiseListSectionAdapter);

        // 初始化，商品详情
        goodsDesImgList.setAdapter(goodsDesImgListAdapter);
        goodsDesImgList.setLayoutManager(new LinearLayoutManager(this));

        // 初始化，推荐商品
        storeGoodsRv.setAdapter(goodsListAdapter);
        storeGoodsRv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
//        goodsListAdapter.getLoadMoreModule().setOnLoadMoreListener(() -> viewModel.loadMoreRecommendList());

        // 设置滚动更改侦听器
        scrollerLayout.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> handleScroll(scrollY));

        // 事件
        back.setOnClickListener(v -> finish());
        backTop.setOnClickListener(v -> scrollerLayout.smoothScrollTo(0, 0, 500));
    }

    private void initData() {
        if (mPresenter != null) {
            mPresenter.queryGoodsDetail();
        }
    }

    /**
     * 商品信息
     */
    private void showGoodsInfo(GoodsInfo goodsInfo) {
        if (!goodsInfo.getOriginalPrice().isEmpty()) {
            detailGoodsLayout.setVisibility(View.VISIBLE);
            originalPriceTv.setText("￥" + goodsInfo.getOriginalPrice());
        }
        goodsNameTv.setText(goodsInfo.getGoodsName());
    }

    /**
     * 显示商品详情
     */
    private void showDetailInfo(DetailInfo detailInfo) {
        hdzqTv.setText("活动专区");
        mImageLoader.loadImage(getApplicationContext(),
                ImageConfigImpl
                        .builder()
                        .placeholder(R.mipmap.default_img)
                        .errorPic(R.mipmap.default_img)
                        .url(detailInfo.getHdzq())
                        .imageView(hdzqIv)
                        .build());

        dnyxTv.setText("店内优选");
        mImageLoader.loadImage(getApplicationContext(),
                ImageConfigImpl
                        .builder()
                        .placeholder(R.mipmap.default_img)
                        .errorPic(R.mipmap.default_img)
                        .url(detailInfo.getDnyx())
                        .imageView(dnyxIv)
                        .build());

        spjsTv.setText("商品介绍");
        if (!detailInfo.getIntroductionList().isEmpty()) {
            detailDesLayout.setVisibility(View.VISIBLE);
            goodsDesImgListAdapter.setList(detailInfo.getIntroductionList());
            appraiseTxt.setText("评价");
        }
    }

    /**
     * 滚动到指定位置
     */
    private void scrollToPositionByTab(int position) {
        switch (position) {
            case 0:
                scrollerLayout.smoothScrollTo(0, 0, 200);
                break;
            case 1:
                scrollerLayout.smoothScrollTo(0, detailAppraiseLayout.getTop() - statusBarHeight - (int) CommonUtils.dipToPixels(getApplicationContext(), 50f), 200);
                break;
            case 2:
                scrollerLayout.smoothScrollTo(0, detailDesLayout.getTop() - statusBarHeight - (int) CommonUtils.dipToPixels(getApplicationContext(), 50f), 200);
                break;
            case 3:
                scrollerLayout.smoothScrollTo(0, detailRecommend.getTop() - statusBarHeight - (int) CommonUtils.dipToPixels(getApplicationContext(), 50f), 200);
                break;
        }
    }

    /**
     * 显示Banner
     */
    private void showGoodsImgBanner(List<String> data) {
        List<TopBanner> bannerList = new ArrayList<>();
        for (String v : data) {
            bannerList.add(new TopBanner(v, ""));
        }
        goodsImgBanner.setBannerData(bannerList);
        goodsImgBanner.loadImage((banner, model, view, position) -> {
            ImageView imageView = (ImageView) view;

            // 显示图片
            mImageLoader.loadImage(getApplicationContext(),
                    ImageConfigImpl.builder()
                            .url(data.get(position))
                            .imageView(imageView)
                            .build());
        });
    }

    private int currentTabPosition = 0;

    private void handleScroll(int dy) {
        float delayDy = dy * 0.65f;
        if (delayDy >= ScreenUtils.getScreenHeight()) {
            backTop.setVisibility(View.VISIBLE);
        } else {
            backTop.setVisibility(View.GONE);
        }

        if (delayDy >= CommonUtils.getHeight(this)) {
            detailHeaderLayout.setBackgroundResource(R.color.white);
            detailHeaderTabLayout.setVisibility(View.VISIBLE);
        } else {
            detailHeaderLayout.setBackgroundResource(android.R.color.transparent);
            detailHeaderTabLayout.setVisibility(View.INVISIBLE);
        }

        int appraiseToTop = detailAppraiseLayout.getTop() - statusBarHeight - (int) CommonUtils.dipToPixels(getApplicationContext(), 50f);
        int goodsDesToTop = detailDesLayout.getTop() - statusBarHeight - (int) CommonUtils.dipToPixels(getApplicationContext(), 50f);
        int recommendToTop = detailRecommend.getTop() - statusBarHeight - (int) CommonUtils.dipToPixels(getApplicationContext(), 50f);

        int tabPosition;
        if (dy >= 0 && dy < appraiseToTop) {
            tabPosition = 0;
        } else if (dy < goodsDesToTop) {
            tabPosition = 1;
        } else if (dy < recommendToTop) {
            tabPosition = 2;
        } else {
            tabPosition = 3;
        }

        if (currentTabPosition != tabPosition) {
            currentTabPosition = tabPosition;
            detailHeaderTabLayout.setScrollPosition(tabPosition, 0f, true);
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

    @Override
    public void loadData(List<BannerBean> bannerList, GoodsInfo goodsInfo, DetailInfo detailInfo, List<GoodsBean> goodsList) {
        if (!bannerList.isEmpty()) {
            BannerBean current = null;
            for (BannerBean bean : bannerList) {
                if (bean.isSelect()) {
                    current = bean;
                    break;
                }
            }
            if (current != null) {
                showGoodsImgBanner(current.getImgList());
                colorThumbListAdapter.setList(bannerList);
                colorOptionTv.setText(bannerList.size() + "色可选");
            }
        }

        showGoodsInfo(goodsInfo);

        if (!goodsInfo.getAppraiseList().isEmpty()) {
            detailAppraiseLayout.setVisibility(View.VISIBLE);
            appraiseListSectionAdapter.setList(goodsInfo.getAppraiseList());
        }

        showDetailInfo(detailInfo);
        if (!goodsList.isEmpty()) {
            detailRecommend.setVisibility(View.VISIBLE);
            storeGoodsTv.setText("同店好货");
            mGoodsList.clear();
            mGoodsList.addAll(goodsList);
            goodsListAdapter.notifyDataSetChanged();
        }
    }
}