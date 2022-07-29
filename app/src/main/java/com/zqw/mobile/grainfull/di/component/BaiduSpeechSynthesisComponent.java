package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.BaiduSpeechSynthesisModule;
import com.zqw.mobile.grainfull.mvp.contract.BaiduSpeechSynthesisContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.BaiduSpeechSynthesisActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/07/26 18:34
 * ================================================
 */
@ActivityScope
@Component(modules = BaiduSpeechSynthesisModule.class, dependencies = AppComponent.class)
public interface BaiduSpeechSynthesisComponent {

    void inject(BaiduSpeechSynthesisActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        BaiduSpeechSynthesisComponent.Builder view(BaiduSpeechSynthesisContract.View view);

        BaiduSpeechSynthesisComponent.Builder appComponent(AppComponent appComponent);

        BaiduSpeechSynthesisComponent build();
    }
}