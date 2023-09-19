package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.umeng.analytics.MobclickAgent;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerCardOverlapComponent;
import com.zqw.mobile.grainfull.mvp.contract.CardOverlapContract;
import com.zqw.mobile.grainfull.mvp.model.entity.HomeActionBarInfo;
import com.zqw.mobile.grainfull.mvp.presenter.CardOverlapPresenter;
import com.zqw.mobile.grainfull.mvp.ui.adapter.CardAdapter;
import com.zqw.mobile.grainfull.mvp.ui.widget.stackedcards.CardConfig;
import com.zqw.mobile.grainfull.mvp.ui.widget.stackedcards.OverLayCardLayoutManager;
import com.zqw.mobile.grainfull.mvp.ui.widget.stackedcards.TanTanCallback;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Description: 卡片重叠滑动 - 探探效果
 * <p>
 * Created on 2023/09/19 11:46
 *
 * @author 赤槿
 * module name is CardOverlapActivity
 */
public class CardOverlapActivity extends BaseActivity<CardOverlapPresenter> implements CardOverlapContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_card_overlap)
    LinearLayout contentLayout;                                                                     // 主布局

    @BindView(R.id.revi_cardoverlap_content)
    RecyclerView mRecyclerView;

    /*------------------------------------------------业务区域------------------------------------------------*/
    @Inject
    OverLayCardLayoutManager mLayoutManager;

    @Inject
    CardAdapter mAdapter;                                                                           // 内容适配器

    @Inject
    List<HomeActionBarInfo> mList;

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
        DaggerCardOverlapComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_card_overlap;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("卡片重叠滑动");

        // 友盟统计 - 自定义事件
        MobclickAgent.onEvent(getApplicationContext(), "card_overlap_open");

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
        CardConfig.initConfig(this);

    }

    /**
     * 加载数据
     */
    private void loadData() {
        mList.clear();
        addData("http://imgs.ebrun.com/resources/2016_03/2016_03_25/201603259771458878793312_origin.jpg", "小姐姐");
        addData("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fsafe-img.xhscdn.com%2Fbw1%2F2bf87843-3aa2-4d40-ad09-6ce18e8eb41c%3FimageView2%2F2%2Fw%2F1080%2Fformat%2Fjpg&refer=http%3A%2F%2Fsafe-img.xhscdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1692254573&t=0c84282280e6065799903f0b152b5672", "云韵");
        addData("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fsafe-img.xhscdn.com%2Fbw1%2F0914cf40-a7a9-41ec-8b09-8a7cc1fd87b9%3FimageView2%2F2%2Fw%2F1080%2Fformat%2Fjpg&refer=http%3A%2F%2Fsafe-img.xhscdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1692254587&t=3bf3934cbbcfc8b8024e299e8b6cc67b", "海波东");
        addData("https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2021%2F1109%2Fda793cecj00r2asr5003tc000hs0116c.jpg&thumbnail=660x2147483647&quality=80&type=jpg", "纳兰嫣然1");
        addData("https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2021%2F1109%2F3571be09j00r2asr5001ic000hs00tzc.jpg&thumbnail=660x2147483647&quality=80&type=jpg", "美杜莎1");
        addData("https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2022%2F0127%2F81845b82j00r6c5gh001wc000hs00vmc.jpg&thumbnail=660x2147483647&quality=80&type=jpg", "云芝1");
        addData("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fci.xiaohongshu.com%2Fa4dd321e-30b9-c0c9-bc20-14d206c934c6%3FimageView2%2F2%2Fw%2F1080%2Fformat%2Fjpg&refer=http%3A%2F%2Fci.xiaohongshu.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1692254680&t=6da8da567645370cf50dc3ace76369c6", "女王大人");
        addData("https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2021%2F1214%2F4cee9f56j00r43gy20019c000hs00vmc.jpg&thumbnail=660x2147483647&quality=80&type=jpg", "美杜莎2");
        addData("https://img.qzonei.com/data/attachment/portal/202206/30/102432q0lnhvlxqez7nb7e.jpg", "萧薰儿");
        addData("https://pics2.baidu.com/feed/0b7b02087bf40ad173890dc7bbdb39d9a8eccea7.jpeg?token=a8e0ebe159ca580d31e202d5c627a1d0&s=2575168FC002E4FF5E95E6AB0300E017", "小医仙");
        addData("https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2021%2F1030%2F97f21769j00r1s70y001pc000hs00vnc.jpg&thumbnail=650x2147483647&quality=80&type=jpg", "云芝2");
        addData("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fsafe-img.xhscdn.com%2Fbw%2F63525cbc-130f-4f52-9ad1-bc87693a1900%3FimageView2%2F2%2Fw%2F1080%2Fformat%2Fjpg&refer=http%3A%2F%2Fsafe-img.xhscdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1692254799&t=18b702ec5d4b78b02410f9c89b4b1df3", "凌影");
        addData("https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2021%2F1030%2F8ead0baaj00r1s70y0029c000hs00vmc.jpg&thumbnail=650x2147483647&quality=80&type=jpg", "萧媚儿");
        addData("https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2021%2F1205%2F1b0fce8cj00r3m5lx0025c000hs00vjc.jpg&thumbnail=660x2147483647&quality=80&type=jpg", "纳兰嫣然2");

        final TanTanCallback callback = new TanTanCallback(mRecyclerView, mAdapter, mList);
        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    /**
     * 添加一条数据
     */
    private void addData(String url, String name) {
        HomeActionBarInfo swipeCardBean = new HomeActionBarInfo();
        swipeCardBean.setUrl(url);
        swipeCardBean.setName(name);
        mList.add(swipeCardBean);
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