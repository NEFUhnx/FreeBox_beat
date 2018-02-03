package com.nefu.freebox.Entity;

/**
 * Created by 22062 on 2018/2/2.
 */

public class Order_Item {
    public String getOrder_itemName() {
        return Order_itemName;
    }

    public void setOrder_itemName(String order_itemName) {
        Order_itemName = order_itemName;
    }

    public int getOrder_imageId() {
        return Order_imageId;
    }

    public void setOrder_imageId(int order_imageId) {
        Order_imageId = order_imageId;
    }

    private String Order_itemName;
    private int Order_imageId;

}
