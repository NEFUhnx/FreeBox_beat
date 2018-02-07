package com.nefu.freebox.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nefu.freebox.Adapter.Adapter_Stars_Item;
import com.nefu.freebox.Entity.Home_MainItem;
import com.nefu.freebox.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 22062 on 2018/1/21.
 */

public class Stars_Fragment extends Fragment {

    private Home_MainItem[] items = new Home_MainItem[2];
    private List<Home_MainItem> itemList = new ArrayList<>();
    private Adapter_Stars_Item adapter;

    public static Stars_Fragment newInstance(){
        Stars_Fragment fragment = new Stars_Fragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stars, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar_stars);
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = (ActionBar) activity.getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Stars");
            actionBar.setHomeAsUpIndicator(R.mipmap.menu);
        }

        initItems();
        RecyclerView recyclerView = (RecyclerView) activity.findViewById(
                R.id.fragment_stars_recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(activity, 1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter_Stars_Item(itemList);
        recyclerView.setAdapter(adapter);
    }

    private void initItems(){
        items[0] = new Home_MainItem("name", R.mipmap.touxiang);
        items[1] = new Home_MainItem("name1", R.mipmap.touxiang);
        itemList.add(items[0]);
        itemList.add(items[1]);
    }
}
