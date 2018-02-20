package com.nefu.freebox.adapter;

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
import com.nefu.freebox.activity.HomeActivity;
import com.nefu.freebox.entity.Home_MainItem;
import com.nefu.freebox.R;
import com.nefu.freebox.entity.House;

import java.util.List;

/**
 * Created by 22062 on 2018/2/6.
 */

public class Adapter_Stars_Item extends RecyclerView.Adapter<Adapter_Stars_Item.ViewHolder> {

    private Context mContext;
    private List<House> mItemList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView itemImage;
        TextView itemTitle;
        TextView itemDescribe;
        TextView itemRent;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            itemImage = view.findViewById(R.id.item_home_main_img);
            itemTitle = view.findViewById(R.id.item_home_main_title);
            itemDescribe = view.findViewById(R.id.item_home_main_describe);
            itemRent = view.findViewById(R.id.item_home_main_rent);
        }
    }

    public Adapter_Stars_Item(List<House> itemList){
        mItemList = itemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_home_main,parent,
                false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                House item = mItemList.get(position);
                Intent intent = new Intent(mContext, HomeActivity.class);
                intent.putExtra(HomeActivity.ITEM_OBJECT_ID, item.getObjectId());
                intent.putExtra(HomeActivity.ITEM_TITLE, item.getTitle());
                intent.putExtra(HomeActivity.ITEM_IMAGE, item.getImage().getUrl());
                intent.putExtra(HomeActivity.ITEM_NUMBER, item.getMobileNumber());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        House item = mItemList.get(position);
        holder.itemTitle.setText(item.getTitle());
        Glide.with(mContext).load(item.getImage().getUrl()).into(holder.itemImage);
        holder.itemDescribe.setText(item.getDescribe());
        holder.itemRent.setText(item.getRent());
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}
