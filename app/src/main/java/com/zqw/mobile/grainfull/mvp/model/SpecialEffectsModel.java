package com.zqw.mobile.grainfull.mvp.model;

import com.jess.arms.cj.ApiOperator;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.zqw.mobile.grainfull.mvp.contract.SpecialEffectsContract;

import javax.inject.Inject;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/31/2019 10:34
 * ================================================
 */
@FragmentScope
public class SpecialEffectsModel extends BaseModel implements SpecialEffectsContract.Model {
    @Inject
    ApiOperator apiOperator;                                                                        // 数据转换

    @Inject
    public SpecialEffectsModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.apiOperator = null;

    }

}