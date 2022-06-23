package com.zqw.mobile.grainfull.app.global;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.jess.arms.cj.ClientInfo;
import com.jess.arms.cj.GsonRequest;
import com.jess.arms.cj.IRequestMapper;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.DeviceUtils;

/**
 * 包名： com.cj.mobile.recycling.api
 * 对象名： RequestMapper
 * 描述：请求映射
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2017/3/24 14:36
 */
public class RequestMapper implements IRequestMapper {

    private Context mContext;
    private AccountManager accountManager;

    public RequestMapper(Context mContext, AccountManager accountManager) {
        this.mContext = mContext;
        this.accountManager = accountManager;
    }

    @Override
    public <T> GsonRequest<T> transform(T t) {
        GsonRequest<T> request = new GsonRequest<>();
        String token = accountManager.getToken();
        request.setUserId(accountManager.getUserid());
        request.setData(t);
        request.setToken(token);
        request.setVersion(Constant.version);

        request.setClient(getPhoneInfo(mContext));
        return request;
    }

    /**
     * 获取设备信息
     *
     * @param context 句柄
     * @return 返回 设备信息
     */
    private static ClientInfo getPhoneInfo(Context context) {
        ClientInfo loginBeanIn = new ClientInfo();
        loginBeanIn.setCell(CommonUtils.getPhoneNumber(context));
        loginBeanIn.setDeviceid(DeviceUtils.getIMEI(context));
        loginBeanIn.setSimid(CommonUtils.getSimSerialNumber(context));
        loginBeanIn.setOs("android");
        loginBeanIn.setOsver(android.os.Build.VERSION.SDK_INT + "");
        loginBeanIn.setPpiheight(String.valueOf(ArmsUtils.getScreenHeidth(context)));
        loginBeanIn.setPpiwidth(String.valueOf(ArmsUtils.getScreenWidth(context)));

        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            loginBeanIn.setVercode(pi.versionCode + "");
            loginBeanIn.setVername(pi.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return loginBeanIn;
    }
}
