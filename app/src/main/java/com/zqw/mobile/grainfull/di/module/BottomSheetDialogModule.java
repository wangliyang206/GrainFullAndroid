package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.BottomSheetDialogContract;
import com.zqw.mobile.grainfull.mvp.model.BottomSheetDialogModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/03/29 10:16
 * ================================================
 */
@Module
//构建BottomSheetDialogModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class BottomSheetDialogModule {

    @Binds
    abstract BottomSheetDialogContract.Model bindBottomSheetDialogModel(BottomSheetDialogModel model);
}