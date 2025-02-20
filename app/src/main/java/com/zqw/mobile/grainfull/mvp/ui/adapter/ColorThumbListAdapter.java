package com.zqw.mobile.grainfull.mvp.ui.adapter;

import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.LayoutRes;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.model.entity.BannerBean;

import java.util.List;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.adapter
 * @ClassName: ColorThumbListAdapter
 * @Description: 颜色缩略图列表适配器
 * @Author: WLY
 * @CreateDate: 2025/2/18 14:58
 */
public class ColorThumbListAdapter extends BaseQuickAdapter<BannerBean, BaseViewHolder> {
    // 显示图片对象
    private ImageLoader mImageLoader;

    public ColorThumbListAdapter(@LayoutRes int layoutResId, List<BannerBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder holder, BannerBean bean) {
        ImageView thumbImg = holder.getView(R.id.thumbImg);

        if (mImageLoader == null) {
            AppComponent mAppComponent = ArmsUtils.obtainAppComponentFromContext(getContext());
            mImageLoader = mAppComponent.imageLoader();
        }

        mImageLoader.loadImage(getContext(),
                ImageConfigImpl
                        .builder()
                        .placeholder(R.mipmap.default_img)
                        .errorPic(R.mipmap.mis_default_error)
                        .url(bean.getThumb())
                        .imageView(thumbImg)
                        .build());

        LinearLayout colorOptionLayout = holder.getView(R.id.colorOptionLayout);
        if (bean.isSelect()) {
            colorOptionLayout.setBackgroundResource(R.drawable.detail_color_thumb_select);
        } else {
            colorOptionLayout.setBackgroundResource(R.drawable.detail_color_thumb_unselect);
        }
    }
}