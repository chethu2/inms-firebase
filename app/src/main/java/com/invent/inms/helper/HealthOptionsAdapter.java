package com.invent.inms.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import inms.invent.com.i_invent_inms.R;

public class HealthOptionsAdapter extends BaseAdapter {
    String[] names;
    Context context;
    LayoutInflater inflter;
    String value;

    public HealthOptionsAdapter(Context context, String[] names) {
        this.context = context;
        this.names = names;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = inflter.inflate(R.layout.health_option, null);
        final CheckedTextView simpleCheckedTextView = (CheckedTextView) view.findViewById(R.id.HealthOptionCheckedText);
        simpleCheckedTextView.setText(names[position]);
        simpleCheckedTextView.setCheckMarkDrawable(0);
        simpleCheckedTextView.setChecked(false);
        return view;
    }
}