package com.radicalninja.speedtestpowercycler.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.radicalninja.speedtestpowercycler.R;

public class TextValueView extends FrameLayout {

    private TextView labelView, valueView, suffixView;
    private String value = "", suffix = "";
    private boolean separateSuffix;

    public TextValueView(@NonNull Context context) {
        super(context);
        init();
    }

    public TextValueView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        parseAttrs(attrs);
    }

    public TextValueView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        parseAttrs(attrs);
    }

    public TextValueView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
        parseAttrs(attrs);
    }

    protected void init() {
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        final View layout = inflater.inflate(R.layout.view_text_value, this, true);
        labelView = layout.findViewById(R.id.label);
        valueView = layout.findViewById(R.id.value);
        suffixView = layout.findViewById(R.id.suffix);
    }

    protected void parseAttrs(@Nullable final AttributeSet attrs) {
        if (null == attrs) {
            return;
        }
        final TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TextValueView,
                0, 0
        );
        try {
            final boolean aSeparateSuffix = a.getBoolean(R.styleable.TextValueView_separate_suffix, false);
            setSeparateSuffix(aSeparateSuffix);
            final String aLabel = a.getString(R.styleable.TextValueView_label);
            setLabel(aLabel);
            final String aValue = a.getString(R.styleable.TextValueView_value);
            final String aSuffix = a.getString(R.styleable.TextValueView_suffix);
            setValue(aValue, aSuffix);
        } finally {
            a.recycle();
        }
    }

    public void refreshValue() {
        if (separateSuffix) {
            valueView.setText(value);
            suffixView.setText(suffix);
        } else {
            final String valueString = String.format("%s%s", value, suffix);
            valueView.setText(valueString);
        }
    }

    public void setSeparateSuffix(final boolean shouldSeparate) {
        separateSuffix = shouldSeparate;
        //suffixView.setVisibility(shouldSeparate ? VISIBLE : GONE);
        refreshValue();
    }

    public void setLabel(final String label) {
        if (null != label) {
            labelView.setText(label);
        }
    }

    public void setValue(final String value, final String suffix) {
        if (null != value) {
            this.value = value;
        }
        if (null != suffix) {
            this.suffix = suffix;
        }
        refreshValue();
    }

    public void setValue(final String value) {
        if (null != value) {
            this.value = value;
            refreshValue();
        }
    }

    public void setSuffix(final String suffix) {
        if (null != suffix) {
            this.suffix = suffix;
            refreshValue();
        }
    }

}
