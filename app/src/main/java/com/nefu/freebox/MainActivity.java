package com.nefu.freebox;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Home");
            //actionBar.setHomeAsUpIndicator(R.mipmap.touxiang);
        }
        navigationView.setCheckedItem(R.id.nav_home);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        toolbar.setTitle("Home");
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_msg:
                        toolbar.setTitle("Message");
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_myhouse:
                        toolbar.setTitle("My House");
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_stars:
                        toolbar.setTitle("Stars");
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_history:
                        toolbar.setTitle("History");
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_setting:
                        toolbar.setTitle("Setting");
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_abouts:
                        toolbar.setTitle("Abouts");
                        mDrawerLayout.closeDrawers();
                        break;
                    default:
                }
                return true;
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }
}
