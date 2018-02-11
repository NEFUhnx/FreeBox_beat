package com.nefu.freebox.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by 22062 on 2018/2/10.
 */

public class User extends BmobObject {

    private String mobileNumber;
    private String password;
    private Integer userImg;

    public User(){
    }


    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getUserImg() {
        return userImg;
    }

    public void setUserImg(Integer userImg) {
        this.userImg = userImg;
    }
}
