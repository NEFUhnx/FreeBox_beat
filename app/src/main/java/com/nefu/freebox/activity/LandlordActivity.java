package com.nefu.freebox.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.nefu.freebox.R;
import com.nefu.freebox.bean.Flyme;
import com.nefu.freebox.bean.MIUI;
import com.nefu.freebox.entity.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class LandlordActivity extends AppCompatActivity {

    public static final String MOBILE_NUMBER = "mobile_number";

    private CircleImageView imageView;
    private EditText textName;
    private EditText textMobileNumber;
    private EditText textAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landlord);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        if(MIUI.MIUISetStatusBarLightMode(this,true)){
        }
        else Flyme.FlymeSetStatusBarLightMode(this.getWindow(),true);

        initView();
    }

    private void initView(){
        Intent intent = getIntent();
        String mobileNumber = intent.getStringExtra(MOBILE_NUMBER);

        Toolbar toolbar = findViewById(R.id.toolbar_landlord);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        imageView = findViewById(R.id.landlord_image);
        textName = findViewById(R.id.landlord_et_name);
        textMobileNumber = findViewById(R.id.landlord_et_mobile);
        textAddress = findViewById(R.id.landlord_et_address);

        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("mobileNumber", mobileNumber);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null){
                    User user = list.get(0);
                    Glide.with(LandlordActivity.this).load(user.getImage().getUrl()).into(imageView);
                    textName.setText(user.getName());
                    textMobileNumber.setText(user.getMobileNumber());
                    textAddress.setText(user.getAddress());
                    if (actionBar != null) {
                        actionBar.setTitle(user.getName());
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
        }
        return true;
    }
}
