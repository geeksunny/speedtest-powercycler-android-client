package com.radicalninja.speedtestpowercycler.data;

import org.json.JSONObject;

public abstract class Event<T extends Event> extends JsonResponse<T> {

    public Event(JSONObject raw) {
        super(raw);
    }

}
