package com.radicalninja.speedtestpowercycler.ui.options;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.radicalninja.speedtestpowercycler.data.Options;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.ViewHolder> {

    private final Map<String, List<Options.Item>> items = new HashMap<>();

    public void setOptions(final Options options) {
        items.clear();
        items.putAll(options.getItems());
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        int count = 0;
        for (final Map.Entry<String, List<Options.Item>> entry : items.entrySet()) {
            count += entry.getValue().size();
        }
        return count;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

    }

}
