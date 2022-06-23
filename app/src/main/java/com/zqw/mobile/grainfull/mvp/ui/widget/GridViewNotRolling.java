package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 不滚动的GridView
 * @author 王力杨
 *
 */
public class GridViewNotRolling extends GridView{

	public GridViewNotRolling(Context context) {
		super(context);
	}
	
	public GridViewNotRolling(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public GridViewNotRolling(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	  * 设置不滚动
	  */
	 public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	 {
	  int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
	  super.onMeasure(widthMeasureSpec, expandSpec);

	 }
}
