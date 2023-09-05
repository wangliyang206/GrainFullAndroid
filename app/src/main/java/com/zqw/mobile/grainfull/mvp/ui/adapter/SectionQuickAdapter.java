package com.zqw.mobile.grainfull.mvp.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.model.entity.CategoryModal;

import java.util.List;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.adapter
 * @ClassName: SectionQuickAdapter
 * @Description:
 * @Author: WLY
 * @CreateDate: 2023/9/4 15:07
 */
public class SectionQuickAdapter extends BaseSectionQuickAdapter<CategoryModal, BaseViewHolder> {
    private final Context mContext;
    /**
     * 用于加载图片的管理类, 默认使用 Glide, 使用策略模式, 可替换框架
     */
    private final ImageLoader mImageLoader;

    public SectionQuickAdapter(Context context, int sectionHeadResId, int layoutResId, @Nullable List<CategoryModal> data) {
        super(sectionHeadResId, layoutResId, data);
        this.mContext = context;
        //可以在任何可以拿到 Context 的地方, 拿到 AppComponent, 从而得到用 Dagger 管理的单例对象
        AppComponent mAppComponent = ArmsUtils.obtainAppComponentFromContext(mContext);
        mImageLoader = mAppComponent.imageLoader();
    }

    @Override
    protected void convertHeader(@NonNull BaseViewHolder baseViewHolder, @NonNull CategoryModal categoryModal) {
        baseViewHolder.setText(R.id.txvi_categoryrightgridheader_header, categoryModal.getCategoryName());
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, CategoryModal categoryModal) {
        ImageView imviIcon = baseViewHolder.getView(R.id.imvi_categoryrightgrid_icon);
        mImageLoader.loadImage(mContext,
                ImageConfigImpl.builder().url(categoryModal.getIconUrl())
                        .imageView(imviIcon).build());

        baseViewHolder.setText(R.id.txvi_categoryrightgrid_name, categoryModal.getCategoryName());
    }
}