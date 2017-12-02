package com.radicalninja.speedtestpowercycler.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OnComplete extends Event<OnComplete> {

    private String upload, download, nearest, userAgent, jitter, ip, dns, grades, sid, url;
    private int ping, cpubench;
    private boolean https;
    private Server[] servers;
    private Grades grades1;

    public OnComplete(JSONObject raw) {
        super(raw);
    }

    public String getUpload() {
        return upload;
    }

    public String getDownload() {
        return download;
    }

    public String getNearest() {
        return nearest;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getJitter() {
        return jitter;
    }

    public String getIp() {
        return ip;
    }

    public String getDns() {
        return dns;
    }

    public String getGradesString() {
        return grades;
    }

    public String getSid() {
        return sid;
    }

    public String getUrl() {
        return url;
    }

    public int getPing() {
        return ping;
    }

    public int getCpubench() {
        return cpubench;
    }

    public boolean isHttps() {
        return https;
    }

    public Server[] getServers() {
        return servers;
    }

    public Grades getGrades() {
        return grades1;
    }

    @Override
    protected void parse(JSONObject json) {
        upload = json.optString("upload");
        download = json.optString("download");
        nearest = json.optString("nearest");
        userAgent = json.optString("ua");
        jitter = json.optString("jitter");
        ip = json.optString("ip");
        dns = json.optString("dns");
        grades = json.optString("grades");
        sid = json.optString("sid");
        url = json.optString("url");
        ping = json.optInt("ping", -1);
        cpubench = json.optInt("cpubench", -1);
        https = json.optBoolean("https");
        grades1 = new Grades(json.optJSONObject("grades1"));
        final JSONArray serverJson = json.optJSONArray("servers");
        if (null != servers) {
            servers = new Server[serverJson.length()];
            for (int i = 0; i <= serverJson.length(); i++) {
                try {
                    servers[i] = new Server(serverJson.getJSONObject(i));
                } catch (JSONException e) { }
            }
        }
    }

}
