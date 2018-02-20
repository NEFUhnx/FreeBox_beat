package com.nefu.freebox.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nefu.freebox.bean.AppBarStateChangeListener;
import com.nefu.freebox.bean.BaseActivity;
import com.nefu.freebox.R;
import com.nefu.freebox.entity.House;
import com.nefu.freebox.entity.Stars;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class HomeActivity extends BaseActivity {

    private static final String TAG = "HomeActivity";

    public static final String ITEM_OBJECT_ID = "item_object_id";
    public static final String ITEM_NUMBER = "item_number";
    public static final String ITEM_TITLE = "item_title";
    public static final String ITEM_IMAGE = "item_image";

    private String itemObjectId;
    private String itemMobileNumber;

    private ImageView imageView;
    private TextView textViewRent;
    private TextView textViewLocation;
    private TextView textViewHouseArea;
    private TextView textViewDescribe;
    private Button bt_landlord;
    private Button bt_message;
    private Button bt_call;
    private FloatingActionButton fab_star;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
        setListener();
    }

    private void initView(){
        Intent intent = getIntent();
        itemObjectId = intent.getStringExtra(ITEM_OBJECT_ID);
        itemMobileNumber = intent.getStringExtra(ITEM_NUMBER);
        String itemTitle = intent.getStringExtra(ITEM_TITLE);
        String itemImage = intent.getStringExtra(ITEM_IMAGE);
        collapsingToolbarLayout = findViewById(R.id.activity_home_collapsing);
        collapsingToolbarLayout.setTitle(itemTitle);
        final Toolbar toolbar = findViewById(R.id.activity_home_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        imageView = findViewById(R.id.activity_home_img);
        textViewRent = findViewById(R.id.home_text_rent);
        textViewLocation = findViewById(R.id.home_text_location);
        textViewHouseArea = findViewById(R.id.home_text_house_area);
        textViewDescribe = findViewById(R.id.home_text_describe);
        bt_landlord = findViewById(R.id.bt_landlord);
        bt_message = findViewById(R.id.bt_message);
        bt_call = findViewById(R.id.bt_call);
        fab_star = findViewById(R.id.fab_star);

        Glide.with(this).load(itemImage).into(imageView);
        BmobQuery<House> query = new BmobQuery<>();
        query.getObject(itemObjectId, new QueryListener<House>() {
            @Override
            public void done(House house, BmobException e) {
                if(e == null){
                    textViewRent.setText(house.getRent());
                    textViewLocation.setText(house.getLocation());
                    textViewHouseArea.setText(house.getHouseArea());
                    textViewDescribe.setText(house.getDescribe());
                }
            }
        });

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        final String mobile = pref.getString("MOBILE_NUMBER", "");
        BmobQuery<Stars> q = new BmobQuery<>();
        q.addWhereEqualTo("mobileNumber", mobile);
        q.addWhereEqualTo("itemObjectId", itemObjectId);
        q.findObjects(new FindListener<Stars>() {
            @Override
            public void done(List<Stars> list, BmobException e) {
                if (e == null){
                    if (list.size() == 0){
                        //查到数据为0，未收藏
                        fab_star.setImageResource(R.mipmap.stars_white);
                    }else{
                        //不为0，收藏过
                        fab_star.setImageResource(R.mipmap.stars_checked_white);
                    }
                    Log.i(TAG, "done: yes"+list.size());
                }else{
                    Log.i(TAG, "done: -----------------------------------------------");
                }
            }
        });
    }

    private void setListener(){
        bt_landlord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, LandlordActivity.class);
                intent.putExtra(LandlordActivity.MOBILE_NUMBER, itemMobileNumber);
                startActivity(intent);
            }
        });

        bt_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        bt_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(HomeActivity.this);
                dialog.setTitle("Call");
                dialog.setMessage("Call " + itemMobileNumber + "?");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (ContextCompat.checkSelfPermission(HomeActivity.this,
                                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(HomeActivity.this,
                                    new String[]{ Manifest.permission.CALL_PHONE}, 1);
                        }else{
                            call();
                        }
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();
            }
        });

        fab_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                star();
            }
        });
    }

    private void call(){
        try{
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:"+itemMobileNumber));
            startActivity(intent);
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    private void star(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        final String mobile = pref.getString("MOBILE_NUMBER", "");
        BmobQuery<Stars> query = new BmobQuery<>();
        query.addWhereEqualTo("mobileNumber", mobile);
        query.addWhereEqualTo("itemObjectId", itemObjectId);
        query.findObjects(new FindListener<Stars>() {
            @Override
            public void done(List<Stars> list, BmobException e) {
                if (e == null){
                    //取消收藏
                    list.get(0).delete(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null){
                                Log.i(TAG, "done: remove star");
                                fab_star.setImageResource(R.mipmap.stars_white);
                            }else{
                                Log.i(TAG, "done: failed");
                            }
                        }
                    });
                }else{
                    //收藏
                    Stars stars = new Stars();
                    stars.setItemObjectId(itemObjectId);
                    stars.setMobileNumber(mobile);
                    stars.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null){
                                Log.i(TAG, "star");
                                fab_star.setImageResource(R.mipmap.stars_checked_white);
                            }else{
                                Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    call();
                }else{
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
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
