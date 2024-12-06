package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioGroup;

import com.zqw.mobile.grainfull.R;

/**
 * @ProjectName: BeanSproutAssistantAndroid
 * @Package: com.wly.beansprout.view
 * @ClassName: GridRadioGroup
 * @Description: 多行显示
 * @Author: WLY
 * @CreateDate: 2024/7/11 12:13
 */
public class GridRadioGroup extends RadioGroup {

    //显示的列数
    private int columnNum = 2;//默认2列

    public GridRadioGroup(Context context) {
        this(context, null);
    }

    public GridRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GridRadioGroup, 0, 0);
        try {
            columnNum = ta.getInt(R.styleable.GridRadioGroup_columnNum, 2);
        } finally {
            ta.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int itemWidth = (widthSize - (columnNum + 1)) / columnNum;
        int childCount = getChildCount();
        int itemHeight = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.measure(MeasureSpec.makeMeasureSpec(itemWidth, MeasureSpec.EXACTLY), MeasureSpec.UNSPECIFIED);
            itemHeight = child.getMeasuredHeight();
        }
        int rows = childCount % columnNum == 0 ? childCount / columnNum : childCount / columnNum + 1;
        int heightSize = rows * itemHeight + (rows + 1);
        setMeasuredDimension(widthMeasureSpec, heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int rows = 0;
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            int width = view.getMeasuredWidth();
            int height = view.getMeasuredHeight();

            int yu = i % columnNum;
            int cl, ct, cr, cb;
            if (i >= columnNum - 1 && yu == 0) {
                rows++;
            }

            cl = (yu + 1) + yu * width;
            ct = (rows + 1) + rows * height;
            cr = (yu + 1) + (yu + 1) * width;
            cb = (rows + 1) + (rows + 1) * height;

            view.layout(cl, ct, cr, cb);
        }
    }

    /**
     * 设置列数
     *
     * @param columnNum
     */
    public void setColumnNum(int columnNum) {
        this.columnNum = columnNum;
    }
}
