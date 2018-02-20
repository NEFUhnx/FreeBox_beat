package com.nefu.freebox.entity;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by hnx on 2018/2/18.
 */

public class House extends BmobObject{
    private BmobFile image;
    private String title;
    private String location;
    private String rent;
    private String houseArea;
    private String mobileNumber;
    private String describe;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRent() {
        return rent + "￥/day";
    }

    public void setRent(String rent) {
        this.rent = rent;
    }

    public String getHouseArea() {
        return houseArea + "m²";
    }

    public void setHouseArea(String houseArea) {
        this.houseArea = houseArea;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public BmobFile getImage() {
        return image;
    }

    public void setImage(BmobFile image) {
        this.image = image;
    }
}
