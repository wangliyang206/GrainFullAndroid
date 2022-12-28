package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.di.component.DaggerEditTurntableComponent;
import com.zqw.mobile.grainfull.mvp.contract.EditTurntableContract;
import com.zqw.mobile.grainfull.mvp.presenter.EditTurntablePresenter;
import com.zqw.mobile.grainfull.mvp.ui.adapter.EditTurntableAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:编辑转盘
 * <p>
 * Created on 2022/12/28 16:08
 *
 * @author 赤槿
 * module name is EditTurntableActivity
 */
public class EditTurntableActivity extends BaseActivity<EditTurntablePresenter> implements EditTurntableContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_edit_turntable)
    LinearLayout contentLayout;                                                                     // 主布局

    @BindView(R.id.edit_editturntable_title)
    EditText editTitle;                                                                             // 标题

    @BindView(R.id.revi_editturntable_content)
    RecyclerView mRecyclerView;                                                                     // 列表

    /*------------------------------------------------业务区域------------------------------------------------*/
    @Inject
    RecyclerView.LayoutManager mLayoutManager;

    @Inject
    List<String> mList;

    @Inject
    EditTurntableAdapter mAdapter;

    @Override
    protected void onDestroy() {
        // super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        DefaultAdapter.releaseAllHolder(mRecyclerView);
        KeyboardUtils.unregisterSoftInputChangedListener(getWindow());
        super.onDestroy();
        this.mLayoutManager = null;
        this.mAdapter = null;

    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerEditTurntableComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_edit_turntable;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("编辑转盘");

        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @OnClick({
            R.id.imvi_editturntable_add,                                                            // 添加
            R.id.imvi_editturntable_ok,                                                             // 确定
    })
    @Override
    public void onClick(View v) {
        hideInput();
        switch (v.getId()) {
            case R.id.imvi_editturntable_add:                                                       // 添加
                mList.add("");
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.imvi_editturntable_ok:                                                        // 确定
                onSubmit();
                break;
        }
    }

    /**
     * 确定
     */
    private void onSubmit() {
        String title = editTitle.getText().toString();
        if (TextUtils.isEmpty(title)) {
            showMessage("请输入标题！");
            return;
        }

        if (!CommonUtils.isNotEmpty(mAdapter.getData())) {
            showMessage("请添加内容！");
            return;
        }

        if (mAdapter.isEmpty()) {
            showMessage("选项中内容不能为空！");
            return;
        }

        if (mAdapter.getData().size() < 2) {
            showMessage("选项总数不能小于两个！");
            return;
        }

        Intent intent = new Intent();
        intent.putExtra("title", title);
        intent.putStringArrayListExtra("Turntable", (ArrayList<String>) mAdapter.getData());
        setResult(RESULT_OK, intent);
        // 关闭当前页
        killMyself();
    }

    /**
     * 隐藏软键盘
     */
    private void hideInput() {
        KeyboardUtils.hideSoftInput(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        hideInput();
        return super.onTouchEvent(event);
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