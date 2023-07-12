package com.zqw.mobile.grainfull.app.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.zqw.mobile.grainfull.R;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.app.dialog
 * @ClassName: CardFlippingClearanceDialog
 * @Description: 卡牌消消乐游戏通关提示
 * @Author: WLY
 * @CreateDate: 2023/7/12 9:44
 */
public class CardFlippingClearanceDialog extends AlertDialog implements View.OnClickListener {

    // 得分
    private int score;
    // 回调
    private CommTipsDialog.ItemClick popupClick;

    public CardFlippingClearanceDialog(Context context, int score, CommTipsDialog.ItemClick popupClick) {
        super(context, R.style.weicomeDialog);
        this.popupClick = popupClick;
        this.score = score;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_card_flipping_layout);
        getWindow().setGravity(Gravity.CENTER);
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = ViewGroup.LayoutParams.MATCH_PARENT;
        p.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(p);

        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        // 称号
        TextView mAppellation = findViewById(R.id.txvi_popcardflipping_appellation);
        // 内容
        TextView mContent = findViewById(R.id.txvi_popcardflipping_content);
        // 取消
        TextView cancel = findViewById(R.id.txvi_popcardflipping_no);
        // 确定
        TextView ok = findViewById(R.id.txvi_popcardflipping_yes);

        mAppellation.setText(getAppeliation());
        mContent.setText("恭喜您，以" + score + "步数获得胜利！");
        cancel.setOnClickListener(this);
        ok.setOnClickListener(this);
    }

    /**
     * 获取称号
     */
    private String getAppeliation() {
        if (score >= 16 && score <= 18) {
            return "绝顶";
        } else if (score >= 19 && score <= 24) {
            return "超凡";
        } else if (score >= 25 && score <= 28) {
            return "卓然";
        } else if (score >= 29 && score <= 34) {
            return "豪侠";
        } else if (score >= 35 && score <= 48) {
            return "大侠";
        } else if (score >= 49 && score <= 64) {
            return "少侠";
        } else {
            return "新秀";
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txvi_popcardflipping_no:
                if (popupClick != null) {
                    popupClick.itemClick(false);
                }
                break;
            case R.id.txvi_popcardflipping_yes:
                if (popupClick != null) {
                    popupClick.itemClick(true);
                }
                break;
        }
        // 关闭当前弹出框
        this.dismiss();
    }
}
