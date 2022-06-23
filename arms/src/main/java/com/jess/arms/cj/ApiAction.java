package com.jess.arms.cj;

import io.reactivex.Observable;

/**
 * @Title: ApiAction
 * @Package com.cj.mobile.common.api
 * @Description: Api请求接口
 * @author: 王力杨
 * @date: 16/5/24 上午10:26
 */
public interface ApiAction<T, P> {
    public Observable<GsonResponse<P>> chain(GsonRequest<T> request);
}
