package com.radicalninja.speedtestpowercycler.data;

import org.json.JSONObject;

public abstract class JsonResponse<T extends JsonResponse> {

    public JsonResponse(final JSONObject raw) {
        if (null != raw) {
            parse(raw);
//        } else {
//            throw new NullPointerException("Null JSON object provided.");
        }
    }

    protected abstract void parse(final JSONObject json);

}
