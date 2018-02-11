package com.nefu.freebox.activity;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nefu.freebox.bean.AppBarStateChangeListener;
import com.nefu.freebox.bean.BaseActivity;
import com.nefu.freebox.R;

public class HomeActivity extends BaseActivity {
    public static final String ITEM_NAME = "item_name";
    public static final String ITEM_IMAGE_ID = "item_image_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent = getIntent();
        String itemName = intent.getStringExtra(ITEM_NAME);
        int itemImageId = intent.getIntExtra(ITEM_IMAGE_ID, 0);
        final Toolbar toolbar = findViewById(R.id.activity_home_toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)
                findViewById(R.id.activity_home_collapsing);
        ImageView imageView = (ImageView) findViewById(R.id.activity_home_img);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout.setTitle(itemName);
        Glide.with(this).load(itemImageId).into(imageView);

        AppBarLayout appBarLayout;
        appBarLayout = (AppBarLayout) findViewById(R.id.activity_home_appBar);
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if( state == State.EXPANDED ) {
                    //展开状态
                    toolbar.getMenu().setGroupVisible(0, false);
                }else if(state == State.COLLAPSED){
                    //折叠状态
                    toolbar.getMenu().setGroupVisible(0, true);
                }else {
                    //中间状态
                    toolbar.getMenu().setGroupVisible(0, false);
                }
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.setGroupVisible(0, false);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
