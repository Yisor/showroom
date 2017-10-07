package com.komect.showroom.data.response;

import com.google.gson.JsonObject;
import java.io.Serializable;

/**
 * 最基础的HTTP请求返回结果.
 * Author by hf
 * Create on 16/9/18
 */
public class BaseResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private int recode;
    private String msg;
    private JsonObject data;

    public String getMsg() {
        return msg;
    }


    public void setMsg(String msg) {
        this.msg = msg;
    }


    public int getRecode() {
        return recode;
    }


    public void setRecode(int recode) {
        this.recode = recode;
    }

    public JsonObject getData() {
        return data;
    }

    public void setData(JsonObject data) {
        this.data = data;
    }
}
