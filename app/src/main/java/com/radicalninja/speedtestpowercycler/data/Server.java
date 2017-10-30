package com.radicalninja.speedtestpowercycler.data;

import org.json.JSONObject;

public class Server extends JsonResponse<Server> {

    private String url, addr;
    private int ping;

    public Server(JSONObject raw) {
        super(raw);
    }

    @Override
    protected void parse(JSONObject json) {
        url = json.optString("url");
        addr = json.optString("addr");
        ping = json.optInt("ping", -1);
    }
}
