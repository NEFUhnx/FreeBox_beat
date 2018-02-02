package com.nefu.freebox.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nefu.freebox.Activity.MsgActivity;
import com.nefu.freebox.Entity.Msg_Item;
import com.nefu.freebox.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 22062 on 2018/1/26.
 */

public class Adapter_Msg_Item extends RecyclerView.Adapter<Adapter_Msg_Item.ViewHolder>{
    private Context mContext;
    private List<Msg_Item> mItemList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View itemView;
        CircleImageView itemImage;
        TextView itemName;
        TextView itemMsg;
        TextView itemTime;
        TextView itemCount;

        public ViewHolder(View view){
            super(view);
            itemView = view;
            itemImage = (CircleImageView) view.findViewById(R.id.item_msg_img);
            itemName = (TextView) view.findViewById(R.id.item_msg_username);
            itemMsg = (TextView) view.findViewById(R.id.item_msg_msg);
            itemTime = (TextView) view.findViewById(R.id.item_msg_time);
            itemCount = (TextView) view.findViewById(R.id.item_msg_count);
        }
    }

    public Adapter_Msg_Item(List<Msg_Item> itemList){
        mItemList = itemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_msg_main, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Msg_Item item = mItemList.get(position);
                Intent intent = new Intent(mContext, MsgActivity.class);
                intent.putExtra(MsgActivity.MSG_USER_NAME, item.getMsg_userName());
                intent.putExtra(MsgActivity.MSG_USER_IMAGE, item.getMsg_userImage());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Msg_Item item = mItemList.get(position);
        holder.itemName.setText(item.getMsg_userName());
        Glide.with(mContext).load(item.getMsg_userImage()).into(holder.itemImage);
        holder.itemMsg.setText(item.getMsg_userMsg());
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}
