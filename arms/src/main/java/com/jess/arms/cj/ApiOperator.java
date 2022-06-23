package com.jess.arms.cj;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * @Title: ApiOperator
 * @Package com.chinamall21.mobile.common.api
 * @Description: Api请求转换, 对请求过程进行封装, 简化请求处理过程
 * @author: 王力杨
 * @date: 16/5/24 上午10:01
 */
public class ApiOperator {
    private IRequestMapper mapper;

    public ApiOperator(IRequestMapper mapper) {
        this.mapper = mapper;
    }

    public final <T, P> Observable<P> chain(T t, final ApiAction<T, P> action) {
        //转化成Observable<GsonRequest<T>>
        return Observable.just(t)                                                                   // 生成Observable<T>
                .map(t1 -> mapper.transform(t1))                                                    // 转化成Observable<GsonRequest<T>>
                .flatMap(tGsonRequest -> action.chain(tGsonRequest))                                // 转化成Observable<GsonResponse<P>>
                .flatMap(this.<P>transformResponse());
    }

    /**
     * 服务其返回的数据解析
     * 正常服务器返回数据和服务器可能返回的exception
     *
     * @param <P>
     */
    public static final <P> Function<GsonResponse<P>, Observable<P>> transformResponse() {
        //转化成Observable<P>
        return pGsonResponse -> {
            if (pGsonResponse == null) {
                ErrorInfo errorinfo = new ErrorInfo();
                errorinfo.setErrorcode(ErrorCode.SERVER_ERROR);
                ApiException exp = new ApiException(errorinfo);
                return Observable.error(exp);
            } else {
                P data = pGsonResponse.getData();
                ErrorInfo errorinfo = pGsonResponse.getErrorinfo();
                if (errorinfo != null) {
                    return Observable.error(new ApiException(errorinfo));
                } else {
                    return Observable.just(data);
                }
            }
        };
    }
}
