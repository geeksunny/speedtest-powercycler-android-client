package com.radicalninja.speedtestpowercycler.ui.options;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.radicalninja.speedtestpowercycler.R;

public class OptionsFragment extends Fragment {

    private final OptionsAdapter adapter = new OptionsAdapter();

    private RecyclerView view;

    public static OptionsFragment newInstance() {
        return new OptionsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = (RecyclerView) inflater.inflate(R.layout.fragment_options, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        this.view.setAdapter(adapter);
    }
}
