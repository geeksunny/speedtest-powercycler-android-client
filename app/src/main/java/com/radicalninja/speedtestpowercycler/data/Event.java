package com.radicalninja.speedtestpowercycler.data;

import org.json.JSONObject;

public abstract class Event<T extends Event> extends JsonResponse<T> {

    private final String json;

    public Event(JSONObject raw) {
        super(raw);
        json = raw.toString();
    }

    public String getName() {
        return getClass().getSimpleName();
    }

    public String getJson() {
        return json;
    }

}
