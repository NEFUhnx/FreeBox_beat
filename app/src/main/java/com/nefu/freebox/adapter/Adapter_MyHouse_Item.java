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
import com.nefu.freebox.activity.MyHouseActivity;
import com.nefu.freebox.entity.MyHouse_Item;
import com.nefu.freebox.R;

import java.util.List;

/**
 * Created by 22062 on 2018/2/6.
 */

public class Adapter_MyHouse_Item extends RecyclerView.Adapter<Adapter_MyHouse_Item.ViewHolder> {

    private Context mContext;
    private List<MyHouse_Item> mItemList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView itemImage;
        TextView itemName;
        TextView itemDescribe;
        TextView itemPrice;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            itemImage = (ImageView) view.findViewById(R.id.item_house_main_img);
            itemName = (TextView) view.findViewById(R.id.item_house_main_name);
            itemDescribe = (TextView) view.findViewById(R.id.item_house_main_describe);
            itemPrice = (TextView) view.findViewById(R.id.item_house_main_price);
        }
    }

    public Adapter_MyHouse_Item(List<MyHouse_Item> itemList){
        mItemList = itemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_myhouse_main, parent,
                false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                MyHouse_Item item = mItemList.get(position);
                Intent intent = new Intent(mContext, MyHouseActivity.class);
                intent.putExtra(MyHouseActivity.ITEM_NAME, item.getMyHouse_itemName());
                intent.putExtra(MyHouseActivity.ITEM_IMAGE_ID, item.getMyHouse_imageId());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MyHouse_Item item = mItemList.get(position);
        holder.itemName.setText(item.getMyHouse_itemName());
        Glide.with(mContext).load(item.getMyHouse_imageId()).into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}
