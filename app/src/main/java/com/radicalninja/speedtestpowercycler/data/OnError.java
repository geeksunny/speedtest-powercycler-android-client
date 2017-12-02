package com.radicalninja.speedtestpowercycler.data;

import org.json.JSONObject;

public class OnError extends Event<OnError> {

    private String message;

    public OnError(JSONObject raw) {
        super(raw);
    }

    public String getMessage() {
        return message;
    }

    @Override
    protected void parse(JSONObject json) {
        message = json.optString("msg");
    }

}
