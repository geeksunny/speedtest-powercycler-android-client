package com.radicalninja.speedtestpowercycler.ui.options;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.radicalninja.speedtestpowercycler.R;
import com.radicalninja.speedtestpowercycler.data.Options;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.ViewHolder> {

    private static final int VIEW_TYPE_TITLE = 0;
    private static final int VIEW_TYPE_BOOL = 1;
    private static final int VIEW_TYPE_NUM = 2;
    private static final int VIEW_TYPE_STR = 3;

    private final LayoutInflater inflater;
    private final List<Options.Item> items = Collections.synchronizedList(new ArrayList<Options.Item>());

    public OptionsAdapter(final Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setOptions(final Options options) {
        synchronized (items) {
            items.clear();
            for (final Map.Entry<String, List<Options.Item>> entry : options.getItems().entrySet()) {
                final TitleItem title = new TitleItem(entry.getKey());
                items.add(title);
                items.addAll(entry.getValue());
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemViewType(int position) {
        final Options.Item item = items.get(position);
        if (item instanceof TitleItem) {
            return VIEW_TYPE_TITLE;
        }
        final Class type = item.getType();
        if (type.equals(Boolean.class)) {
            return VIEW_TYPE_BOOL;
        } else if (type.equals(Integer.class)) {
            return VIEW_TYPE_NUM;
        } else if (type.equals(String.class)) {
            return VIEW_TYPE_STR;
        }
        return -1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view;
        final ViewHolder vh;
        switch (viewType) {
            case VIEW_TYPE_TITLE:
                view = inflater.inflate(R.layout.options_item_title, parent, false);
                vh = new TitleViewHolder(view);
                break;
            case VIEW_TYPE_BOOL:
                view = inflater.inflate(R.layout.options_item_boolean, parent, false);
                vh = new BooleanViewHolder(view);
                break;
            case VIEW_TYPE_NUM:
                view = inflater.inflate(R.layout.options_item_number, parent, false);
                vh = new TextViewHolder(view);
                break;
            case VIEW_TYPE_STR:
                view = inflater.inflate(R.layout.options_item_string, parent, false);
                vh = new TextViewHolder(view);
                break;
            default:
                return null;
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Options.Item item = items.get(position);
        holder.bindOption(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    abstract class ViewHolder<T extends View> extends RecyclerView.ViewHolder {

        final T value;
        final TextView name, desc;

        Options.Item optionItem;

        ViewHolder(final View itemView) {
            super(itemView);
            value = itemView.findViewById(R.id.option_value);
            name = itemView.findViewById(R.id.option_name);
            desc = itemView.findViewById(R.id.option_description);
        }

        void bindOption(final Options.Item item) {
            this.optionItem = item;
            if (null != name) {
                name.setText(item.getName());
            }
            if (null != desc) {
                desc.setText(item.getDescription());
            }
            setup(item);
        }

        abstract void setup(final Options.Item item);
        abstract void onUpdate();

    }

    class BooleanViewHolder extends ViewHolder<CheckBox> {

        BooleanViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        void setup(final Options.Item item) {
            value.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    //
                }
            });
        }

        @Override
        void onUpdate() {

        }

    }

    class TextViewHolder extends ViewHolder<TextView> {

        TextViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        void setup(final Options.Item item) {

        }

        @Override
        void onUpdate() {

        }

    }

    class TitleViewHolder extends ViewHolder<View> {

        TitleViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        void setup(final Options.Item item) {
            name.setText(((TitleItem) item).getTitle());
        }

        @Override
        void onUpdate() {
            //
        }

    }

    private class TitleItem extends Options.Item<Void> {

        private final String title;

        TitleItem(final String title) {
            super(null, Void.class);
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

    }

}
