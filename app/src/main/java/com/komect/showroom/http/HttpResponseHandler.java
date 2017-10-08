package com.komect.showroom.http;

import android.util.Log;
import com.google.gson.JsonObject;
import com.komect.showroom.data.response.BaseResult;
import com.komect.showroom.event.GlobalMsgEvent;

/**
 * Created by xieyusheng on 2017/10/7.
 */

public class HttpResponseHandler {
    public static JsonObject handleResponse(BaseResult baseResult) {
        if (baseResult == null) {
            return null;
        }
        final int recode = baseResult.getRecode();
        switch (recode) {
            case 200:
                return baseResult.getData();
            case 401:
                Log.d("handleResponse", "handleResponse: 返回登录也");
                break;
            default:
                new GlobalMsgEvent().setDisplayType(GlobalMsgEvent.DisplayType.Toast)
                                    .setMsg(baseResult.getMsg())
                                    .send();
                break;
        }
        return null;
    }
}
