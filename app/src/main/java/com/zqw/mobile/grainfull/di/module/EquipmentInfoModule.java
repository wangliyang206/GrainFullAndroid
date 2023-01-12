package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.EquipmentInfoContract;
import com.zqw.mobile.grainfull.mvp.model.EquipmentInfoModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/01/12 09:54
 * ================================================
 */
@Module
//构建EquipmentInfoModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class EquipmentInfoModule {

    @Binds
    abstract EquipmentInfoContract.Model bindEquipmentInfoModel(EquipmentInfoModel model);
}