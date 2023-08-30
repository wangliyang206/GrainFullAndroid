package com.zqw.mobile.grainfull.mvp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ActivityUtils;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerLayoutForumComponent;
import com.zqw.mobile.grainfull.mvp.contract.LayoutForumContract;
import com.zqw.mobile.grainfull.mvp.model.entity.BannerBean;
import com.zqw.mobile.grainfull.mvp.model.entity.MenuBean;
import com.zqw.mobile.grainfull.mvp.model.entity.TabBean;
import com.zqw.mobile.grainfull.mvp.presenter.LayoutForumPresenter;
import com.zqw.mobile.grainfull.mvp.ui.activity.NewWindowX5Activity;
import com.zqw.mobile.grainfull.mvp.ui.adapter.NineGridAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.fragment
 * @ClassName: GridFragment
 * @Description: 操作栏
 * @Author: WLY
 * @CreateDate: 2023/8/29 17:49
 */
public class GridFragment extends BaseFragment<LayoutForumPresenter> implements LayoutForumContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.view_ninemenu_gridView)
    GridView gridView;                                                                              // 主布局

    /*------------------------------------------------业务区域------------------------------------------------*/
    private NineGridAdapter nineGridAdapter;
    private List<MenuBean> list;
    private int position;
    private int pageSize;

    public GridFragment(List<MenuBean> list, int position, int pageSize) {
        this.list = list;
        this.position = position;
        this.pageSize = pageSize;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerLayoutForumComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.nine_memu_gridview, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        nineGridAdapter = new NineGridAdapter(getContext(), list, position, pageSize);
        gridView.setAdapter(nineGridAdapter);
        nineGridAdapter.setOnGridItemClickListener(item -> {
            Bundle mBundle = new Bundle();
            mBundle.putString("TITLE", item.getMenuName());
            mBundle.putString("URL", item.getH5url());
            mBundle.putBoolean("isShowTop", true);

            ActivityUtils.startActivity(mBundle, NewWindowX5Activity.class);
        });
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void loadBanner(List<BannerBean> list) {

    }

    @Override
    public void loadAdvertisingBar(String url) {

    }

    @Override
    public void loadNineMenu(List<MenuBean> nineMenuList) {

    }

    @Override
    public void showTabLayout(List<TabBean> list) {

    }

    @Override
    public void showMessage(@NonNull String message) {

    }
}
