package com.jess.arms.cj.colorful.setter;

import android.content.res.Resources.Theme;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import timber.log.Timber;

/**
 * Created by 赤槿 on 2017/9/12.
 */

public class ViewGroupSetter extends ViewSetter {
    /**
     * ListView的子试图的Setter
     */
    protected Set<ViewSetter> mItemViewSetters = new HashSet<ViewSetter>();

    /**
     * @param targetView
     * @param resId
     */
    public ViewGroupSetter(ViewGroup targetView, int resId) {
        super(targetView, resId);
    }

    public ViewGroupSetter(ViewGroup targetView) {
        super(targetView, 0);
    }

    /**
     * 设置View的背景色
     *
     * @param viewId
     * @param colorId
     * @return
     */
    public ViewGroupSetter childViewBgColor(int viewId, int colorId) {
        mItemViewSetters.add(new ViewBackgroundColorSetter(viewId, colorId));
        return this;
    }

    /**
     * 设置View的drawable背景
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public ViewGroupSetter childViewBgDrawable(int viewId, int drawableId) {
        mItemViewSetters.add(new ViewBackgroundDrawableSetter(viewId,
                drawableId));
        return this;
    }

    /**
     * 设置文本颜色,因此View的类型必须为TextView或者其子类
     *
     * @param viewId
     * @param colorId
     * @return
     */
    public ViewGroupSetter childViewTextColor(int viewId, int colorId) {
        mItemViewSetters.add(new TextColorSetter(viewId, colorId));
        return this;
    }

    @Override
    public void setValue(Theme newTheme, int themeId) {
        Timber.e("color123 setValue(ViewGroupSetter.java:86)");
        mView.setBackgroundColor(getColor(newTheme));
        // 清空AbsListView的元素
        clearListViewRecyclerBin(mView);
        // 清空RecyclerView
        clearRecyclerViewRecyclerBin(mView);
        // 修改所有子元素的相关属性
        changeChildenAttrs((ViewGroup) mView, newTheme, themeId);
    }

    /**
     * @param viewId
     * @return
     */
    private View findViewById(View rootView, int viewId) {
        View targetView = rootView.findViewById(viewId);
        Timber.d("### viewgroup find view : " + targetView);
        return targetView;
    }

    /**
     * 修改子视图的对应属性
     *
     * @param viewGroup
     * @param newTheme
     * @param themeId
     */
    private void changeChildenAttrs(ViewGroup viewGroup, Theme newTheme,
                                    int themeId) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = viewGroup.getChildAt(i);
            // 深度遍历
            if (childView instanceof ViewGroup) {
                changeChildenAttrs((ViewGroup) childView, newTheme, themeId);
            }

            // 遍历子元素与要修改的属性,如果相同那么则修改子View的属性
            for (ViewSetter setter : mItemViewSetters) {
                // 每次都要从ViewGroup中查找数据
                setter.mView = findViewById(viewGroup, setter.mViewId);

                Timber.e("### childView : " + childView + ", id = "
                        + childView.getId());
                Timber.e("### setter view : " + setter.mView + ", id = "
                        + setter.getViewId());
                if (childView.getId() == setter.getViewId()) {
                    setter.setValue(newTheme, themeId);
                    Timber.e("@@@ 修改新的属性: " + childView);
                }
            }
        }
    }

    private void clearListViewRecyclerBin(View rootView) {
        if (rootView instanceof AbsListView) {
            try {
                Field localField = AbsListView.class
                        .getDeclaredField("mRecycler");
                localField.setAccessible(true);
                Method localMethod = Class.forName(
                        "android.widget.AbsListView$RecycleBin")
                        .getDeclaredMethod("clear", new Class[0]);
                localMethod.setAccessible(true);
                localMethod.invoke(localField.get(rootView), new Object[0]);
                Timber.e("### 清空AbsListView的RecycerBin ");
            } catch (NoSuchFieldException e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException e2) {
                e2.printStackTrace();
            } catch (NoSuchMethodException e3) {
                e3.printStackTrace();
            } catch (IllegalAccessException e4) {
                e4.printStackTrace();
            } catch (InvocationTargetException e5) {
                e5.printStackTrace();
            }
        }
    }

    protected void clearRecyclerViewRecyclerBin(View rootView) {
        if (rootView instanceof RecyclerView) {
            try {
                Field localField = RecyclerView.class
                        .getDeclaredField("mRecycler");
                localField.setAccessible(true);
                Method localMethod = Class.forName(
                        "android.support.v7.widget.RecyclerView$Recycler")
                        .getDeclaredMethod("clear", new Class[0]);
                localMethod.setAccessible(true);
                localMethod.invoke(localField.get(rootView), new Object[0]);
                Timber.e("### 清空RecyclerView的Recycer ");
                rootView.invalidate();
                ((RecyclerView) rootView).getRecycledViewPool().clear();
            } catch (NoSuchFieldException e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException e2) {
                e2.printStackTrace();
            } catch (NoSuchMethodException e3) {
                e3.printStackTrace();
            } catch (IllegalAccessException e4) {
                e4.printStackTrace();
            } catch (InvocationTargetException e5) {
                e5.printStackTrace();
            }
        }
//        ((RecyclerView) rootView).getRecycledViewPool().clear();
    }
}
