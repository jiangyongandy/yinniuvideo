package com.zuiai.nn.obj;

import java.io.Serializable;

/**
 * 功能
 * Created by Jiang on 2017/11/29.
 */

public class UpdateEntity implements Serializable {

    /**
     * bbh : 1.0
     * address : http://www.baidu.com
     * isNo : yes
     */


    private Double bbh;
    private String address;
    private String isNo;

    public Double getBbh() {
        return bbh;
    }

    public void setBbh(Double bbh) {
        this.bbh = bbh;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIsNo() {
        return isNo;
    }

    public void setIsNo(String isNo) {
        this.isNo = isNo;
    }
}
