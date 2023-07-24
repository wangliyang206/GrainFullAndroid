package com.zqw.mobile.grainfull.mvp.ui.holder;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.baidu.idl.face.platform.utils.DensityUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.model.entity.HomeContentInfo;
import com.zqw.mobile.grainfull.mvp.model.entity.NewHomeInfo;
import com.zqw.mobile.grainfull.mvp.ui.adapter.HomeBannerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import timber.log.Timber;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.holder
 * @ClassName: HomeTopHolder
 * @Description: 首页2.0顶部模块
 * @Author: WLY
 * @CreateDate: 2023/7/14 15:42
 */
public class HomeTopHolder extends BaseHolder<NewHomeInfo> implements View.OnClickListener {
    @BindView(R.id.home_top_item_layout)
    RelativeLayout mLayout;

    @BindView(R.id.view_hometopitemlayout_pager)
    ViewPager mViewPager;
    @BindView(R.id.revi_hometopitemlayout_content)
    RecyclerView mRecyclerView;

    private AppComponent mAppComponent;
    /**
     * 用于加载图片的管理类, 默认使用 Glide, 使用策略模式, 可替换框架
     */
    private ImageLoader mImageLoader;

    private List<View> mViewList;
    private PagerAdapter mPagerAdapter;
    private List<HomeContentInfo> mContentList;
    private LinearLayoutManager mLayoutManager;
    private HomeBannerAdapter mAdapter;

    public HomeTopHolder(View itemView) {
        super(itemView);

        //可以在任何可以拿到 Context 的地方, 拿到 AppComponent, 从而得到用 Dagger 管理的单例对象
        mAppComponent = ArmsUtils.obtainAppComponentFromContext(itemView.getContext());
        mImageLoader = mAppComponent.imageLoader();

        // 初始化
        initViewPager();
        initRecyclerView();
    }

    /**
     * 初始化 ViewPager
     */
    private void initViewPager() {
        mViewList = new ArrayList<>();
        mViewPager.setAdapter(mPagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                //将page总数返回整型最大，使之ViewPager可以无限向右滑动
                return mViewList.size() != 0 ? Integer.MAX_VALUE : 0;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            // 添加界面，一般会添加当前页和左右两边的页面
            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                //将ViewPager的index转化为实际中Page的index
                View view = mViewList.get(position % mViewList.size());
                container.addView(view);
                return view;
            }

            // 去除页面
            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//                container.removeView(mViewList.get(position % mViewList.size()));
                container.removeView((View)object);
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Timber.i("###### onPageSelected######1position=" + position);
                Timber.i("###### onPageSelected######2position=" + position % mViewList.size());

                int firstVisibleItemCount = mLayoutManager.findFirstVisibleItemPosition();
                if (position % mContentList.size() != firstVisibleItemCount) {
                    Timber.i("###### onPageSelected######1firstVisibleItemCount=" + firstVisibleItemCount);
                    double w = (ArmsUtils.getScreenWidth(itemView.getContext()) - DensityUtils.dip2px(itemView.getContext(), 16)) / 4;
                    View findViewByPosition = mLayoutManager.findViewByPosition(position);
                    if (findViewByPosition != null) {
                        int left = findViewByPosition.getLeft();
                        if (left == 0) {
                            return;
                        }
                        if ((float) left / w > -0.5f) {
                            mRecyclerView.smoothScrollBy(left, 0);
                        } else {
                            mRecyclerView.smoothScrollBy((int) (w + left), 0);
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 初始化 RecyclerView
     */
    private void initRecyclerView() {
        mContentList = new ArrayList<>();
        mAdapter = new HomeBannerAdapter(mContentList);
        mLayoutManager = new LinearLayoutManager(itemView.getContext(), RecyclerView.HORIZONTAL, false);
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.left = DensityUtils.dip2px(itemView.getContext(), 10);
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItemCount = mLayoutManager.findFirstVisibleItemPosition();
                //一个itme的宽度
                double w = (ArmsUtils.getScreenWidth(itemView.getContext()) - DensityUtils.dip2px(itemView.getContext(), 65)) / 4;
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
                    double w = (ArmsUtils.getScreenWidth(itemView.getContext()) - DensityUtils.dip2px(itemView.getContext(), 16)) / 4;
                    int firstVisibleItemCount = mLayoutManager.findFirstVisibleItemPosition();
                    View findViewByPosition = mLayoutManager.findViewByPosition(firstVisibleItemCount);
                    if (findViewByPosition != null) {
                        int left = findViewByPosition.getLeft();
                        if (left == 0) {
                            return;
                        }
                        if ((float) left / w > -0.5f) {
                            recyclerView.smoothScrollBy(left, 0);
                        } else {
                            recyclerView.smoothScrollBy((int) (w + left), 0);
                        }
                    }

                    Timber.i("###### onScrollStateChanged######1position=" + firstVisibleItemCount);
                    Timber.i("###### onScrollStateChanged######2position=" + firstVisibleItemCount % mContentList.size());

                    if (firstVisibleItemCount % mContentList.size() != mViewPager.getCurrentItem()) {
                        Timber.i("###### onScrollStateChanged######2 ----------");
                        mViewPager.setCurrentItem(firstVisibleItemCount % mContentList.size());
                    }
                }
            }
        });
    }

    /**
     * 设置滚动
     */
    private void setScroll(int firstVisibleItemCount, double left) {
        // 动态改变View的Margin
        setMargin(firstVisibleItemCount, left);
        setMargin1(firstVisibleItemCount + 1);
        setMargin1(firstVisibleItemCount + 2);
        setMargin1(firstVisibleItemCount + 3);
        setMargin2(firstVisibleItemCount + 4);
        setMargin2(firstVisibleItemCount + 5);
    }

