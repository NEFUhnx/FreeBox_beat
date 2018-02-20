package com.nefu.freebox.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by hnx on 2018/2/20.
 */

public class Stars extends BmobObject {

    private String mobileNumber;
    private String itemObjectId;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getItemObjectId() {
        return itemObjectId;
    }

    public void setItemObjectId(String itemObjectId) {
        this.itemObjectId = itemObjectId;
    }
}
