package com.zqw.mobile.grainfull.mvp.ui.holder;

import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.blankj.utilcode.util.ConvertUtils;
import com.jess.arms.base.BaseHolder;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.model.entity.NewHomeInfo;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.holder
 * @ClassName: HomeActionBarHolder
 * @Description: 操作栏
 * @Author: WLY
 * @CreateDate: 2023/7/14 15:49
 */
public class HomeActionBarHolder extends BaseHolder<NewHomeInfo> implements View.OnClickListener {
    @BindView(R.id.home_actionbar_item_layout)
    ConstraintLayout mLayout;

    public HomeActionBarHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(@NotNull NewHomeInfo data, int position) {
        if (position == 0) {
            setLayoutMargin(false);
        } else {
            setLayoutMargin(true);
        }
    }

    /**
     * 动态设置Margin
     */
    private void setLayoutMargin(boolean isSet) {
        ConstraintLayout.LayoutParams layoutParam = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        if (isSet)
            layoutParam.setMargins(0, ConvertUtils.dp2px(10), 0, 0);
        else
            layoutParam.setMargins(0, 0, 0, 0);

        mLayout.setLayoutParams(layoutParam);
    }

    @Override
    protected void onRelease() {
        this.mLayout = null;
    }
}
