package com.zqw.mobile.grainfull.mvp.ui.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.model.entity.AppraiseBean;

import java.util.List;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.adapter
 * @ClassName: AppraiseListSectionAdapter
 * @Description: 评估列表部分适配器
 * @Author: WLY
 * @CreateDate: 2025/2/18 15:56
 */
public class AppraiseListSectionAdapter extends BaseSectionQuickAdapter<AppraiseBean, BaseViewHolder> {
    // 显示图片对象
    private ImageLoader mImageLoader;

    public AppraiseListSectionAdapter(@LayoutRes int headerLayoutResId, @LayoutRes int itemLayoutResId, List<AppraiseBean> data) {
        super(headerLayoutResId, itemLayoutResId, data);

    }

    @Override
    protected void convertHeader(@NonNull BaseViewHolder helper, @NonNull AppraiseBean item) {
        ImageView commentatorImg = helper.getView(R.id.commentatorImg);
        TextView commentatorTxt = helper.getView(R.id.commentatorTxt);
        TextView contentTxt = helper.getView(R.id.contentTxt);
        TextView modelTxt = helper.getView(R.id.modelTxt);

        if (mImageLoader == null) {
            AppComponent mAppComponent = ArmsUtils.obtainAppComponentFromContext(getContext());
            mImageLoader = mAppComponent.imageLoader();
        }

        mImageLoader.loadImage(getContext(),
                ImageConfigImpl
                        .builder()
                        .placeholder(R.mipmap.default_img)
                        .errorPic(R.mipmap.default_img)
                        .url(item.getHeaderUrl())
                        .imageView(commentatorImg)
                        .build());

        commentatorTxt.setText(item.getUserName());
        contentTxt.setText(item.getContent());
        modelTxt.setText(item.getColor() + ";" + item.getSize());
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, @NonNull AppraiseBean item) {
        ImageView appraiseImg = holder.getView(R.id.appraiseImg);

        mImageLoader.loadImage(getContext(),
                ImageConfigImpl
                        .builder()
                        .placeholder(R.mipmap.default_img)
                        .errorPic(R.mipmap.default_img)
                        .url(item.getUrl())
                        .imageView(appraiseImg)
                        .build());
    }
}
