package com.radicalninja.speedtestpowercycler;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.radicalninja.speedtestpowercycler.data.OnComplete;
import com.radicalninja.speedtestpowercycler.data.OnConfirm;
import com.radicalninja.speedtestpowercycler.data.OnError;
import com.radicalninja.speedtestpowercycler.data.OnProgress;
import com.radicalninja.speedtestpowercycler.data.OnStatus;
import com.radicalninja.speedtestpowercycler.data.Options;

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
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final UpdateListener updateListener = new UpdateListenerImpl();
    private final List<UpdateListener> updateListeners = Collections.synchronizedList(new ArrayList<UpdateListener>());
    private final EventListener eventListener = new EventListenerImpl();
    private final List<EventListener> eventListeners = Collections.synchronizedList(new ArrayList<EventListener>());

    private boolean ready, running;
    private OnProgress lastProgress;
    private OnStatus lastStatus;
    private Options options;

    public static SpeedtestSocket create(final String url) {
        try {
            return new SpeedtestSocket(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public SpeedtestSocket(final String url) throws URISyntaxException {
        socket = IO.socket(url);    // TODO: Implement socket disconnect code
        init();
    }

    protected void init() {
        socket.on(EVENT_READY, onEventReady);
        socket.on(EVENT_UPDATE, onEventUpdate);
        socket.on(EVENT_FINISHED, onEventFinished);
    }

    public void connect() {
        if (!socket.connected()) {
            socket.connect();
        }
    }

    public void disconnect() {
        if (socket.connected()) {
            socket.disconnect();
        }
    }

    public void startTest() {
        if (running) {
            return;
        }
        socket.emit("start", true);
        running = true;
    }

    public void stopTest() {
        if (!running) {
            return;
        }
        socket.emit("stop");
        //handleFinished(null);   // TODO: Is this necessary? "finish" should trigger when the test is successfully stopped.
    }

    public void reset() {
        lastProgress = null;
        lastStatus = null;
    }

    public boolean isReady() {
        return ready;
    }

    public boolean isRunning() {
        return running;
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

    protected void handleReady() {
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
            ready = true;
            final JSONObject json = (args.length > 0) ? (JSONObject) args[0] : null;
            options = new Options(json);
            handleReady();
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
            running = true;
            final JSONObject json = (args.length > 0) ? (JSONObject) args[0] : null;
            handleFinished(json);
        }
    };

    // TODO: Any way to cut down on duplicate code here? Lambda requires AS3.0
    private class UpdateListenerImpl implements UpdateListener {
        @Override
        public void onProgress(final OnProgress data, final JSONObject raw) {
            lastProgress = data;
            final Runnable task = new Runnable() {
                @Override
                public void run() {
                    synchronized (updateListeners) {
                        for (final UpdateListener listener : updateListeners) {
                            listener.onProgress(data, raw);
                        }
                    }
                }
            };
            handler.post(task);
        }

        @Override
        public void onStatus(final OnStatus data, final JSONObject raw) {
            lastStatus = data;
            final Runnable task = new Runnable() {
                @Override
                public void run() {
                    synchronized (updateListeners) {
                        for (final UpdateListener listener : updateListeners) {
                            listener.onStatus(data, raw);
                        }
                    }
                }
            };
            handler.post(task);
        }

        @Override
        public void onComplete(final OnComplete data, final JSONObject raw) {
            final Runnable task = new Runnable() {
                @Override
                public void run() {
                    synchronized (updateListeners) {
                        for (final UpdateListener listener : updateListeners) {
                            listener.onComplete(data, raw);
                        }
                    }
                }
            };
            handler.post(task);
        }

        @Override
        public void onError(final OnError data, final JSONObject raw) {
            final Runnable task = new Runnable() {
                @Override
                public void run() {
                    synchronized (updateListeners) {
                        for (final UpdateListener listener : updateListeners) {
                            listener.onError(data, raw);
                        }
                    }
                }
            };
            handler.post(task);
        }

        @Override
        public void onConfirm(final OnConfirm data, final JSONObject raw) {
            final Runnable task = new Runnable() {
                @Override
                public void run() {
                    synchronized (updateListeners) {
                        for (final UpdateListener listener : updateListeners) {
                            listener.onConfirm(data, raw);
                        }
                    }
                }
            };
            handler.post(task);
        }
    }

    private class EventListenerImpl implements EventListener {
        @Override
        public void onReady() {
            final Runnable task = new Runnable() {
                @Override
                public void run() {
                    synchronized (eventListeners) {
                        for (final EventListener listener : eventListeners) {
                            listener.onReady();
                        }
                    }
                }
            };
            handler.post(task);
        }

        @Override
        public void onAttached(@Nullable final OnProgress progress, @Nullable final OnStatus status) {
            if (null == progress && null == status) {
                return;
            }
            final Runnable task = new Runnable() {
                @Override
                public void run() {
                    synchronized (eventListeners) {
                        for (final EventListener listener : eventListeners) {
                            listener.onAttached(progress, status);
                        }
                    }
                }
            };
            handler.post(task);
        }

        @Override
        public void onFinished() {
            final Runnable task = new Runnable() {
                @Override
                public void run() {
                    synchronized (eventListeners) {
                        for (final EventListener listener : eventListeners) {
                            listener.onFinished();
                        }
                    }
                }
            };
            handler.post(task);
        }
    }

}
