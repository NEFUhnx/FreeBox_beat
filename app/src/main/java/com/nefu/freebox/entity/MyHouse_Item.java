package com.nefu.freebox.entity;

/**
 * Created by 22062 on 2018/2/6.
 */

public class MyHouse_Item {
    public String getMyHouse_itemName() {
        return MyHouse_itemName;
    }

    public void setMyHouse_itemName(String order_itemName) {
        MyHouse_itemName = order_itemName;
    }

    public int getMyHouse_imageId() {
        return MyHouse_imageId;
    }

    public void setMyHouse_imageId(int order_imageId) {
        MyHouse_imageId = order_imageId;
    }

    private String MyHouse_itemName;
    private int MyHouse_imageId;

    public MyHouse_Item(String name, int imageId){
        MyHouse_itemName = name;
        MyHouse_imageId = imageId;
    }
}
