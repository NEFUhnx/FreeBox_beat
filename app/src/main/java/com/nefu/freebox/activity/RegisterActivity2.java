package com.nefu.freebox.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nefu.freebox.R;
import com.nefu.freebox.bean.BaseActivity;
import com.nefu.freebox.entity.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity2 extends BaseActivity {

    private String number;
    private String password;

    private TextInputLayout textInputLayoutPw;
    private TextInputLayout textInputLayoutPw2;
    private Button signup;

    private static final String TAG = "RegisterActivity2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        initView();
        setListener();
    }

    private void initView(){
        number = getIntent().getStringExtra(MOBILE_NUMBER);
        textInputLayoutPw = findViewById(R.id.activity_register2_til_pw);
        textInputLayoutPw2 = findViewById(R.id.activity_register2_til_pw2);
        signup = findViewById(R.id.bt_signup);
    }

    private void setListener(){
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password = textInputLayoutPw.getEditText().getText().toString();
                String password2 = textInputLayoutPw2.getEditText().getText().toString();
                if(password.equals(password2)){
                    //两次密码相同，注册成功
                    //账号密码保存入数据库
                    Log.i(TAG, "onClick: -----------------------------------");
                    User user = new User();
                    user.setMobileNumber(number);
                    user.setName(number);
                    user.setImage(null);
                    user.setAddress("");
                    user.setPassword(password);
                    user.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if(e == null){
                                //保存成功
                                Log.d(TAG, s);
                            }else{
                                //保存失败
                                Log.d(TAG, s);
                                Toast.makeText(RegisterActivity2.this, "failed",
                                        Toast.LENGTH_SHORT);
                            }
                        }
                    });

                    AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity2.this);
                    dialog.setTitle("Sign Up");
                    dialog.setMessage("Sign up successful.");
                    dialog.setCancelable(true);
                    dialog.setPositiveButton("Go Login", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(RegisterActivity2.this, LoginActivity.class);
                            intent.putExtra(MOBILE_NUMBER, number);
                            startActivity(intent);
                        }
                    });
                    dialog.show();
                }else{
                    //两次输入密码不同，提示，不做任何处理
                    AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity2.this);
                    dialog.setTitle("Sign Up");
                    dialog.setMessage("Different passwords for the two input. Please reenter it.");
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
}