    /**
     * Item 1 设置 Margin
     */
    private void setMargin(int lastVisibleItemCount, double left) {
        View viewItem = mLayoutManager.findViewByPosition(lastVisibleItemCount);
        if (viewItem != null) {
            LinearLayout layoutBottom = viewItem.findViewById(R.id.lila_homebanneritemlayout_bottom);
            if (layoutBottom != null) {
                layoutBottom.setVisibility(View.GONE);
            }
            LinearLayout layoutTop = viewItem.findViewById(R.id.lila_homebanneritemlayout_top);
            if (layoutTop != null) {
                layoutTop.setVisibility(View.VISIBLE);
            }
            RecyclerView.LayoutParams layoupar = (RecyclerView.LayoutParams) viewItem.getLayoutParams();
            double w = (ArmsUtils.getScreenWidth(itemView.getContext()) - DensityUtils.dip2px(itemView.getContext(), 65)) / 4;

            layoupar.width = (int) (w - DensityUtils.dip2px(itemView.getContext(), 10));
            int w1 = layoupar.width;
            int w2 = (int) (w + DensityUtils.dip2px(itemView.getContext(), 30));
            layoupar.width = (int) (w2 - (w2 - w1) * Math.abs(left));
            layoupar.height = DensityUtils.dip2px(itemView.getContext(), 90);
            layoupar.topMargin = 50;
            viewItem.setLayoutParams(layoupar);
        }
    }

    /**
     * Item 2 ~ 4 设置 Margin
     */
    private void setMargin1(int lastVisibleItemCount) {
        View viewItem = mLayoutManager.findViewByPosition(lastVisibleItemCount);
        if (viewItem != null) {
            LinearLayout layoutBottom = viewItem.findViewById(R.id.lila_homebanneritemlayout_bottom);
            if (layoutBottom != null) {
                layoutBottom.setVisibility(View.VISIBLE);
            }
            LinearLayout layoutTop = viewItem.findViewById(R.id.lila_homebanneritemlayout_top);
            if (layoutTop != null) {
                layoutTop.setVisibility(View.GONE);
            }
            RecyclerView.LayoutParams layoupar = (RecyclerView.LayoutParams) viewItem.getLayoutParams();
            double w = (ArmsUtils.getScreenWidth(itemView.getContext()) - DensityUtils.dip2px(itemView.getContext(), 65)) / 4;
            layoupar.width = (int) (w - DensityUtils.dip2px(itemView.getContext(), 10));
            layoupar.height = DensityUtils.dip2px(itemView.getContext(), 90);
            layoupar.topMargin = 50;
            viewItem.setLayoutParams(layoupar);
        }
    }

    /**
     * Item 5+ 设置 Margin
     */
    private void setMargin2(int lastVisibleItemCount) {
        View viewItem = mLayoutManager.findViewByPosition(lastVisibleItemCount);
        if (viewItem != null) {
            LinearLayout layoutBottom = viewItem.findViewById(R.id.lila_homebanneritemlayout_bottom);
            if (layoutBottom != null) {
                layoutBottom.setVisibility(View.VISIBLE);
            }
            LinearLayout layoutTop = viewItem.findViewById(R.id.lila_homebanneritemlayout_top);
            if (layoutTop != null) {
                layoutTop.setVisibility(View.GONE);
            }
            RecyclerView.LayoutParams layoupar = (RecyclerView.LayoutParams) viewItem.getLayoutParams();
            double w = (ArmsUtils.getScreenWidth(itemView.getContext()) - DensityUtils.dip2px(itemView.getContext(), 65)) / 4;
            layoupar.width = (int) (w - DensityUtils.dip2px(itemView.getContext(), 10));
            layoupar.height = DensityUtils.dip2px(itemView.getContext(), 90);
            layoupar.topMargin = 50;
            viewItem.setLayoutParams(layoupar);
        }
    }

    @Override
    public void setData(@NotNull NewHomeInfo info, int position) {
        mContentList.clear();
        mViewList.clear();
        mContentList.addAll(info.getTopList());

        for (HomeContentInfo item : mContentList) {
            ImageView view = (ImageView) View.inflate(itemView.getContext(), R.layout.view_pager_image, null);
            // 显示图片
            mImageLoader.loadImage(mLayout.getContext(), ImageConfigImpl.builder().url(item.getImage())
                    .errorPic(R.mipmap.mis_default_error)
                    .placeholder(R.mipmap.mis_default_error)
                    .imageRadius(ConvertUtils.dp2px(10))
                    .imageView(view).build());
            mViewList.add(view);
        }

        mAdapter.notifyDataSetChanged();
        mPagerAdapter.notifyDataSetChanged();
    }

    /**
     * 动态设置Margin
     */
    private void setLayoutMargin(boolean isSet) {
        ConstraintLayout.LayoutParams layoutParam = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        if (isSet)
            layoutParam.setMargins(ConvertUtils.dp2px(10), 0, 0, 0);
        else
            layoutParam.setMargins(0, 0, 0, 0);

        mLayout.setLayoutParams(layoutParam);
    }

    @Override
    protected void onRelease() {
        // super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        DefaultAdapter.releaseAllHolder(mRecyclerView);
        this.mLayout = null;

        this.mLayoutManager = null;
        this.mAdapter = null;
        this.mContentList = null;
        this.mPagerAdapter = null;
        this.mViewList = null;
        this.mAppComponent = null;
        this.mImageLoader = null;
    }
}