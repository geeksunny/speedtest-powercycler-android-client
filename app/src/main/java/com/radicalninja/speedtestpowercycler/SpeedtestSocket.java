package com.radicalninja.speedtestpowercycler;

import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SpeedtestSocket {

    interface TestListener {
        void onProgress();
        void onStatus();
        void onComplete();
        void onError();
        void onConfirm();
    }

    private static final String NAME_KEY = "key";
    private static final String NAME_DATA = "data";

    private static final String EVENT_READY = "ready";
    private static final String EVENT_UPDATE = "update";
    private static final String EVENT_FINISHED = "finished";
    private static final String EVENT_STOP = "stop";

    private static final String UPDATE_PROGRESS = "onprogress";
    private static final String UPDATE_STATUS = "onstatus";
    private static final String UPDATE_COMPLETE = "oncomplete";
    private static final String UPDATE_ERROR = "onerror";
    private static final String UPDATE_CONFIRM = "onconfirm";

    private final Socket socket;

    private WeakReference<View> viewDelegate;

    public static SpeedtestSocket create(final String url) {
        try {
            return new SpeedtestSocket(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public SpeedtestSocket(final String url) throws URISyntaxException {
        socket = IO.socket(url);
        init();
    }

    protected void init() {
        socket.on(EVENT_READY, onEventReady);
        socket.on(EVENT_UPDATE, onEventUpdate);
        socket.on(EVENT_FINISHED, onEventFinishd);
    }

    protected void handleProgress(final JSONObject data) {
        //
    }

    protected void handleStatus(final JSONObject data) {
        //
    }

    private final Emitter.Listener onEventReady = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            final JSONObject json = (JSONObject) args[0];
            // TODO: Set object as ready to proceed
        }
    };

    private final Emitter.Listener onEventUpdate = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                final JSONObject json = (JSONObject) args[0];
                final String key = json.getString(NAME_KEY);
                final JSONObject data = json.getJSONObject(NAME_DATA);

                switch (key) {
                    case UPDATE_PROGRESS:
                        handleProgress(data);
                        break;
                    case UPDATE_STATUS:
                        handleStatus(data);
                        break;
                    case UPDATE_COMPLETE:
                    case UPDATE_ERROR:
                    case UPDATE_CONFIRM:
                    default:
                        break;
                }
                // TODO: Debug log?
            } catch (JSONException | ClassCastException e) {
                // TODO: HANDLE PARSING ERROR
                e.printStackTrace();
            }
        }
    };

    private final Emitter.Listener onEventFinishd = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            final JSONObject json = (JSONObject) args[0];
        }
    };

}
