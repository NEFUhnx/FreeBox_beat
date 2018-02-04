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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nefu.freebox.Adapter.Adapter_Msg_Item;
import com.nefu.freebox.Entity.Msg_Item;
import com.nefu.freebox.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 22062 on 2018/1/20.
 */

public class Msg_Fragment extends Fragment {

    private Msg_Item[] items = new Msg_Item[5];
    private List<Msg_Item> itemList = new ArrayList<>();
    private Adapter_Msg_Item adapter;
    private SwipeRefreshLayout swipeRefresh;

    public static Msg_Fragment newInstance(){
        Msg_Fragment fragment = new Msg_Fragment();
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
        View view = inflater.inflate(R.layout.fragment_msg, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar_msg);
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = (ActionBar) activity.getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Message");
            actionBar.setHomeAsUpIndicator(R.mipmap.menu);
        }

        initItem();
        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.fragment_msg_recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(activity, 1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter_Msg_Item(itemList);
        recyclerView.setAdapter(adapter);

        swipeRefresh = (SwipeRefreshLayout) activity.findViewById(R.id.msg_main_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorAccent);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });
    }

    private void initItem(){
        itemList.clear();
        for(int i=0; i<items.length; i++){
            items[i] = new Msg_Item(R.mipmap.touxiang, "NAME", "NAME:MESSAGE");
            itemList.add(items[i]);
        }
    }

    private void refreshItems(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(300);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        initItem();
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
}
