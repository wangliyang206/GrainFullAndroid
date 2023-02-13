package com.zqw.mobile.grainfull.di.module;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.AboutContract;
import com.zqw.mobile.grainfull.mvp.model.AboutModel;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 03/28/2020 11:19
 * ================================================
 */
@Module
public abstract class AboutModule {

    @Binds
    abstract AboutContract.Model bindAboutModel(AboutModel model);
}