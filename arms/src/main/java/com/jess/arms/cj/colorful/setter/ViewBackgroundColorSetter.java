package com.jess.arms.cj.colorful.setter;

import android.content.res.Resources.Theme;
import android.view.View;

import timber.log.Timber;

/**
 * Created by 赤槿 on 2017/9/12.
 */

public class ViewBackgroundColorSetter extends ViewSetter {
    public ViewBackgroundColorSetter(View target, int resId) {
        super(target, resId);
    }

    public ViewBackgroundColorSetter(int viewId, int resId) {
        super(viewId, resId);
    }

    @Override
    public void setValue(Theme newTheme, int themeId) {
        if (mView != null) {
            Timber.e("color123 setValue(ViewBackgroundColorSetter.java:29)");
            mView.setBackgroundColor(getColor(newTheme));
        }
    }
}
