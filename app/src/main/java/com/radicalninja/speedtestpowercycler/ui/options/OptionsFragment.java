package com.radicalninja.speedtestpowercycler.ui.options;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.radicalninja.speedtestpowercycler.App;
import com.radicalninja.speedtestpowercycler.R;
import com.radicalninja.speedtestpowercycler.SpeedtestSocket;
import com.radicalninja.speedtestpowercycler.data.OnProgress;
import com.radicalninja.speedtestpowercycler.data.OnStatus;
import com.radicalninja.speedtestpowercycler.data.Options;

public class OptionsFragment extends Fragment {

    private final EventListener eventListener = new EventListener();

    private OptionsAdapter adapter;

    private RecyclerView view;

    public static OptionsFragment newInstance() {
        return new OptionsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new OptionsAdapter(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        App.getInstance().getSocket().addEventListener(eventListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        App.getInstance().getSocket().removeEventListener(eventListener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = (RecyclerView) inflater.inflate(R.layout.fragment_options, container, false);
        view.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        this.view.setAdapter(adapter);
    }

    private class EventListener implements SpeedtestSocket.EventListener {

        @Override
        public void onReady(final Options options) {
            adapter.setOptions(options);
        }

        @Override
        public void onAttached(final Options options, @Nullable OnProgress progress,
                               @Nullable OnStatus status) {
            if (adapter.getItemCount() == 0) {
                adapter.setOptions(options);
            }
        }

        @Override
        public void onFinished() {

        }

    }

}
