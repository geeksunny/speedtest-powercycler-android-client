package com.radicalninja.speedtestpowercycler.data;

import android.text.TextUtils;

import com.radicalninja.speedtestpowercycler.JsonResponse;

import org.json.JSONObject;

public class OnStatus extends JsonResponse<OnStatus> {

    public enum Direction {
        PING("pinging"),
        DOWNLOAD("downloading"),
        UPLOAD("uploading"),
        _UNKNOWN("NOT RECOGNIZED");

        private final String value;

        Direction(final String value) {
            this.value = value;
        }

        public static Direction fromString(final String value) {
            if (!TextUtils.isEmpty(value)) {
                for (final Direction d : Direction.values()) {
                    if (d.value.equalsIgnoreCase(value)) {
                        return d;
                    }
                }
            }
            return _UNKNOWN;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    private Direction direction;
    private String directionString;
    private double down, download;
    private double up, upload;
    private int ping;

    public OnStatus(JSONObject raw) {
        super(raw);
    }

    @Override
    protected void parse(JSONObject json) {
        directionString = json.optString("direction");
        direction = Direction.fromString(directionString);
        down = json.optDouble("down", -1);
        download = json.optDouble("download", -1);
        up = json.optDouble("up", -1);
        upload = json.optDouble("upload", -1);
        ping = json.optInt("ping", -1);
    }

}
