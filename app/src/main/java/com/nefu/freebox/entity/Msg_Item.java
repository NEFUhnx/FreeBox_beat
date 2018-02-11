package com.nefu.freebox.entity;

/**
 * Created by 22062 on 2018/1/26.
 */

public class Msg_Item {
    private int Msg_userId;
    private int Msg_userImage;
    private String Msg_userName;
    private String Msg_userMsg;

    public Msg_Item(){}

    public Msg_Item(int Msg_userImage, String Msg_userName, String Msg_userMsg){
        this.Msg_userImage = Msg_userImage;
        this.Msg_userName = Msg_userName;
        this.Msg_userMsg = Msg_userMsg;
    }

    public int getMsg_userId() {
        return Msg_userId;
    }

    public void setMsg_userId(int msg_userId) {
        this.Msg_userId = msg_userId;
    }

    public int getMsg_userImage() {
        return Msg_userImage;
    }

    public void setMsg_userImage(int msg_userImage) {
        this.Msg_userImage = msg_userImage;
    }

    public String getMsg_userName() {
        return Msg_userName;
    }

    public void setMsg_userName(String msg_userName) {
        this.Msg_userName = msg_userName;
    }

    public String getMsg_userMsg() {
        return Msg_userMsg;
    }

    public void setMsg_userMsg(String msg_userMsg) {
        this.Msg_userMsg = msg_userMsg;
    }
}
