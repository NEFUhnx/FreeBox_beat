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
import android.support.design.widget.TextInputLayout;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nefu.freebox.R;
import com.nefu.freebox.bean.BaseActivity;
import com.nefu.freebox.entity.House;
import com.nefu.freebox.entity.User;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class ReleaseActivity extends BaseActivity {

    private ImageView imageView;
    private TextInputEditText textTitle;
    private TextInputEditText textLocation;
    private TextInputEditText textRent;
    private TextInputEditText textHouseArea;
    private TextInputEditText textMobileNumber;
    private TextInputEditText textDescribe;
    private Button bt_release;

    private SharedPreferences pref;
    private String mobile;
    private String imgPath = null;

    private House house = new House();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);
        initView();
        setListener();
    }

    private void initView(){
        Toolbar toolbar = findViewById(R.id.activity_release_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("发布");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        imageView = findViewById(R.id.release_image);
        textTitle = findViewById(R.id.release_et_title);
        textLocation = findViewById(R.id.release_et_location);
        textRent = findViewById(R.id.release_et_rent);
        textHouseArea = findViewById(R.id.release_et_house_area);
        textMobileNumber = findViewById(R.id.release_et_mobile);
        textDescribe = findViewById(R.id.release_et_describe);
        bt_release = findViewById(R.id.bt_release);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        mobile = pref.getString("MOBILE_NUMBER", "");
        textMobileNumber.setText(mobile);
        textMobileNumber.setEnabled(false);
    }

    private void setListener(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permissionCheck = ContextCompat.checkSelfPermission(ReleaseActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ReleaseActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    //TODO
                    chooseImage();
                }
            }
        });

        bt_release.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgPath == null){
                    Toast.makeText(ReleaseActivity.this, "请选择一张照片", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (textTitle.getText().toString().equals("") || (textTitle.getText() == null)){
                    Toast.makeText(ReleaseActivity.this, "请输入标题", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (textLocation.getText().toString().equals("") || (textLocation.getText() == null)){
                    Toast.makeText(ReleaseActivity.this, "请输入地址", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (textRent.getText().toString().equals("") || (textRent.getText() == null)){
                    Toast.makeText(ReleaseActivity.this, "请输入租金", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (textHouseArea.getText().toString().equals("") || (textHouseArea.getText() == null)){
                    Toast.makeText(ReleaseActivity.this, "请输入房屋面积", Toast.LENGTH_SHORT).show();
                    return;
                }
                house.setTitle(textTitle.getText().toString());
                house.setLocation(textLocation.getText().toString());
                house.setRent(textRent.getText().toString());
                house.setHouseArea(textHouseArea.getText().toString());
                house.setMobileNumber(textMobileNumber.getText().toString());
                house.setDescribe(textDescribe.getText().toString());
                AlertDialog.Builder dialog = new AlertDialog.Builder(ReleaseActivity.this);
                dialog.setTitle("发布");
                dialog.setMessage("确认要发布该房屋?");
                dialog.setCancelable(true);
                dialog.setPositiveButton("好", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Release();
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

    private void Release(){
        final ProgressDialog progressDialog = new ProgressDialog(ReleaseActivity.this);
        progressDialog.setTitle("上传");
        progressDialog.setMessage("上传中...");
        progressDialog.show();
        final BmobFile bmobFile = new BmobFile(new File(imgPath));
        house.setImage(bmobFile);
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    house.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null){
                                progressDialog.dismiss();
                                Toast.makeText(ReleaseActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
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
