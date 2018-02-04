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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nefu.freebox.Adapter.Adapter_Order_Item;
import com.nefu.freebox.Entity.Order_Item;
import com.nefu.freebox.R;

import java.util.ArrayList;
import java.util.List;


public class Order_Fragment extends Fragment {

    private Order_Item[] items = new Order_Item[2];
    private List<Order_Item> itemList = new ArrayList<>();
    private Adapter_Order_Item adapter;

    public static Order_Fragment newInstance(){
        Order_Fragment fragment = new Order_Fragment();
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
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar_order);
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = (ActionBar) activity.getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Order");
            actionBar.setHomeAsUpIndicator(R.mipmap.menu);
        }

        initItems();
        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.fragment_order_recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(activity, 1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter_Order_Item(itemList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_order, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_edit:
                Toast.makeText(getActivity(), "Edit", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }

    private void initItems(){
        items[0] = new Order_Item("name", R.mipmap.touxiang);
        items[1] = new Order_Item("name1", R.mipmap.touxiang);
        itemList.add(items[0]);
        itemList.add(items[1]);
    }
}
