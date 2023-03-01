package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.lcw.library.imagepicker.ImagePicker;
import com.umeng.analytics.MobclickAgent;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.global.Constant;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.di.component.DaggerPictureCompressionComponent;
import com.zqw.mobile.grainfull.mvp.contract.PictureCompressionContract;
import com.zqw.mobile.grainfull.mvp.presenter.PictureCompressionPresenter;
import com.zqw.mobile.grainfull.mvp.ui.adapter.FragmentPagerAdapter;
import com.zqw.mobile.grainfull.mvp.ui.fragment.CompressPicScaleFragment;
import com.zqw.mobile.grainfull.mvp.ui.fragment.CompressPicSizeFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Description:图片压缩
 * <p>
 * Created on 2023/02/15 13:51
 *
 * @author 赤槿
 * module name is PictureCompressionActivity
 */
public class PictureCompressionActivity extends BaseActivity<PictureCompressionPresenter> implements PictureCompressionContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_picture_compression)
    LinearLayout contentLayout;                                                                     // 主布局

    @BindView(R.id.tab_picturecompression_nav)
    TabLayout mTabLayout;
    @BindView(R.id.view_picturecompression_pager)
    ViewPager mViewPager;

    /*------------------------------------------------业务区域------------------------------------------------*/
    /**
     * 保存Title
     */
    private List<String> itemTitle = new ArrayList<>();

    /**
     * 管理器
     */
    private FragmentPagerAdapter mAdapter;

    /**
     * 当前Fragment
     */
    private Fragment mCurFragment;

    @Override
    protected void onDestroy() {
        if (CommonUtils.isNotEmpty(itemTitle)) {
            itemTitle.clear();
            itemTitle = null;
        }
        super.onDestroy();

        this.mAdapter = null;
        this.mCurFragment = null;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerPictureCompressionComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_picture_compression;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("图片压缩");

        // 友盟统计 - 自定义事件
        MobclickAgent.onEvent(getApplicationContext(), "picture_compression");

        initTab();
        initContent();
    }

    /**
     * 初始化Tab
     */
    private void initTab() {
//        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.common_text_tips_color), ContextCompat.getColor(this, R.color.teal));
        mTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.teal));
        ViewCompat.setElevation(mTabLayout, 10);

        itemTitle.add("按大小压缩");
        itemTitle.add("按比例压缩");
    }

    /**
     * 初始化订单列表
     */
    private void initContent() {

        mViewPager.setAdapter(mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0)
                    return CompressPicSizeFragment.instantiate();
                else
                    return CompressPicScaleFragment.instantiate();
            }

            @Override
            public int getCount() {
                return itemTitle != null ? itemTitle.size() : 0;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return itemTitle.get(position);
            }

            @Override
            public void setPrimaryItem(ViewGroup container, int position, Object object) {
                super.setPrimaryItem(container, position, object);
                if (mCurFragment == null) {
                    commitUpdate();
                }
                mCurFragment = (Fragment) object;
            }

            //this is called when notifyDataSetChanged() is called
            @Override
            public int getItemPosition(@NotNull Object object) {
                return PagerAdapter.POSITION_NONE;
            }

        });
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    mAdapter.commitUpdate();
                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

            }
        });

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setSmoothScrollingEnabled(true);
        mViewPager.setOffscreenPageLimit(0);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.REQUEST_SELECT_IMAGES_CODE) {
                // 返回的参数
                ((CompressPicSizeFragment) mCurFragment).onResult(data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES));
            } else if (requestCode == Constant.MAIN_AVATAR) {
                // 返回的参数
                ((CompressPicScaleFragment) mCurFragment).onResult(data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES));
            }
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