package com.zqw.mobile.grainfull.mvp.ui.adapter;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ConvertUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;

import java.util.List;

/**
 * 包名： com.zqw.mobile.grainfull.mvp.ui.adapter
 * 对象名： EditTurntableAdapter
 * 描述：编辑转盘适配器
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2021/1/20 16:53
 */

public class EditTurntableAdapter extends RecyclerView.Adapter<EditTurntableAdapter.ViewHolder> implements View.OnClickListener {
    private List<String> mList;

    public EditTurntableAdapter(List<String> infos) {
        this.mList = infos;
    }

    /**
     * 获取数据集
     */
    public List<String> getData() {
        return mList;
    }

    /**
     * 数据中是否包含为空的
     */
    public boolean isEmpty() {
        boolean isVale = false;

        for (String val : mList) {
            if (TextUtils.isEmpty(val)) {
                isVale = true;
                break;
            }
        }

        return isVale;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.turntable_item_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String info = mList.get(position);

        // 分割线
        setLayoutMargin(holder, position != 0);

        if (holder.editName.getTag() instanceof TextWatcher) {
            holder.editName.removeTextChangedListener((TextWatcher) holder.editName.getTag());
        }

        holder.editName.setText(info);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null && !TextUtils.isEmpty(editable.toString())) {
                    mList.set(position, editable.toString());
                }
            }
        };

        holder.editName.addTextChangedListener(textWatcher);
        holder.editName.setTag(textWatcher);

        holder.imviDel.setTag(position);
        holder.imviDel.setOnClickListener(this);
    }

    /**
     * 动态设置Margin
     */
    private void setLayoutMargin(ViewHolder holder, boolean isSet) {
        ConstraintLayout.LayoutParams layoutParam = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        if (isSet)
            layoutParam.setMargins(0, ConvertUtils.dp2px(10), 0, 0);
        else
            layoutParam.setMargins(0, 0, 0, 0);

        holder.mLayout.setLayoutParams(layoutParam);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return CommonUtils.isNotEmpty(mList) ? mList.size() : 0;
    }

    @Override
    public void onClick(View view) {
        final int position = (Integer) view.getTag();
        mList.remove(position);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout mLayout;
        private EditText editName;
        private ImageView imviDel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mLayout = itemView.findViewById(R.id.turntable_item_layout);
            editName = itemView.findViewById(R.id.edit_turntableitem_name);
            imviDel = itemView.findViewById(R.id.imvi_turntableitem_del);
        }
    }

}
