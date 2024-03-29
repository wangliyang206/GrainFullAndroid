package com.zqw.mobile.grainfull.mvp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment;
import com.blankj.utilcode.util.ConvertUtils;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.model.entity.HomeOrderInfo;
import com.zqw.mobile.grainfull.mvp.ui.adapter.CreditOrderAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.fragment
 * @ClassName: BottomSheetListFragment
 * @Description: 底部弹出列表
 * @Author: WLY
 * @CreateDate: 2024/3/29 11:37
 */
public class BottomSheetListFragment extends SuperBottomSheetFragment {
    // 总布局
    private View view;
    // 适配器
    private CreditOrderAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_bottom_sheet_content, container, false);
        return view;
    }

    @Override
    public float getCornerRadius() {
        return ConvertUtils.dp2px(20);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView mRecyclerView = view.findViewById(R.id.view_bottomsheetcontent_content);
        ArmsUtils.configRecyclerView(mRecyclerView, new LinearLayoutManager(getContext()));
        mAdapter = new CreditOrderAdapter(new ArrayList<>());
        mRecyclerView.setAdapter(mAdapter);

        List<HomeOrderInfo> mList = new ArrayList<>();
        mList.add(new HomeOrderInfo("TN20170801000530", 1, "待接单", "小刀电动车", "秤砣", "长江龙帝", 1100, 1100, "2024-03-29"));
        mList.add(new HomeOrderInfo("TN20220801000001", 3, "待派单", "小李子快修", "小李子", "鑫城实业", 2100, 2100, "2024-03-29"));
        mList.add(new HomeOrderInfo("TN20220801000002", 4, "已派单", "小杨快修", "小王", "风雨科技", 2100, 2100, "2024-03-28"));
        mList.add(new HomeOrderInfo("TN20220801000003", 7, "待确认", "小刀电动车", "张某某", "找铅网科技", 3000, 3000, "2024-03-27"));
        mList.add(new HomeOrderInfo("TN20220801000004", 8, "待结算", "小杨快修", "小王", "易收网", 3100, 3100, "2024-03-26"));
        mList.add(new HomeOrderInfo("TN20220801000005", 9, "待签收", "小李子快修", "小李子", "小米科技", 3200, 3200, "2024-03-25"));
        mList.add(new HomeOrderInfo("TN20220801000006", 10, "已完成", "小刀电动车", "张某某", "华为科技", 4000, 4000, "2024-03-24"));
        mList.add(new HomeOrderInfo("TN20220801000006", 10, "已完成", "小刀电动车", "张某某", "华为科技", 4000, 4000, "2024-03-24"));
        mList.add(new HomeOrderInfo("TN20220801000006", 10, "已完成", "小刀电动车", "张某某", "华为科技", 4000, 4000, "2024-03-24"));
        mList.add(new HomeOrderInfo("TN20220801000006", 10, "已完成", "小刀电动车", "张某某", "华为科技", 4000, 4000, "2024-03-24"));
        mList.add(new HomeOrderInfo("TN20220801000006", 10, "已完成", "小刀电动车", "张某某", "华为科技", 4000, 4000, "2024-03-24"));
        mList.add(new HomeOrderInfo("TN20220801000006", 10, "已完成", "小刀电动车", "张某某", "华为科技", 4000, 4000, "2024-03-24"));
        mList.add(new HomeOrderInfo("TN20220801000006", 10, "已完成", "小刀电动车", "张某某", "华为科技", 4000, 4000, "2024-03-24"));
        mList.add(new HomeOrderInfo("TN20220801000006", 10, "已完成", "小刀电动车", "张某某", "华为科技", 4000, 4000, "2024-03-24"));
        mList.add(new HomeOrderInfo("TN20220801000006", 10, "已完成", "小刀电动车", "张某某", "华为科技", 4000, 4000, "2024-03-24"));
        mList.add(new HomeOrderInfo("TN20220801000006", 10, "已完成", "小刀电动车", "张某某", "华为科技", 4000, 4000, "2024-03-24"));
        mAdapter.setData(mList);
        mAdapter.notifyDataSetChanged();
    }
}
