package com.nefu.freebox.Activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.nefu.freebox.Bean.BaseActivity;
import com.nefu.freebox.R;

public class OrderActivity extends BaseActivity {

    public static final String ITEM_NAME = "item_name";
    public static final String ITEM_IMAGE_ID = "item_image_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_order_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = (ActionBar) getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("Order Details");
            actionBar.setDisplayHomeAsUpEnabled(true);
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
