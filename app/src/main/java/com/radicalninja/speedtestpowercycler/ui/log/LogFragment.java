package com.radicalninja.speedtestpowercycler.ui.log;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.radicalninja.speedtestpowercycler.App;
import com.radicalninja.speedtestpowercycler.R;
import com.radicalninja.speedtestpowercycler.SpeedtestSocket;
import com.radicalninja.speedtestpowercycler.data.Event;
import com.radicalninja.speedtestpowercycler.ui.NavigationFragment;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class LogFragment extends NavigationFragment {

    private static final String LOG_FORMAT = "%s | %s :: %s\n";

    private final SocketListener socketListener = new SocketListener();
    private final DateFormat dateFormat = DateFormat.getDateTimeInstance();

    private TextView logView;

    public static LogFragment newInstance() {
        return new LogFragment();
    }

    @IdRes
    @Override
    public int getNavId() {
        return R.id.navigation_log;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_log, container, false);
        logView = view.findViewById(R.id.output_log);
        logView.setMovementMethod(new ScrollingMovementMethod());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        populateLog();
    }

    @Override
    public void onResume() {
        super.onResume();
        App.getInstance().getSocket().addEventLogListener(socketListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        App.getInstance().getSocket().removeEventLogListener(socketListener);
    }

    private String prepareLogLine(final Event event) {
        return String.format(LOG_FORMAT,
                dateFormat.format(new Date()), event.getName(), event.getJson());
    }

    private void log(final Event event) {
        logView.append(prepareLogLine(event));
    }

    private void populateLog() {
        final List<Event> history = App.getInstance().getSocket().getEventHistory();
        if (history.isEmpty()) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        for (final Event event : history) {
            sb.append(prepareLogLine(event));
        }
        logView.setText(sb.toString());
    }

    private class SocketListener implements SpeedtestSocket.EventLogListener {
        @Override
        public void onHistoryReset() {
            logView.setText("");
        }

        @Override
        public void onEventLogged(Event event) {
            log(event);
        }
    }

}
