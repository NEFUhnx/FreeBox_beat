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

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class ForgotPasswordActivity2 extends BaseActivity {

    private static final String TAG = "ForgotPasswordActivity2";

    private String number;
    private String password;

    private TextInputLayout textInputLayoutPw;
    private TextInputLayout textInputLayoutPw2;
    private Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password2);
        initView();
        setListener();
    }

    private void initView(){
        Intent intent = getIntent();
        number = intent.getStringExtra(MOBILE_NUMBER);
        textInputLayoutPw = findViewById(R.id.activity_forgot_password2_til_pw);
        textInputLayoutPw2 = findViewById(R.id.activity_forgot_password2_til_pw2);
        signup = findViewById(R.id.bt_signup);
    }

    private void setListener(){
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password = textInputLayoutPw.getEditText().getText().toString();
                String password2 = textInputLayoutPw2.getEditText().getText().toString();
                if(password.equals(password2)){
                    //两次密码相同，修改成功
                    //密码修改保存入数据库
                    BmobQuery<User> query = new BmobQuery<User>();
                    query.addWhereEqualTo("mobileNumber", number);
                    query.findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> list, BmobException e) {
                            if(e == null){
                                User user = list.get(0);
                                String objectID = user.getObjectId();
                                user.setValue("password", password);
                                user.update(objectID, new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if(e == null){
                                            AlertDialog.Builder dialog = new AlertDialog.Builder(ForgotPasswordActivity2.this);
                                            dialog.setTitle("Reset Password");
                                            dialog.setMessage("Reset password successful.");
                                            dialog.setCancelable(true);
                                            dialog.setPositiveButton("Go Login", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Intent intent = new Intent(ForgotPasswordActivity2.this, LoginActivity.class);
                                                    startActivity(intent);
                                                }
                                            });
                                            dialog.show();
                                        }
                                    }
                                });
                            }else{
                                Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }
                    });
                }else{
                    //两次输入密码不同，提示，不做任何处理
                    AlertDialog.Builder dialog = new AlertDialog.Builder(ForgotPasswordActivity2.this);
                    dialog.setTitle("Reset password");
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
