package com.radicalninja.speedtestpowercycler.ui.speedtest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.radicalninja.speedtestpowercycler.App;
import com.radicalninja.speedtestpowercycler.R;
import com.radicalninja.speedtestpowercycler.SpeedtestSocket;
import com.radicalninja.speedtestpowercycler.data.OnComplete;
import com.radicalninja.speedtestpowercycler.data.OnConfirm;
import com.radicalninja.speedtestpowercycler.data.OnError;
import com.radicalninja.speedtestpowercycler.data.OnProgress;
import com.radicalninja.speedtestpowercycler.data.OnStatus;
import com.radicalninja.speedtestpowercycler.ui.view.TextValueView;

import org.json.JSONObject;

public class SpeedtestFragment extends Fragment implements View.OnClickListener {

    private final SocketListener socketListener = new SocketListener();

    private TextView statusLabel;
    private Button startStopButton;
    private TextValueView pingView, downloadView, uploadView, progressView;

    public static SpeedtestFragment newInstance() {
        return new SpeedtestFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_speedtest, container, false);

        statusLabel = view.findViewById(R.id.label_status);
        startStopButton = view.findViewById(R.id.button_start_stop);
        startStopButton.setOnClickListener(this);

        pingView = view.findViewById(R.id.value_ping);
        downloadView = view.findViewById(R.id.value_download);
        uploadView = view.findViewById(R.id.value_upload);
        progressView = view.findViewById(R.id.value_progress);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //
    }

    @Override
    public void onStart() {
        super.onStart();
        App.getInstance().getSocket().addEventListener(socketListener);
        App.getInstance().getSocket().addUpdateListener(socketListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        App.getInstance().getSocket().removeEventListener(socketListener);
        App.getInstance().getSocket().removeUpdateListener(socketListener);
    }

    @Override
    public void onClick(View view) {
        // TODO: Start/stop the test
        final SpeedtestSocket socket = App.getInstance().getSocket();
        if (!socket.isReady()) {
            // not ready yet
            // TODO: Alert the user
            Toast.makeText(getContext(), "Socket not ready", Toast.LENGTH_SHORT).show();
        } else if (!socket.isRunning()) {
            Toast.makeText(getContext(), "Starting test", Toast.LENGTH_SHORT).show();
            socket.startTest();
        } else {
            Toast.makeText(getContext(), "Stopping test", Toast.LENGTH_SHORT).show();
            socket.stopTest();
        }
    }

    private void setReady(final boolean ready) {
        startStopButton.setEnabled(ready);
        statusLabel.setText(ready ? R.string.speedtest_status_ready : R.string.speedtest_status_not_ready);
    }

    private class SocketListener implements SpeedtestSocket.EventListener, SpeedtestSocket.UpdateListener {

        @SuppressLint("DefaultLocale")
        String prepareSpeed(final double speed) {
            return String.format("%.2f", speed);
        }

        @Override
        public void onReady() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setReady(true);
                }
            });
        }

        @Override
        public void onFinished() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setReady(false);
                }
            });
        }

        @Override
        public void onAttached(@Nullable final OnProgress progress, @Nullable final OnStatus status) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (null != progress) {
                        progressView.setValue(String.valueOf(progress.getProgress()));
                    }
                    if (null != status) {
                        switch (status.getDirection()) {
                            case UPLOAD:
                                uploadView.setValue(prepareSpeed(status.getUp()));
                            case DOWNLOAD:
                                downloadView.setValue(prepareSpeed(status.getDown()));
                            case PING:
                                pingView.setValue(String.valueOf(status.getPing()));
                        }
                    }
                    // TODO: Ensure the button state is correct
                }
            });
        }

        @Override
        public void onProgress(OnProgress data, JSONObject raw) {
            progressView.setValue(String.valueOf(data.getProgress()));
        }

        @Override
        public void onStatus(OnStatus data, JSONObject raw) {
            switch (data.getDirection()) {
                case PING:
                    pingView.setValue(String.valueOf(data.getPing()));
                    break;
                case DOWNLOAD:
                    downloadView.setValue(prepareSpeed(data.getDown()));
                    break;
                case UPLOAD:
                    downloadView.setValue(prepareSpeed(data.getUp()));
                    break;
            }
        }

        @Override
        public void onComplete(OnComplete data, JSONObject raw) {
            //
        }

        @Override
        public void onError(OnError data, JSONObject raw) {
            // TODO: Show error message
        }

        @Override
        public void onConfirm(OnConfirm data, JSONObject raw) {
            //
        }
    }
}
