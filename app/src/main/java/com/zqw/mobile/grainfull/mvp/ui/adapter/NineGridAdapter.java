package com.zqw.mobile.grainfull.mvp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.model.entity.MenuBean;

import java.util.List;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.adapter
 * @ClassName: NineGridAdapter
 * @Description:
 * @Author: WLY
 * @CreateDate: 2023/8/29 18:03
 */
public class NineGridAdapter extends BaseAdapter {
    private final Context context;
    private List<MenuBean> data;
    private int index;
    private int pageSize;
    private OnGridItemClickListener mOnGridItemClickListener;
    /**
     * 用于加载图片的管理类, 默认使用 Glide, 使用策略模式, 可替换框架
     */
    private final ImageLoader mImageLoader;

    public NineGridAdapter(Context context, List<MenuBean> data, int index, int pageSize) {
        this.context = context;
        this.data = data;
        this.index = index;
        this.pageSize = pageSize;

        //可以在任何可以拿到 Context 的地方, 拿到 AppComponent, 从而得到用 Dagger 管理的单例对象
        AppComponent mAppComponent = ArmsUtils.obtainAppComponentFromContext(context);
        mImageLoader = mAppComponent.imageLoader();
    }

    @Override
    public int getCount() {
        return this.data.size() > (this.index + 1) * this.pageSize ? this.pageSize : this.data.size() - this.index * this.pageSize;
    }

    @Override
    public MenuBean getItem(int position) {
        return this.data.get(position + this.index * this.pageSize);
    }

    @Override
    public long getItemId(int position) {
        return position + this.index * this.pageSize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.nine_memu_item, parent, false);
        }
        NineGridAdapter.ViewHolder holder = new NineGridAdapter.ViewHolder(convertView);
        convertView.setTag(holder);
        MenuBean menuBean = getItem(position);

        mImageLoader.loadImage(context, ImageConfigImpl.builder().url(menuBean.getMenuIcon())
                .imageView(holder.menuIcon).build());
        holder.menuName.setText(menuBean.getMenuName());

        holder.nineMenuLayout.setTag(menuBean);
        holder.nineMenuLayout.setOnClickListener(v -> {
            if (mOnGridItemClickListener != null) {
                mOnGridItemClickListener.onItemClick((MenuBean) v.getTag());
            }
        });

        return convertView;
    }

    public List<MenuBean> getData() {
        return this.data;
    }

    public void setData(List<MenuBean> var1) {
        this.data = var1;
    }

    public interface OnGridItemClickListener {
        void onItemClick(MenuBean item);
    }

    public void setOnGridItemClickListener(OnGridItemClickListener onGridItemClickListener) {
        this.mOnGridItemClickListener = onGridItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout nineMenuLayout;
        private ImageView menuIcon;
        private TextView menuName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nineMenuLayout = itemView.findViewById(R.id.nineMenuLayout);
            menuIcon = itemView.findViewById(R.id.menuIcon);
            menuName = itemView.findViewById(R.id.menuName);
        }
    }
}
