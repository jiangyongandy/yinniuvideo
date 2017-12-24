package com.zuiai.nn.obj;

import java.io.Serializable;

/**
 * 功能
 * Created by Jiang on 2017/11/29.
 */

public class UserEntity implements Serializable {

    private String vip;
    private String msg;



    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
