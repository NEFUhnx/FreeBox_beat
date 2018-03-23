package com.nefu.freebox.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nefu.freebox.R;
import com.nefu.freebox.bean.BaseActivity;
import com.nefu.freebox.entity.House;

import java.io.File;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class MyHouseActivity extends BaseActivity {

    public static final String ITEM_OBJECT_ID = "item_object_id";
    public static final String ITEM_TITLE = "item_title";
    public static final String ITEM_IMAGE = "item_image";

    private String itemObjectId;

    private ImageView imageView;
    private TextInputEditText textTitle;
    private TextInputEditText textLocation;
    private TextInputEditText textRent;
    private TextInputEditText textHouseArea;
    private TextInputEditText textMobileNumber;
    private TextInputEditText textDescribe;
    private Button bt_MyHouse;

    private SharedPreferences pref;
    private String mobile;
    private String itemImage;
    private String imgPath = null;

    private House house = new House();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_house);
        initView();
        setListener();
    }

    private void initView(){
        Intent intent = getIntent();
        itemObjectId = intent.getStringExtra(ITEM_OBJECT_ID);
        itemImage = intent.getStringExtra(ITEM_IMAGE);
        String itemTitle = intent.getStringExtra(ITEM_TITLE);

        Toolbar toolbar = findViewById(R.id.activity_my_house_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle(itemTitle);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        imageView = findViewById(R.id.my_house_image);
        textTitle = findViewById(R.id.my_house_et_title);
        textLocation = findViewById(R.id.my_house_et_location);
        textRent = findViewById(R.id.my_house_et_rent);
        textHouseArea = findViewById(R.id.my_house_et_house_area);
        textMobileNumber = findViewById(R.id.my_house_et_mobile);
        textDescribe = findViewById(R.id.my_house_et_describe);
        bt_MyHouse = findViewById(R.id.bt_my_house);

        Glide.with(MyHouseActivity.this).load(itemImage).into(imageView);
        textTitle.setText(itemTitle);
        BmobQuery<House> query = new BmobQuery<>();
        query.getObject(itemObjectId, new QueryListener<House>() {
            @Override
            public void done(House house, BmobException e) {
                if (e == null){
                    textLocation.setText(house.getLocation());
                    textRent.setText(house.getRent());
                    textHouseArea.setText(house.getHouseArea());
                    textMobileNumber.setText(house.getMobileNumber());
                    textDescribe.setText(house.getDescribe());
                }
            }
        });
        textMobileNumber.setEnabled(false);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        mobile = pref.getString("MOBILE_NUMBER", "");
        textMobileNumber.setText(mobile);
        textMobileNumber.setEnabled(false);
    }

    private void setListener(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permissionCheck = ContextCompat.checkSelfPermission(MyHouseActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MyHouseActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    //TODO
                    chooseImage();
                }
            }
        });

        bt_MyHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textTitle.getText().toString().equals("") || (textTitle.getText() == null)){
                    Toast.makeText(MyHouseActivity.this, "请输入标题", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (textLocation.getText().toString().equals("") || (textLocation.getText() == null)){
                    Toast.makeText(MyHouseActivity.this, "请输入地址", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (textRent.getText().toString().equals("") || (textRent.getText() == null)){
                    Toast.makeText(MyHouseActivity.this, "请输入租金", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (textHouseArea.getText().toString().equals("") || (textHouseArea.getText() == null)){
                    Toast.makeText(MyHouseActivity.this, "请输入房屋面积", Toast.LENGTH_SHORT).show();
                    return;
                }
                house.setTitle(textTitle.getText().toString());
                house.setLocation(textLocation.getText().toString());
                house.setRent(textRent.getText().toString());
                house.setHouseArea(textHouseArea.getText().toString());
                house.setMobileNumber(textMobileNumber.getText().toString());
                house.setDescribe(textDescribe.getText().toString());
                AlertDialog.Builder dialog = new AlertDialog.Builder(MyHouseActivity.this);
                dialog.setTitle("保存");
                dialog.setMessage("确认要保存？");
                dialog.setCancelable(true);
                dialog.setPositiveButton("好", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (imgPath == null){
                            save1();
                        }else{
                            save2();
                        }
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();
            }
        });
    }

    private void save1(){
        final ProgressDialog progressDialog = new ProgressDialog(MyHouseActivity.this);
        progressDialog.setTitle("上传");
        progressDialog.setMessage("上传中...");
        progressDialog.show();
        house.update(itemObjectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    progressDialog.dismiss();
                    Toast.makeText(MyHouseActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.dismiss();
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });

    }

    private void save2(){
        final ProgressDialog progressDialog = new ProgressDialog(MyHouseActivity.this);
        progressDialog.setTitle("上传");
        progressDialog.setMessage("上传中...");
        progressDialog.show();
        final BmobFile bmobFile = new BmobFile(new File(imgPath));
        house.setImage(bmobFile);
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    house.update(itemObjectId, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e == null){
                                progressDialog.dismiss();
                                Toast.makeText(MyHouseActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                            }else{
                                progressDialog.dismiss();
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
                if (!(grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                    Toast.makeText(this, "您取消了授权", Toast.LENGTH_SHORT).show();
                }else{
                    chooseImage();
                }
                break;
        }
    }

    private void chooseImage(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;
        switch (requestCode){
            case CHOOSE_PHOTO:
                handleImageOnKitKat(data);
        }
    }

    private void handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this, uri)){
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            }else if ("content".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            imagePath = getImagePath(uri, null);
        }else if ("file".equalsIgnoreCase(uri.getScheme())){
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection){
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        imgPath = path;
        return path;
    }

    private void displayImage(String imagePath){
        if(imagePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            imageView.setImageBitmap(bitmap);
        }else{
            Toast.makeText(this, "获取图片失败", Toast.LENGTH_SHORT).show();
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
