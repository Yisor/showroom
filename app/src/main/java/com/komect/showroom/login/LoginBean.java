package com.komect.showroom.login;

import android.databinding.ObservableField;

/**
 * Created by eyow on 2017/10/7.
 */

public class LoginBean {
    public final ObservableField<String> btPassCode = new ObservableField<>("发送验证码"); //获取验证码按钮显示的文字
    public final ObservableField<Boolean> isPhoneNumberCompleted = new ObservableField<>(false);//是否可以获取验证码
    public final ObservableField<Boolean> isPhoneNumberNull = new ObservableField<>(false);//手机号是否为空 true 为非空，false为空
    public final ObservableField<Boolean> isPassCompleted = new ObservableField<>(false);//是否可以登录
    public final ObservableField<Boolean> isPasswordNull = new ObservableField<>(false);

    private String password;
    private String phone;


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
        isPasswordNull.set(!password.isEmpty());
        isPasswordNull.notifyChange();
        updateButtonState();
    }


    public String getPhone() {
        return phone;
    }


    public void setPhone(String phone) {
        this.phone = phone;
        isPhoneNumberNull.set(!phone.isEmpty());
        isPhoneNumberNull.notifyChange();
        updateButtonState();
    }


    private void updateButtonState() {
        if (isPhoneNumberNull.get()) {
            if (phone.length() == 11) {
                isPhoneNumberCompleted.set(true);
            } else {
                isPhoneNumberCompleted.set(false);
            }
            if (isPasswordNull.get()) {
                if (phone.length() == 11 && password.length() > 0) {
                    isPassCompleted.set(true);
                } else {
                    isPassCompleted.set(false);
                }
            } else {
                isPassCompleted.set(false);
            }
        } else {
            isPhoneNumberCompleted.set(false);
        }
    }
}
