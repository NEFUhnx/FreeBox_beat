package com.nefu.freebox.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.nefu.freebox.Bean.BaseActivity;
import com.nefu.freebox.Bean.MIUI;
import com.nefu.freebox.R;
import com.nefu.freebox.fragment.History_Fragment;
import com.nefu.freebox.fragment.Home_Fragment;
import com.nefu.freebox.fragment.Msg_Fragment;
import com.nefu.freebox.fragment.Myhouse_Fragment;
import com.nefu.freebox.fragment.Order_Fragment;
import com.nefu.freebox.fragment.Stars_Fragment;

public class MainActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout;

    private long mExitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        replaceFragment(new Home_Fragment());
        navigationView.setCheckedItem(R.id.nav_home);
        NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        navigationMenuView.setVerticalScrollBarEnabled(false);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        replaceFragment(new Home_Fragment());
                        break;
                    case R.id.nav_msg:
                        replaceFragment(new Msg_Fragment());
                        break;
                    case R.id.nav_order:
                        replaceFragment(new Order_Fragment());
                        break;
                    case R.id.nav_myhouse:
                        replaceFragment(new Myhouse_Fragment());
                        break;
                    case R.id.nav_stars:
                        replaceFragment(new Stars_Fragment());
                        break;
                    case R.id.nav_history:
                        replaceFragment(new History_Fragment());
                        break;
                    case R.id.nav_setting:
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
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment, fragment);
        fragmentTransaction.commit();
    }

    /*public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }*/

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
            if(System.currentTimeMillis() - mExitTime > 1500) {
                mExitTime = System.currentTimeMillis();
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            }
            else
                super.onBackPressed();
        }
    }
}
