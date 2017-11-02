package com.radicalninja.speedtestpowercycler;

import android.support.annotation.Nullable;

import com.radicalninja.speedtestpowercycler.data.OnComplete;
import com.radicalninja.speedtestpowercycler.data.OnConfirm;
import com.radicalninja.speedtestpowercycler.data.OnError;
import com.radicalninja.speedtestpowercycler.data.OnProgress;
import com.radicalninja.speedtestpowercycler.data.OnStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SpeedtestSocket {

    public interface UpdateListener {
        void onProgress(final OnProgress data, final JSONObject raw);
        void onStatus(final OnStatus data, final JSONObject raw);
        void onComplete(final OnComplete data, final JSONObject raw);
        void onError(final OnError data, final JSONObject raw);
        void onConfirm(final OnConfirm data, final JSONObject raw);
    }

    public interface EventListener {
        void onReady();
        void onAttached(@Nullable final OnProgress progress, @Nullable final OnStatus status);
        void onFinished();
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
    private final UpdateListener updateListener = new UpdateListenerImpl();
    private final List<UpdateListener> updateListeners = Collections.synchronizedList(new ArrayList<UpdateListener>());
    private final EventListener eventListener = new EventListenerImpl();
    private final List<EventListener> eventListeners = Collections.synchronizedList(new ArrayList<EventListener>());

    private OnProgress lastProgress;
    private OnStatus lastStatus;

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
        socket.on(EVENT_FINISHED, onEventFinished);
    }

    public void addUpdateListener(final UpdateListener listener) {
        updateListeners.add(listener);
    }

    public void removeUpdateListener(final UpdateListener listener) {
        updateListeners.remove(listener);
    }

    public void addEventListener(final EventListener listener) {
        eventListeners.add(listener);
    }

    public void removeEventListener(final EventListener listener) {
        eventListeners.add(listener);
    }

    protected void handleReady(final JSONObject data) {
        // TODO: Pass data to listener?
        eventListener.onReady();
    }

    protected void handleUpdate(final String name, final JSONObject data) {
        switch (name) {
            case UPDATE_PROGRESS:
                final OnProgress progress = new OnProgress(data);
                updateListener.onProgress(progress, data);
                break;
            case UPDATE_STATUS:
                final OnStatus status = new OnStatus(data);
                updateListener.onStatus(status, data);
                break;
            case UPDATE_COMPLETE:
                final OnComplete complete = new OnComplete(data);
                updateListener.onComplete(complete, data);
                break;
            case UPDATE_ERROR:
                final OnError error = new OnError(data);
                updateListener.onError(error, data);
                break;
            case UPDATE_CONFIRM:
                final OnConfirm confirm = new OnConfirm(data);
                updateListener.onConfirm(confirm, data);
                break;
        }
    }

    protected void handleFinished(final JSONObject data) {
        // TODO: Pass data to listener?
        eventListener.onFinished();
    }

    private final Emitter.Listener onEventReady = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            final JSONObject json = (JSONObject) args[0];
            // TODO: Set object as ready to proceed
            handleReady(json);
        }
    };

    private final Emitter.Listener onEventUpdate = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                final JSONObject json = (JSONObject) args[0];
                final String key = json.getString(NAME_KEY);
                final JSONObject data = json.getJSONObject(NAME_DATA);
                handleUpdate(key, data);
                // TODO: Debug log?
            } catch (JSONException | ClassCastException e) {
                // TODO: HANDLE PARSING ERROR
                e.printStackTrace();
            }
        }
    };

    private final Emitter.Listener onEventFinished = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            final JSONObject json = (JSONObject) args[0];
            // TODO: Set object to stopped
            handleFinished(json);
        }
    };

    // TODO: Any way to cut down on duplicate code here? Lambda requires AS3.0
    private class UpdateListenerImpl implements UpdateListener {
        @Override
        public void onProgress(OnProgress data, JSONObject raw) {
            synchronized (updateListeners) {
                lastProgress = data;
                for (final UpdateListener listener : updateListeners) {
                    listener.onProgress(data, raw);
                }
            }
        }

        @Override
        public void onStatus(OnStatus data, JSONObject raw) {
            synchronized (updateListeners) {
                lastStatus = data;
                for (final UpdateListener listener : updateListeners) {
                    listener.onStatus(data, raw);
                }
            }
        }

        @Override
        public void onComplete(OnComplete data, JSONObject raw) {
            synchronized (updateListeners) {
                for (final UpdateListener listener : updateListeners) {
                    listener.onComplete(data, raw);
                }
            }
        }

        @Override
        public void onError(OnError data, JSONObject raw) {
            synchronized (updateListeners) {
                for (final UpdateListener listener : updateListeners) {
                    listener.onError(data, raw);
                }
            }
        }

        @Override
        public void onConfirm(OnConfirm data, JSONObject raw) {
            synchronized (updateListeners) {
                for (final UpdateListener listener : updateListeners) {
                    listener.onConfirm(data, raw);
                }
            }
        }
    }

    private class EventListenerImpl implements EventListener {
        @Override
        public void onReady() {
            synchronized (eventListeners) {
                for (final EventListener listener : eventListeners) {
                    listener.onReady();
                }
            }
        }

        @Override
        public void onAttached(@Nullable final OnProgress progress, @Nullable final OnStatus status) {
            synchronized (eventListeners) {
                if (null == progress && null == status) {
                    return;
                }
                for (final EventListener listener : eventListeners) {
                    listener.onAttached(progress, status);
                }
            }
        }

        @Override
        public void onFinished() {
            synchronized (eventListeners) {
                for (final EventListener listener : eventListeners) {
                    listener.onFinished();
                }
            }
        }
    }

}
