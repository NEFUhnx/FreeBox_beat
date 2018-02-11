package com.nefu.freebox.entity;

/**
 * Created by 22062 on 2018/1/22.
 */

public class Home_MainItem {
    private String Home_itemName;
    private int Home_imageId;

    public Home_MainItem(){

    }

    public Home_MainItem(String name, int Home_imageId){
        this.Home_itemName = name;
        this.Home_imageId = Home_imageId;
    }

    public String getHome_itemName() {
        return Home_itemName;
    }

    public void setHome_itemName(String name) {
        this.Home_itemName = name;
    }

    public int getHome_imageId() {
        return Home_imageId;
    }

    public void setHome_imageId(int home_imageId) {
        this.Home_imageId = home_imageId;
    }
}
