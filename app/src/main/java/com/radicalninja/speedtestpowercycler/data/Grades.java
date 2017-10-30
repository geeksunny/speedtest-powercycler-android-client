package com.radicalninja.speedtestpowercycler.data;

import org.json.JSONObject;

public class Grades extends JsonResponse<Grades> {

    public String overall, speed, quality, bloat;

    public Grades(JSONObject raw) {
        super(raw);
    }

    @Override
    protected void parse(JSONObject json) {
        overall = json.optString("overall");
        speed = json.optString("speed");
        quality = json.optString("quality");
        bloat = json.optString("bloat");
    }
}
