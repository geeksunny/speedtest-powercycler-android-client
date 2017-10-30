package com.radicalninja.speedtestpowercycler.data;

import com.radicalninja.speedtestpowercycler.JsonResponse;

import org.json.JSONObject;

// TODO: Is class this necessary?
public class OnConfirm extends JsonResponse<OnConfirm> {

    private String message;

    public OnConfirm(JSONObject raw) {
        super(raw);
    }

    public String getMessage() {
        return message;
    }

    @Override
    protected void parse(JSONObject json) {
        message = json.optString("message");
    }

}
