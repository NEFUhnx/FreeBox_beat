package com.nefu.freebox.bean;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

import cn.bmob.sms.BmobSMS;
import cn.bmob.v3.Bmob;

/**
 * Created by 22062 on 2018/2/2.
 */

public class BaseActivity extends AppCompatActivity {

    public static final String MOBILE_NUMBER = "mobile_number";

    public static boolean LOGIN_STATUS = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        if(MIUI.MIUISetStatusBarLightMode(this,true)){

        }
        else Flyme.FlymeSetStatusBarLightMode(this.getWindow(),true);

        Bmob.initialize(this, "16459bfa83c3768e54925e69a3947512");
        BmobSMS.initialize(this, "16459bfa83c3768e54925e69a3947512");
    }

}
