package com.zqw.mobile.grainfull.app.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.ui.adapter.SelectListAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 包名： com.zqw.mobile.operation.app.dialog;
 * 对象名： PopupSelectList
 * 描述：列表
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2017/5/6 14:48
 */

public class PopupSelectList extends PopupWindow implements DefaultAdapter.OnRecyclerViewItemClickListener, View.OnClickListener {

    // 点击回调
    private ItemClick itemClick;
    // 适配器的当前item(选择品类时使用)
    private int mAdapterPosition;

    private RecyclerView recyclerView;
    private SelectListAdapter recycleAdapter;

    public PopupSelectList(Context context, String title, List<String> mList, ItemClick itemClick) {
        super(context);
        this.itemClick = itemClick;

        //初始化下拉列表
        View view = LayoutInflater.from(context).inflate(R.layout.pop_selectlist_layout, null);
        setContentView(view);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);//LinearLayout.LayoutParams.MATCH_PARENT
        setHeight(LinearLayout.LayoutParams.MATCH_PARENT);

        // 设置背景色
        setBackgroundDrawable(new ColorDrawable());

        // 让popwin获取焦点
        setFocusable(true);
        // 点击弹出窗口区域之外的任意区域，则该窗口关闭
        setOutsideTouchable(true);

        // 关闭按钮
        view.findViewById(R.id.imvi_popselectfinanciallayout_close).setOnClickListener(this);
        view.findViewById(R.id.lila_popselectfinanciallayout_close).setOnClickListener(this);
        // 设置标题
        TextView txviTitle = view.findViewById(R.id.txvi_popselectfinanciallayout_title);
        txviTitle.setText(title);

        recyclerView = view.findViewById(R.id.revi_popselectfinanciallayout_content);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        ArmsUtils.configRecyclerView(recyclerView, mLayoutManager);
        setData(mList);
    }

    /**
     * 设置数据
     */
    public void setData(List<String> mList) {
        if (recycleAdapter == null) {
            // 设置Adapter
            recycleAdapter = new SelectListAdapter(mList);
            recyclerView.setAdapter(recycleAdapter);
            recycleAdapter.setOnItemClickListener(this);
        } else {
            recycleAdapter.setData(mList);
            recycleAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imvi_popselectfinanciallayout_close || v.getId() == R.id.lila_popselectfinanciallayout_close) {
            dismiss();
        }
    }

    @Override
    public void onItemClick(@NotNull View view, int viewType, @NotNull Object data, int position) {
        String info = (String) data;
        if (itemClick != null) {
            itemClick.itemClick(position, info);
        }
        this.dismiss();
    }

    public interface ItemClick {
        void itemClick(int position, String info);
    }

    public int getAdapterPosition() {
        return mAdapterPosition;
    }

    public void setAdapterPosition(int mAdapterPosition) {
        this.mAdapterPosition = mAdapterPosition;
    }
}
