package com.nefu.freebox.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.nefu.freebox.activity.ReleaseActivity;
import com.nefu.freebox.adapter.Adapter_MyHouse_Item;
import com.nefu.freebox.entity.House;
import com.nefu.freebox.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 22062 on 2018/1/21.
 */

public class Myhouse_Fragment extends Fragment {

    private List<House> itemList = new ArrayList<>();
    private Adapter_MyHouse_Item adapter;
    private AppCompatActivity activity;
    private SwipeRefreshLayout refreshLayout;

    private SharedPreferences pref;
    private String mobileNumber;

    public static Myhouse_Fragment newInstance(){
        return new Myhouse_Fragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myhouse, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        setListener();
    }

    private void initView(){
        activity = (AppCompatActivity) getActivity();
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar_myhouse);
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = (ActionBar) activity.getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("我的发布");
            actionBar.setHomeAsUpIndicator(R.mipmap.menu);
        }
        refreshLayout = activity.findViewById(R.id.my_house_refresh);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);

        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        mobileNumber = pref.getString("MOBILE_NUMBER", "");

        initItems();
    }

    private void setListener(){
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_myhouse, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_release:
                Intent intent = new Intent(getActivity(), ReleaseActivity.class);
                startActivity(intent);
                break;
            default:
        }
        return true;
    }

    private void initItems(){
        itemList.clear();
        BmobQuery<House> query = new BmobQuery<>();
        query.addWhereEqualTo("mobileNumber", mobileNumber);
        query.setLimit(10);
        query.findObjects(new FindListener<House>() {
            @Override
            public void done(List<House> list, BmobException e) {
                if (e == null) {
                    itemList.addAll(list);
                    RecyclerView recyclerView = activity.findViewById(R.id.fragment_myhouse_recycler_view);
                    GridLayoutManager layoutManager = new GridLayoutManager(activity, 1);
                    recyclerView.setLayoutManager(layoutManager);
                    adapter = new Adapter_MyHouse_Item(itemList);
                    recyclerView.setAdapter(adapter);
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    private void refreshItems(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        initItems();
                        adapter.notifyDataSetChanged();
                        refreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
}
