package com.radicalninja.speedtestpowercycler;

import android.app.Application;

import java.net.URISyntaxException;

public class App extends Application {

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    private SpeedtestSocket socket;

    public App() {
        super();
        instance = this;
    }

    public void initSocket() {
        // TODO: Add options to this
        if (null != socket) {
            return;
        }
        socket = SpeedtestSocket.create(BuildConfig.DEFAULT_HOSTNAME);
        socket.connect();
    }

    public SpeedtestSocket getSocket() {
        return socket;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
