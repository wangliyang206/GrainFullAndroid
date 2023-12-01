package com.zqw.mobile.grainfull.app.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jakewharton.rxbinding3.widget.RxTextView;
import com.zqw.mobile.grainfull.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 包名： com.zqw.mobile.grainfull.app.dialog
 * 对象名： PopupChatGptMore
 * 描述：ChatGPT 更多
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2023/1/12 14:48
 */

public class PopupChatGptMore extends PopupWindow implements View.OnClickListener {
    TextView txviTotal;
    TextView txviUsed;
    TextView txviSurplus;
    EditText editSk;

    // 用来销毁
    private Disposable mDisposable;
    private ItemClick mItemClick;

    // 旧的 sk
    private String oldSk;

    public PopupChatGptMore(Context context, float total, float used, float surplus, String sk, ItemClick mItemClick) {
        super(context);
        this.oldSk = sk;
        this.mItemClick = mItemClick;

        // 初始化下拉列表
        View view = LayoutInflater.from(context).inflate(R.layout.pop_chatgptmore_layout, null);
        setContentView(view);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.MATCH_PARENT);

        // 设置背景色
        setBackgroundDrawable(new ColorDrawable());

        // 让popwin获取焦点
        setFocusable(true);
        // 点击弹出窗口区域之外的任意区域，则该窗口关闭
        setOutsideTouchable(true);

        txviTotal = view.findViewById(R.id.txvi_popchatgptmore_total);
        txviUsed = view.findViewById(R.id.txvi_popchatgptmore_used);
        txviSurplus = view.findViewById(R.id.txvi_popchatgptmore_surplus);
        editSk = view.findViewById(R.id.edit_popchatgptmore_sk);

//        view.findViewById(R.id.lila_popchatgptmore_top).setOnClickListener(this);
        view.findViewById(R.id.btn_popchatgptmore_ok).setOnClickListener(this);
        view.findViewById(R.id.lila_popchatgptmore_content).setOnClickListener(this);

        txviTotal.setText(String.valueOf(total));
        txviUsed.setText(String.valueOf(used));
        txviSurplus.setText(String.valueOf(surplus));
        editSk.setText(sk);

        onTextChanges();
    }

    /**
     * 更新数据
     */
    public void onUpdate(float total, float used, float surplus) {
        txviTotal.setText(String.valueOf(total));
        txviUsed.setText(String.valueOf(used));
        txviSurplus.setText(String.valueOf(surplus));
    }

    /**
     * 更新数据
     */
    public void onUpdate(String sk) {
        oldSk = sk;
        mDisposable.dispose();
        editSk.setText(sk);
        editSk.setSelection(sk.length());

        onTextChanges();
    }

    /**
     * 监听Edit变更
     */
    private void onTextChanges() {
        mDisposable = RxTextView.textChanges(editSk)
                .debounce(800, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(charSequence -> {
                    if (!TextUtils.isEmpty(editSk.getText().toString())) {
                        // 输入框中不可为空
                        if (oldSk.equalsIgnoreCase(editSk.getText().toString())) {
                            // 一致，不做任何操作
                        } else {
                            // 不一致，需要做保存，及请求查询额度
                            if (mItemClick != null)
                                mItemClick.itemClick(editSk.getText().toString());
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.lila_popchatgptmore_content) {
            // 点击布局不关闭
        } else {
            // 关闭
            this.dismiss();
        }
    }

    public interface ItemClick {
        void itemClick(String sk);
    }
}
