package com.zqw.mobile.grainfull.mvp.ui.fragment;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.app.utils.EventBusTags;
import com.zqw.mobile.grainfull.mvp.model.entity.HomeContentInfo;
import com.zqw.mobile.grainfull.mvp.model.entity.HomeContentResponse;
import com.zqw.mobile.grainfull.mvp.model.entity.MainEvent;
import com.zqw.mobile.grainfull.mvp.ui.adapter.HomeContentAdapter;
import com.zqw.mobile.grainfull.mvp.ui.widget.nestedrecyclerview.ChildRecyclerView;
import com.zqw.mobile.grainfull.mvp.ui.widget.nestedrecyclerview.OnUserVisibleChange;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * 首页集合内容
 */
public class HomeListContentView extends ChildRecyclerView implements OnUserVisibleChange {
    // 是否加载数据
    boolean hasLoadData = false;
    // 类型：0 = 已派单；1 = 已收货；2 = 已入库；
    private int mType = 0;
    // 页数
    private int pageNumber = 1;
    // 排序：1路程最近，2时间倒序
    private int sort = 1;

    // 数据源
    private List<HomeContentInfo> mList = new ArrayList<>();
    // 适配器
    private HomeContentAdapter mAdapter;
    // 是否是支持加载更多
    private boolean isLoadMore;

    /**
     * 设置类型
     */
    public void setType(int mType) {
        this.mType = mType;
        if (mAdapter != null) {
            mAdapter.setType(mType);
        }
    }

    public HomeListContentView(@NonNull Context context) {
        super(context);
        init();
    }

    public HomeListContentView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeListContentView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 初始化的入口
     */
    private void init() {
        Timber.i("#####init=%s", mType);
        initRecyclerView();
        initLoadMore();

    }

    /**
     * 初始化列表
     */
    private void initRecyclerView() {
        setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new HomeContentAdapter(mList);
        setAdapter(mAdapter);
    }

    /**
     * 初始化加载更多
     */
    private void initLoadMore() {
        addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                tryLoadMoreIfNeed();
            }


            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    /**
     * 如果需要，请尝试加载更多
     */
    private void tryLoadMoreIfNeed() {
        if (getAdapter() == null) return;
        RecyclerView.LayoutManager layoutManager = getLayoutManager();

        // 最后一项的下标 == 列表长度时加载更多接口
        if (((LinearLayoutManager) layoutManager).findLastVisibleItemPosition() == (layoutManager.getItemCount() - 1) && isLoadMore) {
            Timber.i("#####执行了 加载更多");
            // 请求数据
            EventBus.getDefault().post(new MainEvent(EventBusTags.NEW_HOME_MORE_TAG, mType, pageNumber), EventBusTags.HOME_TAG);
        }
    }

    @Override
    public void onUserVisibleChange(boolean isVisibleToUser) {
        if (!hasLoadData && isVisibleToUser) {
            onRefreshNavi();
        }
    }

    /**
     * 网络请求入口
     */
    public void onRefreshNavi() {
        Timber.i("#####onRefreshNavi type=%s", mType);
        // 清空数据
        mList.clear();
        // 添加一个loading效果
        mList.add(new HomeContentInfo());
        notifyDataSetChanged();
        // 请求数据
        EventBus.getDefault().post(new MainEvent(EventBusTags.NEW_HOME_REFRESH_TAG, mType, pageNumber), EventBusTags.HOME_TAG);
    }

    @Subscriber(tag = EventBusTags.HOME_TAG, mode = ThreadMode.POST)
    private void eventBusEvent(MainEvent mainEvent) {
        if (mainEvent.getCode() == EventBusTags.NEW_HOME_SUCC_TAG) {
            onSuccessHomeOrder(mainEvent.getInfoResponse());
        }
    }

    /**
     * 列表请求成功
     */
    public void onSuccessHomeOrder(HomeContentResponse info) {
        hasLoadData = true;

        if (!isLoadMore) {
            // 本次请求认定为  刷新，所以要清空数据
            mList.clear();
        }

        if (CommonUtils.isNotEmpty(info.getList())) {
            // 有数据
            mList.addAll(info.getList());
        } else {
            // 没有数据，添加一个空对象
            mList.add(new HomeContentInfo());
        }

        // 控制分页
        if ((pageNumber == 1 && info.getPages() == 0) || pageNumber == info.getPages()) {
            // 最后一页
            mAdapter.setShowBottom(true);
            isLoadMore = false;
        } else {
            // 还有后续页
            pageNumber++;
            mAdapter.setShowBottom(false);
            isLoadMore = true;
        }

        // 有数据时关闭loading效果
        for (HomeContentInfo item : mList) {
            item.setLoading(false);
        }
        notifyDataSetChanged();
    }

    /**
     * 刷新主列表和子列表
     */
    private void notifyDataSetChanged() {
        // 刷新子列表
        if (getAdapter() != null) {
            getAdapter().notifyDataSetChanged();
        }
    }
}
