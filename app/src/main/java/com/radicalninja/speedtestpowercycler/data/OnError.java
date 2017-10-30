package com.radicalninja.speedtestpowercycler.data;

import com.radicalninja.speedtestpowercycler.JsonResponse;

import org.json.JSONObject;

public class OnError extends JsonResponse<OnError> {

    private String message;

    public OnError(JSONObject raw) {
        super(raw);
    }

    @Override
    protected void parse(JSONObject json) {
        message = json.optString("msg");
    }

}
