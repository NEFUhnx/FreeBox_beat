package com.nefu.freebox.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nefu.freebox.bean.BaseActivity;
import com.nefu.freebox.R;
import com.nefu.freebox.entity.User;

import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 22062 on 2018/2/9.
 */

public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";
    private User user = null;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private TextInputLayout textInputLayoutNo;
    private TextInputLayout textInputLayoutPw;
    private Button bt_login;
    private TextView signup;
    private TextView forgotPasswrod;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        setListener();
    }

    private void initView() {
        textInputLayoutNo = findViewById(R.id.activity_login_til_no);
        textInputLayoutPw = findViewById(R.id.activity_login_til_pw);
        bt_login = findViewById(R.id.bt_login);
        signup = findViewById(R.id.link_signup);
        forgotPasswrod = findViewById(R.id.link_forgot_password);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        LOGIN_STATUS = pref.getBoolean("LOGIN_STATUS", false);
        if (LOGIN_STATUS){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void setListener(){
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String number = textInputLayoutNo.getEditText().getText().toString();
                final String password = textInputLayoutPw.getEditText().getText().toString();
                String password2 = null;
                if(!isMobile(number)){
                    textInputLayoutNo.setError("Please input a mobile number.");
                    return;
                }else{
                    textInputLayoutNo.setErrorEnabled(false);
                }
                if(password.equals("")){
                    textInputLayoutPw.setError("Please input password.");
                    return;
                }else{
                    textInputLayoutPw.setErrorEnabled(false);
                }
                BmobQuery<User> query = new BmobQuery<User>();
                query.addWhereEqualTo("mobileNumber", number);
                query.findObjects(new FindListener<User>() {
                    @Override
                    public void done(List<User> list, BmobException e) {
                        if(e == null){
                            user = list.get(0);
                            if(user.getPassword().equals(password)){
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                editor = pref.edit();
                                editor.putBoolean("LOGIN_STATUS", true);
                                editor.putString("MOBILE_NUMBER", number);
                                editor.putString("PASSWORD", password);
                                editor.apply();
                                finish();
                            }else{
                                AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
                                dialog.setTitle("Login");
                                dialog.setMessage("Mobile number or password input error. Please reenter.");
                                dialog.setCancelable(true);
                                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                });
                                dialog.show();
                                textInputLayoutPw.getEditText().setText("");
                            }
                            Log.i(TAG, "onClick: --------------------" + list.get(0).getPassword());
                        }else{
                            AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
                            dialog.setTitle("Login");
                            dialog.setMessage("Mobile number input error. Please reenter.");
                            dialog.setCancelable(true);
                            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                            dialog.show();
                            Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        forgotPasswrod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
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
}
