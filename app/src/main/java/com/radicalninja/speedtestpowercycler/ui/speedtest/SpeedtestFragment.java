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

    private boolean ready;

    private TextView statusLabel;
    private Button startStopButton;
    private TextValueView pingView, downloadView, uploadView, progressView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_speedtest, container, false);

        statusLabel = view.findViewById(R.id.label_status);
        startStopButton = view.findViewById(R.id.button_start_stop);

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
        //
    }

    @Override
    public void onPause() {
        super.onPause();
        //
    }

    @Override
    public void onClick(View view) {
        // TODO: Start/stop the test
    }

    private void setReady(final boolean ready) {
        if (this.ready == ready) {
            return;
        }
        this.ready = ready;
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
            setReady(true);
        }

        @Override
        public void onFinished() {
            setReady(false);
        }

        @Override
        public void onAttached(@Nullable OnProgress progress, @Nullable OnStatus status) {
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
