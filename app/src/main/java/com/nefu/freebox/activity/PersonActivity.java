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
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nefu.freebox.R;
import com.nefu.freebox.bean.Flyme;
import com.nefu.freebox.bean.MIUI;
import com.nefu.freebox.entity.User;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.nefu.freebox.bean.BaseActivity.CHOOSE_PHOTO;

public class PersonActivity extends AppCompatActivity {

    private static int output_X = 600;
    private static int output_Y = 600;

    private CircleImageView imageView;
    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutMobile;
    private TextInputLayout textInputLayoutAdd;
    private Button bt_reset;
    private Button bt_logoff;
    private EditText etAddress;
    private EditText etName;

    private SharedPreferences pref;
    private String number;
    private String imgPath = null;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        if(MIUI.MIUISetStatusBarLightMode(this,true)){

        }
        else Flyme.FlymeSetStatusBarLightMode(this.getWindow(),true);

        initView();
        setListener();
    }

    private void initView(){
        imageView = findViewById(R.id.image);
        textInputLayoutName = findViewById(R.id.id_name);
        textInputLayoutMobile = findViewById(R.id.id_mobile_number);
        textInputLayoutAdd = findViewById(R.id.id_address);
        etAddress = findViewById(R.id.et_address);
        etName = findViewById(R.id.et_name);
        bt_reset = findViewById(R.id.bt_reset_password);
        bt_logoff = findViewById(R.id.bt_logoff);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        number = pref.getString("MOBILE_NUMBER", "");
        textInputLayoutMobile.getEditText().setText(number);
        textInputLayoutMobile.getEditText().setEnabled(false);
        Toolbar toolbar = findViewById(R.id.toolbar_person);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("个人信息");
        }

        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("mobileNumber", number);
        query.findObjects(new FindListener<User>(){
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null){
                    user = list.get(0);
                    if (user.getName() != null){
                        etName.setText(user.getName());
                    }
                    if (user.getAddress() != null){
                        etAddress.setText(user.getAddress());
                    }
                    if (user.getImage() != null){
                        Glide.with(PersonActivity.this).load(user.getImage().getUrl()).into(imageView);
                    }
                }
            }
        });
    }

    private void setListener(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permissionCheck = ContextCompat.checkSelfPermission(PersonActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PersonActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    //TODO
                    chooseImage();
                }
            }
        });

        bt_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        bt_logoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(PersonActivity.this);
                dialog.setTitle("注销");
                dialog.setMessage("您确定要退出登录?");
                dialog.setCancelable(true);
                dialog.setPositiveButton("好", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(PersonActivity.this);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.clear();
                        editor.apply();
                        Intent intent = new Intent(PersonActivity.this, LoginActivity.class);
                        startActivity(intent);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.person_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.save:
                AlertDialog.Builder dialog = new AlertDialog.Builder(PersonActivity.this);
                dialog.setTitle("保存");
                dialog.setMessage("确认保存个人信息?");
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
        return super.onOptionsItemSelected(item);
    }

    private void save1(){
        final ProgressDialog progressDialog = new ProgressDialog(PersonActivity.this);
        progressDialog.setTitle("上传");
        progressDialog.setMessage("上传中...");
        progressDialog.show();
        final String name = etName.getText().toString();
        final String address = etAddress.getText().toString();
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("mobileNumber", number);
        query.findObjects(new FindListener<User>(){
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null){
                    user = list.get(0);
                    user.setName(name);
                    user.setAddress(address);
                    user.update(user.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null){
                                progressDialog.dismiss();
                                Toast.makeText(PersonActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(PersonActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                                Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }
                    });
                }
            }
        });
    }

    private void save2(){
        final ProgressDialog progressDialog = new ProgressDialog(PersonActivity.this);
        progressDialog.setTitle("上传");
        progressDialog.setMessage("上传中...");
        progressDialog.show();
        final String name = etName.getText().toString();
        final String address = etAddress.getText().toString();
        final BmobFile bmobFile = new BmobFile(new File(imgPath));
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("mobileNumber", number);
        query.findObjects(new FindListener<User>(){
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null){
                    user = list.get(0);
                    user.setName(name);
                    user.setAddress(address);
                    user.setImage(bmobFile);
                    bmobFile.uploadblock(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null){
                                user.update(user.getObjectId(), new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null){
                                            progressDialog.dismiss();
                                            Toast.makeText(PersonActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                                        }else{
                                            progressDialog.dismiss();
                                            Toast.makeText(PersonActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                                            Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }
}
