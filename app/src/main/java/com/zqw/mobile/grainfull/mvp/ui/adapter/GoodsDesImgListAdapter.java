package com.zqw.mobile.grainfull.mvp.ui.adapter;

import android.widget.ImageView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;

import java.util.List;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.adapter
 * @ClassName: GoodsDesImgListAdapter
 * @Description: 货物清单适配器
 * @Author: WLY
 * @CreateDate: 2025/2/18 16:12
 */
public class GoodsDesImgListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    // 显示图片对象
    private ImageLoader mImageLoader;

    public GoodsDesImgListAdapter(@LayoutRes int layoutResId, @NonNull List<String> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, @NonNull String url) {
        ImageView desImg = holder.getView(R.id.desImg);

        if (mImageLoader == null) {
            AppComponent mAppComponent = ArmsUtils.obtainAppComponentFromContext(getContext());
            mImageLoader = mAppComponent.imageLoader();
        }

        mImageLoader.loadImage(getContext(),
                ImageConfigImpl
                        .builder()
                        .placeholder(R.mipmap.default_img)
                        .errorPic(R.mipmap.default_img)
                        .url(url)
                        .imageView(desImg)
                        .build());
    }
}
