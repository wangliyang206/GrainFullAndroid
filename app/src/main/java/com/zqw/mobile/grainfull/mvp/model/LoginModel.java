package com.zqw.mobile.grainfull.mvp.model;

import com.jess.arms.cj.ApiOperator;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.zqw.mobile.grainfull.mvp.contract.LoginContract;
import com.zqw.mobile.grainfull.mvp.model.api.AccountService;
import com.zqw.mobile.grainfull.mvp.model.entity.AppUpdate;
import com.zqw.mobile.grainfull.mvp.model.entity.LoginResponse;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 09/16/2019 10:32
 * ================================================
 */
@ActivityScope
public class LoginModel extends BaseModel implements LoginContract.Model {
    @Inject
    ApiOperator apiOperator;                                                                        // 数据转换

    @Inject
    public LoginModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.apiOperator = null;

    }

    /**
     * 登录
     */
    @Override
    public Observable<LoginResponse> btnLogin(String username, String password) {
        Map<String, Object> params = new HashMap<>();
        params.put("userPhone", username);
        params.put("password", password);

        return apiOperator.chain(params, request -> mRepositoryManager.obtainRetrofitService(AccountService.class).toLogin(request));
    }

    @Override
    public Observable<AppUpdate> getVersion(String type) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);

        return apiOperator.chain(params, request -> mRepositoryManager.obtainRetrofitService(AccountService.class).getVersion(request));
    }

}