package com.nefu.freebox.fragment;

import android.os.Bundle;
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
import android.widget.Toast;

import com.nefu.freebox.adapter.Adapter_Home_MainItem;
import com.nefu.freebox.entity.Home_MainItem;
import com.nefu.freebox.R;
import com.nefu.freebox.entity.House;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * Created by 22062 on 2018/1/20.
 */

public class Home_Fragment extends Fragment {

    private AppCompatActivity activity;

    private List<House> itemList = new ArrayList<>();
    private Adapter_Home_MainItem adapter;
    private SwipeRefreshLayout swipeRefresh;

    public static Home_Fragment newInstance(){
        return new Home_Fragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (AppCompatActivity) getActivity();
        Toolbar toolbar = activity.findViewById(R.id.toolbar_home);
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("主页");
            actionBar.setHomeAsUpIndicator(R.mipmap.menu);
        }
        initItems();
        swipeRefresh = activity.findViewById(R.id.home_main_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorAccent);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //menu.clear();
        inflater.inflate(R.menu.toolbar_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.menu_search:
                Toast.makeText(getActivity(), "Search", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }

    private void initItems(){
        /*for(int i=0; i<items.length; i++){
            items[i] = new Home_MainItem("Name"+i, R.mipmap.touxiang);
        }
        itemList.clear();
        for(int i=0; i<20; i++){
            Random random = new Random();
            int index = random.nextInt(items.length);
            itemList.add(items[index]);
        }*/
        itemList.clear();
        BmobQuery<House> query = new BmobQuery<>();
        query.setLimit(10);
        query.findObjects(new FindListener<House>() {
            @Override
            public void done(List<House> list, BmobException e) {
                if (e == null) {
                    itemList.addAll(list);
                    RecyclerView recyclerView = activity.findViewById(R.id.fragment_home_recycler_view);
                    GridLayoutManager layoutManager = new GridLayoutManager(activity, 1);
                    recyclerView.setLayoutManager(layoutManager);
                    adapter = new Adapter_Home_MainItem(itemList);
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
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }


}
