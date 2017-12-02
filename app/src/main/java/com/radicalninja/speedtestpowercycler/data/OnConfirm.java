package com.radicalninja.speedtestpowercycler.data;

import org.json.JSONObject;

public class OnConfirm extends Event<OnConfirm> {

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
