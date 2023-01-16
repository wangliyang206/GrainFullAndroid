package com.zqw.mobile.grainfull.app.utils;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ActivityUtils;
import com.zqw.mobile.grainfull.app.global.Constant;
import com.zqw.mobile.grainfull.mvp.ui.activity.NewWindowX5Activity;

/**
 * Android实现部分文字可点击及变色
 * TextView userAgreement = findViewById(R.id.user_agreement);
 * SpannableString agreement = new SpannableString("Agree to the User Agreement and Privacy Policy");
 * agreement.setSpan(new MyClickableSpan("User Agreement"), 13, 27, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
 * agreement.setSpan(new MyClickableSpan("Privacy Policy"), 32, 46, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
 * userAgreement.setText(agreement);
 * userAgreement.setMovementMethod(LinkMovementMethod.getInstance());
 */
public class MyClickableSpan extends ClickableSpan {

    private String text;

    public MyClickableSpan(String text) {
        this.text = text;
    }

    @Override
    public void onClick(@NonNull View view) {
//        Toast.makeText(view.getContext(), text, EToast2.LENGTH_SHORT).show();

        Bundle bundle = new Bundle();
        bundle.putBoolean("isShowTop", true);

        // 《服务协议》
        if (text.equalsIgnoreCase("《服务协议》")) {
            bundle.putString("URL", Constant.serviceAgreementUrl);
            bundle.putString("TITLE", "服务协议");
        }

        // 《隐私政策》
        if (text.equalsIgnoreCase("《隐私政策》")) {
            bundle.putString("URL", Constant.privacyPolicyUrl);
            bundle.putString("TITLE", "隐私政策");
        }
        ActivityUtils.startActivity(bundle, NewWindowX5Activity.class);
    }

    @Override
    public void updateDrawState(@NonNull TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(Color.parseColor("#46C788"));
        ds.setUnderlineText(false);
    }
}