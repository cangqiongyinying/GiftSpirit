package com.jiangxin.giftspirit;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by my on 2016/8/16.
 */
public class LoginActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initBar("登录");
    }
}
