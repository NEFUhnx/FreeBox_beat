package com.nefu.freebox.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nefu.freebox.bean.BaseActivity;
import com.nefu.freebox.R;
import com.nefu.freebox.entity.User;
import com.nefu.freebox.fragment.History_Fragment;
import com.nefu.freebox.fragment.Home_Fragment;
import com.nefu.freebox.fragment.Msg_Fragment;
import com.nefu.freebox.fragment.Myhouse_Fragment;
import com.nefu.freebox.fragment.Stars_Fragment;

import org.w3c.dom.Text;

import java.util.List;
import java.util.prefs.Preferences;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private View headerView;
    private CircleImageView navImage;
    private TextView userName;
    private TextView mobile;
    private Fragment currentFragment = null;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String mobile_number;
    private User user;
    private String nav_image;

    private long mExitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setListener();
    }

    private void initView(){
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        navImage = headerView.findViewById(R.id.nav_image);
        userName = headerView.findViewById(R.id.nav_username);
        mobile = headerView.findViewById(R.id.nav_mobile);
        if(currentFragment == null){
            currentFragment = Home_Fragment.newInstance();
            replaceFragment(currentFragment);
        }
        navigationView.setCheckedItem(R.id.nav_home);
        NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        navigationMenuView.setVerticalScrollBarEnabled(false);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();
        mobile_number = pref.getString("MOBILE_NUMBER", "");
        nav_image = pref.getString("NAV_IMAGE", "");
        mobile.setText(mobile_number);
        if (!nav_image.equals("")){
            Glide.with(MainActivity.this).load(nav_image).into(navImage);
        }else{
            loadNavImage();
        }
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("mobileNumber", mobile_number);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if(e == null){
                    user = list.get(0);
                    if ((user.getName() == null) || user.getName().equals("")){
                        userName.setText(mobile_number);
                    }else{
                        userName.setText(user.getName());
                    }
                }else{
                    Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    private void loadNavImage(){
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("mobileNumber", mobile_number);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if(e == null){
                    user = list.get(0);
                    if (user.getImage() != null){
                        Glide.with(MainActivity.this).load(user.getImage().getUrl()).into(navImage);
                        editor.putString("NAV_IMAGE", "");
                        editor.apply();
                    }
                }else{
                    Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    private void setListener(){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        currentFragment = Home_Fragment.newInstance();
                        replaceFragment(currentFragment);
                        break;
                    case R.id.nav_msg:
                        currentFragment = Msg_Fragment.newInstance();
                        replaceFragment(currentFragment);
                        break;
                    case R.id.nav_myhouse:
                        currentFragment = Myhouse_Fragment.newInstance();
                        replaceFragment(currentFragment);
                        break;
                    case R.id.nav_stars:
                        currentFragment = Stars_Fragment.newInstance();
                        replaceFragment(currentFragment);
                        break;
                    case R.id.nav_history:
                        currentFragment = History_Fragment.newInstance();
                        replaceFragment(currentFragment);
                        break;
                    case R.id.nav_settings:
                        Intent intentset = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(intentset);
                        break;
                    case R.id.nav_abouts:
                        Intent intentabt = new Intent(MainActivity.this, AboutsActivity.class);
                        startActivity(intentabt);
                        break;
                    default:
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        navImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PersonActivity.class);
                startActivity(intent);
            }
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(findViewById(R.id.nav_view))){
            mDrawerLayout.closeDrawers();
        }else{
            if(currentFragment == null){
                currentFragment = Home_Fragment.newInstance();
            }
            if(!(currentFragment instanceof Home_Fragment)){
                currentFragment = Home_Fragment.newInstance();
                replaceFragment(currentFragment);
                navigationView.setCheckedItem(R.id.nav_home);
                return;
            }

            if(System.currentTimeMillis() - mExitTime > 1500) {
                mExitTime = System.currentTimeMillis();
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            }
            else
                this.finish();
        }
    }
}
