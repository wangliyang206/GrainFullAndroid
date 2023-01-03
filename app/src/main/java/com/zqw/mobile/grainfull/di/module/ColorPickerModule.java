package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.ColorPickerContract;
import com.zqw.mobile.grainfull.mvp.model.ColorPickerModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/01/03 16:06
 * ================================================
 */
@Module
//构建ColorPickerModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class ColorPickerModule {

    @Binds
    abstract ColorPickerContract.Model bindColorPickerModel(ColorPickerModel model);
}