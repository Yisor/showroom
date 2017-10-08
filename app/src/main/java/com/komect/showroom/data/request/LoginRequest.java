package com.komect.showroom.data.request;

/**
 * Created by lsl on 2017/9/7.
 */
public class LoginRequest {
    public String phoneNumber;
    public String verifyCode;


    public String getPhoneNumber() {
        return phoneNumber;
    }


    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getVerifyCode() {
        return verifyCode;
    }


    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
