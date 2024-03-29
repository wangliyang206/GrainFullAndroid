package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.minibugdev.sheetselection.SheetSelection;
import com.minibugdev.sheetselection.SheetSelectionItem;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerBottomSheetDialogComponent;
import com.zqw.mobile.grainfull.mvp.contract.BottomSheetDialogContract;
import com.zqw.mobile.grainfull.mvp.presenter.BottomSheetDialogPresenter;
import com.zqw.mobile.grainfull.mvp.ui.fragment.BottomSheetListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:底部弹出框
 * <p>
 * Created on 2024/03/29 10:16
 *
 * @author 赤槿
 * module name is BottomSheetDialogActivity
 */
public class BottomSheetDialogActivity extends BaseActivity<BottomSheetDialogPresenter> implements BottomSheetDialogContract.View {

    /*------------------------------------------控件信息------------------------------------------*/
    @BindView(R.id.txvi_bottomsheetdialog_one)
    TextView txviOne;

    /*------------------------------------------业务信息------------------------------------------*/
    private int onePosition = 2;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerBottomSheetDialogComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_bottom_sheet_dialog;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("底部弹出框");

    }

    @OnClick({
            R.id.txvi_bottomsheetdialog_one,                                                        // 效果一
            R.id.txvi_bottomsheetdialog_two,                                                        // 效果二
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txvi_bottomsheetdialog_one:                                                   // 效果一
                showOne();
                break;
            case R.id.txvi_bottomsheetdialog_two:                                                   // 效果二
                showTwo();
                break;
        }
    }

    /**
     * 效果一：下拉关闭、带搜索
     */
    private void showOne() {
        List<SheetSelectionItem> items = new ArrayList<>();
        items.add(new SheetSelectionItem("1", "Item #1", R.mipmap.icon_home_99));
        items.add(new SheetSelectionItem("2", "Item #2", R.mipmap.icon_home_elm));
        items.add(new SheetSelectionItem("3", "Item #3", R.mipmap.icon_home_hfcz));
        items.add(new SheetSelectionItem("4", "Item #4", R.mipmap.icon_home_kfc));
        items.add(new SheetSelectionItem("5", "Item #5", R.mipmap.icon_home_low_price));
        items.add(new SheetSelectionItem("6", "Item #6", R.mipmap.icon_home_today_jingdong));

        SheetSelection mSheetSelection = new SheetSelection.Builder(this)
                .title("请选择")
                .items(items)
                .selectedPosition(onePosition)
                .showDraggedIndicator(true)
                .searchEnabled(true)
                .searchNotFoundText("未搜索到结果!!")
                .onItemClickListener((mItem, integer) -> {
                    txviOne.setText("选择结果：" + mItem.getValue());
                    onePosition = integer;
                    return null;
                })
                .build();

        mSheetSelection.show(getSupportFragmentManager(), "one");
    }

    /**
     * 效果二：下拉关闭、上拉展开
     */
    private void showTwo() {
        BottomSheetListFragment fragment = new BottomSheetListFragment();
        fragment.show(getSupportFragmentManager(), "two");
    }

    public Activity getActivity() {
        return this;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }
}