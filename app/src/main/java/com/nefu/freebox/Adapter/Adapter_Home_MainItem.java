package com.nefu.freebox.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nefu.freebox.Activity.Home_Activity;
import com.nefu.freebox.Entity.Home_MainItem;
import com.nefu.freebox.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by 22062 on 2018/1/22.
 */

public class Adapter_Home_MainItem extends RecyclerView.Adapter<Adapter_Home_MainItem.ViewHolder> {
    private Context mContext;
    private List<Home_MainItem> mItemList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView itemImage;
        TextView itemName;
        TextView itemDescribe;
        TextView itemPrice;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            itemImage = (ImageView) view.findViewById(R.id.item_home_main_img);
            itemName = (TextView) view.findViewById(R.id.item_home_main_name);
            itemDescribe = (TextView) view.findViewById(R.id.item_home_main_describe);
            itemPrice = (TextView) view.findViewById(R.id.item_home_main_price);
        }
    }

    public Adapter_Home_MainItem(List<Home_MainItem> itemList){
        mItemList = itemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_home_main, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Home_MainItem item = mItemList.get(position);
                Intent intent = new Intent(mContext, Home_Activity.class);
                intent.putExtra(Home_Activity.ITEM_NAME, item.getName());
                intent.putExtra(Home_Activity.ITEM_IMAGE_ID, item.getImageId());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Home_MainItem item = mItemList.get(position);
        holder.itemName.setText(item.getName());
        Glide.with(mContext).load(item.getImageId()).into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}
