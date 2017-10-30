package com.radicalninja.speedtestpowercycler.data;

import android.text.TextUtils;

import org.json.JSONObject;

public class OnProgress extends JsonResponse<OnProgress> {

    public enum Doing {
        PING("pinging"),
        DOWNLOAD("downloading"),
        UPLOAD("uploading"),
        PAUSE("pausing"),
        END("ending"),
        COMPLETE("complete"),
        _UNKNOWN("NOT RECOGNIZED");

        private final String value;

        Doing(final String value) {
            this.value = value;
        }

        public static Doing fromString(final String value) {
            if (!TextUtils.isEmpty(value)) {
                for (final Doing d : Doing.values()) {
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

    private Doing doing;
    private String doingString;
    private int progress;

    public OnProgress(JSONObject raw) {
        super(raw);
    }

    public Doing getDoing() {
        return doing;
    }

    public String getDoingString() {
        return doingString;
    }

    public int getProgress() {
        return progress;
    }

    @Override
    protected void parse(JSONObject json) {
        doingString = json.optString("doing", "");
        doing = Doing.fromString(doingString);
        progress = json.optInt("progress", -1);
    }

}
