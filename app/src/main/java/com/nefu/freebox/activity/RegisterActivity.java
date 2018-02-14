package com.nefu.freebox.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.nefu.freebox.bean.BaseActivity;
import com.nefu.freebox.R;
import com.nefu.freebox.entity.User;

import java.util.List;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 22062 on 2018/2/9.
 */

public class RegisterActivity extends BaseActivity {

    private static final String TAG = "RegisterActivity";
    private int registered = 0;

    private TextInputLayout textInputLayoutNo;
    private TextInputLayout textInputLayoutCode;
    private Button bt_verify;
    private Button bt_next;
    private TextView intentLogin;

    private String number;
    private String verifyCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        setListener();
    }

    private void initView(){
        textInputLayoutNo = findViewById(R.id.activity_register_til_no);
        textInputLayoutCode = findViewById(R.id.activity_register_til_code);
        bt_verify = findViewById(R.id.bt_verify);
        bt_next = findViewById(R.id.bt_next);
        intentLogin = findViewById(R.id.link_login);
    }

    private void setListener(){
        bt_verify.setOnClickListener(new View.OnClickListener() {
            //获取验证码
            @Override
            public void onClick(View view) {
                int permissionCheck = ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_PHONE_STATE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
                } else {
                    //TODO
                    checkMobileNumber();
                }
            }
        });

        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setTitle("Next");
                progressDialog.setMessage("Being verified...");
                progressDialog.show();
                verifyCode = textInputLayoutCode.getEditText().getText().toString();
                BmobSMS.verifySmsCode(RegisterActivity.this, number, verifyCode,
                        new VerifySMSCodeListener() {
                            @Override
                            public void done(cn.bmob.sms.exception.BmobException e) {
                                if(e == null){
                                    //验证成功
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(RegisterActivity.this, RegisterActivity2.class);
                                    intent.putExtra(MOBILE_NUMBER, number);
                                    startActivity(intent);
                                }else{
                                    //验证失败
                                    progressDialog.dismiss();
                                    final AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                                    dialog.setTitle("Next");
                                    dialog.setMessage("Verification code input error, please reenter.");
                                    dialog.setCancelable(true);
                                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                        }
                                    });
                                    dialog.show();
                                }
                            }
                        });
            }
        });

        intentLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public static boolean isMobile(String number) {
    /*
    移动：134、135、136、137、138、139、150、151、152、157(TD)、158、159、178(新)、182、184、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、170、173、177、180、181、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String num = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[34578]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }
    }

    private void sendSMSCode(){
        Log.d(TAG, "done: ------------------------");
        BmobSMS.requestSMSCode(RegisterActivity.this, number, "Code", new RequestSMSCodeListener(){
            @Override
            public void done(Integer integer, cn.bmob.sms.exception.BmobException e) {
                if(e == null){
                    //发送成功，按钮不可点击变为灰色
                    Log.i("bmob", "短信id：" + integer);//用于查询本次短信发送详情
                    bt_verify.setClickable(false);
                    bt_verify.setBackgroundColor(Color.GRAY);
                    Toast.makeText(RegisterActivity.this,
                            "Verification code has been sent, valid for 10 minutes",
                            Toast.LENGTH_LONG).show();
                    //倒计时1分钟
                    new CountDownTimer(60000,1000){
                        @Override
                        public void onTick(long l) {
                            bt_verify.setText(l/1000 + "s");
                        }

                        @Override
                        public void onFinish() {
                            bt_verify.setClickable(true);
                            bt_verify.setBackgroundColor(getResources().getColor(R.color.colorButton));
                            bt_verify.setText("resend");
                        }
                    }.start();
                }else{
                    Log.i(TAG, "done: -----------------------");
                    Toast.makeText(RegisterActivity.this,
                            "Send failed. please check the network connection.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkMobileNumber(){
        //获取手机号码并检测
        number = textInputLayoutNo.getEditText().getText().toString();
        if(!isMobile(number)){
            textInputLayoutNo.setError("Please input a correct mobile number.");
        }else{
            textInputLayoutNo.setErrorEnabled(false);
            BmobQuery<User> query = new BmobQuery<User>();
            query.addWhereEqualTo("mobileNumber", number);
            query.findObjects(new FindListener<User>() {
                @Override
                public void done(List<User> list, BmobException e) {
                    if(e == null){
                        User user = list.get(0);
                        Log.i(TAG, "done: ---------------------" + user.getObjectId());
                        AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                        dialog.setTitle("Register");
                        dialog.setMessage("The mobile number has been registered.");
                        dialog.setCancelable(true);
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        dialog.show();
                    }else{
                        sendSMSCode();
                    }
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (!(grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                    finish();
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
        }
    }
}
