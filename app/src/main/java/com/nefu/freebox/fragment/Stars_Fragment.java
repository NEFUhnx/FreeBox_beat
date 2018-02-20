package com.nefu.freebox.fragment;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.view.View;
import android.view.ViewGroup;

import com.nefu.freebox.adapter.Adapter_Home_MainItem;
import com.nefu.freebox.adapter.Adapter_Stars_Item;
import com.nefu.freebox.entity.Home_MainItem;
import com.nefu.freebox.R;
import com.nefu.freebox.entity.House;
import com.nefu.freebox.entity.Stars;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by 22062 on 2018/1/21.
 */

public class Stars_Fragment extends Fragment {

    private static final String TAG = "Stars_Fragment";

    private List<House> itemList = new ArrayList<>();
    private Adapter_Stars_Item adapter;
    private SwipeRefreshLayout swipeRefresh;

    private AppCompatActivity activity;

    public static Stars_Fragment newInstance(){
        return new Stars_Fragment();
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

        initView();
        setListener();
    }

    private void initView(){
        activity = (AppCompatActivity) getActivity();
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

        swipeRefresh = activity.findViewById(R.id.star_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorAccent);
    }

    private void setListener(){
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });
    }

    private void initItems(){
        //添加收藏数据
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String mobile = pref.getString("MOBILE_NUMBER", "");
        itemList.clear();
        BmobQuery<Stars> query = new BmobQuery<>();
        query.addWhereEqualTo("mobileNumber", mobile);
        query.setLimit(20);
        query.findObjects(new FindListener<Stars>() {
            @Override
            public void done(List<Stars> list, BmobException e) {
                if (e == null){
                    List<String> objectIds = new ArrayList<>();
                    for (Stars l :list){
                        objectIds.add(l.getItemObjectId());
                    }
                    BmobQuery<House> query1 = new BmobQuery<>();
                    query1.addWhereContainedIn("objectId", objectIds);
                    query1.findObjects(new FindListener<House>() {
                        @Override
                        public void done(List<House> list, BmobException e) {
                            if (e == null){
                                itemList.addAll(list);
                                RecyclerView recyclerView = activity.findViewById(R.id.fragment_stars_recycler_view);
                                GridLayoutManager layoutManager = new GridLayoutManager(activity, 1);
                                recyclerView.setLayoutManager(layoutManager);
                                adapter = new Adapter_Stars_Item(itemList);
                                recyclerView.setAdapter(adapter);
                            }else{
                                Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }
                    });
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
