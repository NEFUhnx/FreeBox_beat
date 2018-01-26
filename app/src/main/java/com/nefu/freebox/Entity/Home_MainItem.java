package com.nefu.freebox.Entity;

/**
 * Created by 22062 on 2018/1/22.
 */

public class Home_MainItem {
    private int itemId;
    private String name;
    private int imageId;

    public Home_MainItem(){

    }

    public Home_MainItem(String name, int imageId){
        this.name = name;
        this.imageId = imageId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
