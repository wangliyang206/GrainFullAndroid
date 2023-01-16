package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import com.zqw.mobile.grainfull.R;

/**
 * 带进度条的WebView
 */

public class ProgressWebView extends WebView {
    private ProgressBar mProgressBar;

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mProgressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 8);
        mProgressBar.setLayoutParams(layoutParams);

        Drawable drawable = context.getResources().getDrawable(R.drawable.web_progress_bar_states);
        mProgressBar.setProgressDrawable(drawable);
        addView(mProgressBar);
        setWebChromeClient(new WebChromeClients());
    }

    public class WebChromeClients extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            onProgressChangedX5(view, newProgress);
            super.onProgressChanged(view, newProgress);
        }
    }

    /** 进度条 */
    public void onProgressChangedX5(WebView view, int newProgress) {
        if (newProgress == 100) {
            mProgressBar.setVisibility(GONE);
        } else {
            if (mProgressBar.getVisibility() == GONE)
                mProgressBar.setVisibility(VISIBLE);
            mProgressBar.setProgress(newProgress);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        ViewGroup.LayoutParams lp = mProgressBar.getLayoutParams();
        lp.width = l;
        lp.height = t;
        mProgressBar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }
}