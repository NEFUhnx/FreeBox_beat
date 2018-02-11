package com.nefu.freebox.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
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

    private TextInputLayout textInputLayoutNo;
    private TextInputLayout textInputLayoutPw;
    private Button bt_login;
    private TextView signup;

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
    }

    private void setListener(){
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String number = textInputLayoutNo.getEditText().getText().toString();
                final String password = textInputLayoutPw.getEditText().getText().toString();
                String password2 = null;
                if(number.equals("")){
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
                                intent.putExtra(MOBILE_NUMBER, number);
                                startActivity(intent);
                                LOGIN_STATUS = LOGGED;
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
                                textInputLayoutNo.getEditText().setText("");
                            }
                            Log.i(TAG, "onClick: --------------------" + list.get(0).getPassword());
                        }else{
                            Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });
                //Log.i(TAG, "onClick: --------------------" + user.getPassword());

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
