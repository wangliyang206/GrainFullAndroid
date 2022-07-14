package com.baidu.idl.face.platform.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.idl.face.platform.ui.R;
import com.baidu.idl.face.platform.utils.DensityUtils;

/**
 * 选择弹窗
 * Created by v_liujialu01 on 2020/4/7.
 */

public class SelectDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private OnSelectDialogClickListener mOnSelectDialogClickListener;
    private int mDialogType;

    private TextView mTextTitle;
    private TextView mTextTips;
    private Button mBtnConfirm;
    private Button mBtnCancel;

    public SelectDialog(Context context) {
        super(context, R.style.DefaultDialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_select, null);
        setContentView(view);
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        int widthPx = DensityUtils.getDisplayWidth(getContext());
        int dp = DensityUtils.px2dip(getContext(), widthPx) - 40;
        lp.width = DensityUtils.dip2px(getContext(), dp);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        mTextTitle = (TextView) view.findViewById(R.id.text_title);
        mTextTips = (TextView) view.findViewById(R.id.text_tips);
        mBtnConfirm = (Button) view.findViewById(R.id.btn_dialog_recollect);
        mBtnConfirm.setOnClickListener(this);
        mBtnCancel = (Button) view.findViewById(R.id.btn_dialog_return);
        mBtnCancel.setOnClickListener(this);
    }

    public void setDialogTitle(int titleId) {
        if (mTextTitle != null) {
            mTextTitle.setText(titleId);
        }
    }

    public void setDialogTips(int tipsId) {
        if (mTextTips != null) {
            mTextTips.setText(tipsId);
        }
    }

    public void setDialogBtnCancel(int cancelId) {
        if (mBtnCancel != null) {
            mBtnCancel.setText(cancelId);
        }
    }

    public void setDialogBtnConfirm(int confirmId) {
        if (mBtnConfirm != null) {
            mBtnConfirm.setText(confirmId);
        }
    }

    public void setDialogType(int dialogType) {
        mDialogType = dialogType;
    }

    public void setDialogListener(OnSelectDialogClickListener listener) {
        mOnSelectDialogClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_dialog_recollect) {
            if (mOnSelectDialogClickListener != null) {
                mOnSelectDialogClickListener.onConfirm(mDialogType);
            }
        } else if (id == R.id.btn_dialog_return) {
            if (mOnSelectDialogClickListener != null) {
                mOnSelectDialogClickListener.onReturn(mDialogType);
            }
        }
    }

    public interface OnSelectDialogClickListener {
        void onConfirm(int dialogType);

        void onReturn(int dialogType);
    }
}
