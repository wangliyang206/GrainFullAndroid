package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.utils.ArmsUtils;
import com.lcw.library.imagepicker.ImagePicker;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.global.Constant;
import com.zqw.mobile.grainfull.app.utils.GlideLoader;
import com.zqw.mobile.grainfull.di.component.DaggerPictureMosaicComponent;
import com.zqw.mobile.grainfull.mvp.contract.PictureMosaicContract;
import com.zqw.mobile.grainfull.mvp.presenter.PictureMosaicPresenter;
import com.zqw.mobile.grainfull.mvp.ui.adapter.PictureMosaicAdapter;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Description: 图片拼接
 * <p>
 * Created on 2023/02/09 15:00
 *
 * @author 赤槿
 * module name is PictureMosaicActivity
 */
@RuntimePermissions
public class PictureMosaicActivity extends BaseActivity<PictureMosaicPresenter> implements PictureMosaicContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_picture_mosaic)
    LinearLayout contentLayout;                                                                     // 主布局

    @BindView(R.id.radio_picturemosaic_group)
    RadioGroup radioGroup;
    @BindView(R.id.radio_picturemosaic_vertical)
    RadioButton radioVertical;
    @BindView(R.id.radio_picturemosaic_horizontal)
    RadioButton radioHorizontal;
    @BindView(R.id.radio_picturemosaic_two)
    RadioButton radioTwo;
    @BindView(R.id.radio_picturemosaic_three)
    RadioButton radioThree;

    @BindView(R.id.view_picturemosaic_vertical)
    NestedScrollView mNestedScrollView;
    @BindView(R.id.revi_picturemosaic_content)
    RecyclerView mRecyclerView;

    @BindView(R.id.view_picturemosaic_horizontal)
    RelativeLayout mHorizontalScrollView;
    @BindView(R.id.lila_picturemosaic_horizontal)
    LinearLayout mHorizontal;

    /*------------------------------------------------业务区域------------------------------------------------*/
    // 最大张图
    private int mMaxCount = 9;

    // 加载图片对象
    @Inject
    ImageLoader mImageLoader;


    // 适配器
    @Inject
    PictureMosaicAdapter mAdapter;
    // 选中的图片
    @Inject
    List<String> mList;

    @Override
    protected void onDestroy() {
        // super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        DefaultAdapter.releaseAllHolder(mRecyclerView);
        super.onDestroy();

        this.mAdapter = null;
        this.mImageLoader = null;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerPictureMosaicComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_picture_mosaic;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("图片拼接");

        // 友盟统计 - 自定义事件
//        MobclickAgent.onEvent(getApplicationContext(), "picture_mosaic");

        // 初始控件
        ArmsUtils.configRecyclerView(mRecyclerView, new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        // 排列监听
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            // 根据选项控制视图展现方式
            if (group.getCheckedRadioButtonId() == R.id.radio_picturemosaic_vertical) {
                // 纵图
                ArmsUtils.configRecyclerView(mRecyclerView, new LinearLayoutManager(PictureMosaicActivity.this));
                mAdapter.setType(1);
                mNestedScrollView.setVisibility(View.VISIBLE);
                mHorizontalScrollView.setVisibility(View.GONE);
            } else if (group.getCheckedRadioButtonId() == R.id.radio_picturemosaic_horizontal) {
                // 横图
                ArmsUtils.configRecyclerView(mRecyclerView, new LinearLayoutManager(PictureMosaicActivity.this, LinearLayoutManager.HORIZONTAL, false));
                mAdapter.setType(2);
                mNestedScrollView.setVisibility(View.GONE);
                mHorizontalScrollView.setVisibility(View.VISIBLE);
            } else if (group.getCheckedRadioButtonId() == R.id.radio_picturemosaic_two) {
                // 2列纵向
                ArmsUtils.configRecyclerView(mRecyclerView, new GridLayoutManager(PictureMosaicActivity.this, 2));
                mAdapter.setType(3);
                mNestedScrollView.setVisibility(View.VISIBLE);
                mHorizontalScrollView.setVisibility(View.GONE);
            } else if (group.getCheckedRadioButtonId() == R.id.radio_picturemosaic_three) {
                // 3列纵向
                ArmsUtils.configRecyclerView(mRecyclerView, new GridLayoutManager(PictureMosaicActivity.this, 3));
                mAdapter.setType(4);
                mNestedScrollView.setVisibility(View.VISIBLE);
                mHorizontalScrollView.setVisibility(View.GONE);
            }

            mAdapter.notifyDataSetChanged();
        });
    }

    @OnClick({
            R.id.imvi_picturemosaic_one,
            R.id.imvi_picturemosaic_two,
            R.id.imvi_picturemosaic_three,
            R.id.imvi_picturemosaic_four,
            R.id.view_picturemosaic_open,                                                           // 选择图片
            R.id.btn_picturemosaic_vertical_create,                                                 // 生成图片 - 纵图
            R.id.btn_picturemosaic_horizontal_create,                                               // 生成图片 - 横图
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvi_picturemosaic_one:
                radioVertical.setChecked(true);
                break;
            case R.id.imvi_picturemosaic_two:
                radioHorizontal.setChecked(true);
                break;
            case R.id.imvi_picturemosaic_three:
                radioTwo.setChecked(true);
                break;
            case R.id.imvi_picturemosaic_four:
                radioThree.setChecked(true);
                break;
            case R.id.view_picturemosaic_open:                                                      // 选择图片
                PictureMosaicActivityPermissionsDispatcher.addAvatarWithPermissionCheck(this);
                break;
            case R.id.btn_picturemosaic_vertical_create:                                            // 生成图片 - 纵图
                onSaveImage(mRecyclerView);
                break;
            case R.id.btn_picturemosaic_horizontal_create:                                          // 生成图片 - 横图
                onSaveImage(mHorizontal);
                break;
        }
    }

    /**
     * 保存图片
     */
    private void onSaveImage(View view) {
        // 将View转换成BitMap格式。缺点：有时会导致View的内容偏移。
//                Bitmap mBitmap = ImageUtils.view2Bitmap(mRecyclerView);
        // 将View转换成BitMap格式。除图片增大，暂时未发现缺点。
        Bitmap mBitmap = getBitmapByScorw(view);
        // 生成文件路径及名称
        String path = Constant.IMAGE_PATH + TimeUtils.getNowString(new SimpleDateFormat("yyyyMMdd_HHmmss")) + ".png";

        new Thread(() -> {
            // 保存图片
            ImageUtils.save(mBitmap, path, Bitmap.CompressFormat.PNG);

            runOnUiThread(() -> {
                // 弹出成功提示
                showMessage("图片保存成功！路径：" + path);
            });
        }).start();
    }

    /**
     * 截取View的屏幕
     *
     * @return Bitmap
     */
    public Bitmap getBitmapByScorw(View viewLayout) {
        Bitmap bmp = Bitmap.createBitmap(viewLayout.getWidth(), viewLayout.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        canvas.drawColor(Color.WHITE);
        viewLayout.draw(canvas);
        return bmp;
    }

    /**
     * 添加照片
     */
    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void addAvatar() {

        ImagePicker.getInstance()
                .setTitle("图片")//设置标题
                .showCamera(true)//设置是否显示拍照按钮
                .showImage(true)//设置是否展示图片
                .showVideo(false)//设置是否展示视频
                .filterGif(true)//设置是否过滤gif图片
                .setMaxCount(mMaxCount)//设置最大选择图片数目(默认为1，单选)
                .setSingleType(true)//设置图片视频不能同时选择
                .setImageLoader(new GlideLoader(this))//设置自定义图片加载器
                .start(this, Constant.REQUEST_SELECT_IMAGES_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.REQUEST_SELECT_IMAGES_CODE) {
                // 清除缓存
                mList.clear();
                // 给纵图加载数据
                mList.addAll(data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES));

                // 给横图加载数据
                mHorizontal.removeAllViews();

                for (int i = 0; i < mList.size(); i++) {
                    String val = mList.get(i);
                    View view = LayoutInflater.from(this).inflate(R.layout.picturemosaic_item_layout, null);
                    ImageView mImageView = view.findViewById(R.id.imvi_picturemosaicitemlayout_pic);
                    // 显示图片
                    mImageView.setImageBitmap(BitmapFactory.decodeFile(val));

                    LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(ArmsUtils.dip2px(this, 160), ArmsUtils.dip2px(this, 160));
                    if (i == 0) {
                        mParams.setMargins(0, 0, 0, 0);
                    } else {
                        mParams.setMargins(ConvertUtils.dp2px(5), 0, 0, 0);
                    }
                    view.setLayoutParams(mParams);
                    mHorizontal.addView(view);
                }

                if (mAdapter.getType() == 2) {
                    // 横图
                    mNestedScrollView.setVisibility(View.GONE);
                    mHorizontalScrollView.setVisibility(View.VISIBLE);
                } else {
                    // 纵图
                    mNestedScrollView.setVisibility(View.VISIBLE);
                    mHorizontalScrollView.setVisibility(View.GONE);
                }

                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PictureMosaicActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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