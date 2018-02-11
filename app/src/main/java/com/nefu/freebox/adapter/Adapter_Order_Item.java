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
import com.nefu.freebox.activity.OrderActivity;
import com.nefu.freebox.entity.Order_Item;
import com.nefu.freebox.R;

import java.util.List;

/**
 * Created by 22062 on 2018/2/2.
 */

public class Adapter_Order_Item extends RecyclerView.Adapter<Adapter_Order_Item.ViewHolder> {
    private Context mContext;
    private List<Order_Item> mItemList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView itemImage;
        TextView itemName;
        TextView itemDescribe;
        TextView itemPrice;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            itemImage = (ImageView) view.findViewById(R.id.item_order_main_img);
            itemName = (TextView) view.findViewById(R.id.item_order_main_name);
            itemDescribe = (TextView) view.findViewById(R.id.item_order_main_describe);
            itemPrice = (TextView) view.findViewById(R.id.item_order_main_price);
        }
    }
    public Adapter_Order_Item(List<Order_Item> itemList){
        mItemList = itemList;
    }

    @Override
    public Adapter_Order_Item.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_order_main, parent, false);
        final Adapter_Order_Item.ViewHolder holder = new Adapter_Order_Item.ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Order_Item item = mItemList.get(position);
                Intent intent = new Intent(mContext, OrderActivity.class);
                intent.putExtra(OrderActivity.ITEM_NAME, item.getOrder_itemName());
                intent.putExtra(OrderActivity.ITEM_IMAGE_ID, item.getOrder_imageId());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(Adapter_Order_Item.ViewHolder holder, int position) {
        Order_Item item = mItemList.get(position);
        holder.itemName.setText(item.getOrder_itemName());
        Glide.with(mContext).load(item.getOrder_imageId()).into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}
