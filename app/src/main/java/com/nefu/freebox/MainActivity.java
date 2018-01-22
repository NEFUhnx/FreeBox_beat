package com.nefu.freebox;

import android.content.Intent;
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
import android.util.Log;
import android.view.MenuItem;

import com.nefu.fragment.History_Fragment;
import com.nefu.fragment.Home_Fragment;
import com.nefu.fragment.Msg_Fragment;
import com.nefu.fragment.Myhouse_Fragment;
import com.nefu.fragment.Order_Fragment;
import com.nefu.fragment.Stars_Fragment;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

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
}
