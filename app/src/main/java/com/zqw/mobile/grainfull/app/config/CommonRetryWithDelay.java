package com.zqw.mobile.grainfull.app.config;

import com.jess.arms.cj.ApiException;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import timber.log.Timber;

/**
 * 包名： com.cj.mobile.aptitude.app.config
 * 对象名： CommonRetryWithDelay
 * 描述：遇到错误时重试和延迟,第一个参数为重试几次,第二个参数为重试的间隔
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2018/6/22 17:02
 */

public class CommonRetryWithDelay implements
        Function<Observable<Throwable>, ObservableSource<?>> {

    public final String TAG = this.getClass().getSimpleName();
    private final int maxRetries;
    private final int retryDelaySecond;
    private int retryCount;

    public CommonRetryWithDelay(int maxRetries, int retryDelaySecond) {
        this.maxRetries = maxRetries;
        this.retryDelaySecond = retryDelaySecond;
    }

    @Override
    public ObservableSource<?> apply(@NonNull Observable<Throwable> throwableObservable) {
        return throwableObservable
                .flatMap((Function<Throwable, ObservableSource<?>>) throwable -> {
                    if (throwable instanceof ApiException) {
                    } else {
                        if (++retryCount <= maxRetries) {
                            // When this Observable calls onNext, the original Observable will be retried (i.e. re-subscribed).
                            Timber.d(TAG + " Observable get error, it will try after " + retryDelaySecond + " second, retry count " + retryCount);
                            return Observable.timer(retryDelaySecond, TimeUnit.SECONDS);
                        }
                    }
                    // Max retries hit. Just pass the error along.
                    return Observable.error(throwable);
                });
    }
}