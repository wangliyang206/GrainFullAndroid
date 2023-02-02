package com.zqw.mobile.grainfull.mvp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.mvp.model.entity.RoadOnePen;

import java.util.List;

/**
 * 一笔画完 游戏适配器
 */
public class OnePenAdapter extends BaseAdapter {

    private final int size;
    private final int startPosition;
    private final List<Integer> road;

    public OnePenAdapter(RoadOnePen bean_road) {
        this.road = bean_road.getRoadList();
        if (!CommonUtils.isNotEmpty(road)) {
            this.size = 0;
            this.startPosition = 0;
        } else {
            this.size = bean_road.getRows() * bean_road.getColumns();
            this.startPosition = road.get(0);
        }
    }


    @Override
    public int getCount() {
        return size;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.onepen_item_layout, parent, false).findViewById(R.id.parentyibi);
        }
        if (startPosition == position) {
            convertView.findViewById(R.id.baseyibi).setBackgroundResource(R.drawable.shape_gray_deep_selected);
        }
        boolean isAllowed = false;
        for (int p : road) {
            if (p == position) {
                isAllowed = true;
            }
        }
        if (!isAllowed) {
            convertView.setTag("forbidden");
            convertView.findViewById(R.id.baseyibi).setBackgroundResource(R.color.onepen_transparency);
        }
        return convertView;
    }

}
