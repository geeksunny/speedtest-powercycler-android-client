package com.radicalninja.speedtestpowercycler.ui.log;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.radicalninja.speedtestpowercycler.App;
import com.radicalninja.speedtestpowercycler.R;
import com.radicalninja.speedtestpowercycler.SpeedtestSocket;
import com.radicalninja.speedtestpowercycler.data.OnComplete;
import com.radicalninja.speedtestpowercycler.data.OnConfirm;
import com.radicalninja.speedtestpowercycler.data.OnError;
import com.radicalninja.speedtestpowercycler.data.OnProgress;
import com.radicalninja.speedtestpowercycler.data.OnStatus;
import com.radicalninja.speedtestpowercycler.data.Options;

import org.json.JSONObject;

public class LogFragment extends Fragment {

    private final SocketListener socketListener = new SocketListener();

    private TextView logView;

    public static LogFragment newInstance() {
        return new LogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_log, container, false);
        logView = view.findViewById(R.id.output_log);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        App.getInstance().getSocket().addEventListener(socketListener);
        App.getInstance().getSocket().addUpdateListener(socketListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        App.getInstance().getSocket().removeEventListener(socketListener);
        App.getInstance().getSocket().removeUpdateListener(socketListener);
    }

    private class SocketListener implements SpeedtestSocket.EventListener, SpeedtestSocket.UpdateListener {

        @Override
        public void onProgress(OnProgress data, JSONObject raw) {

        }

        @Override
        public void onStatus(OnStatus data, JSONObject raw) {

        }

        @Override
        public void onComplete(OnComplete data, JSONObject raw) {

        }

        @Override
        public void onError(OnError data, JSONObject raw) {

        }

        @Override
        public void onConfirm(OnConfirm data, JSONObject raw) {

        }

        @Override
        public void onReady(Options options) {

        }

        @Override
        public void onAttached(Options options, @Nullable OnProgress progress, @Nullable OnStatus status) {

        }

        @Override
        public void onFinished() {

        }
    }

}
