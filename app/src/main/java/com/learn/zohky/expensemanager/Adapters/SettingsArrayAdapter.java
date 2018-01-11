package com.learn.zohky.expensemanager.Adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.learn.zohky.expensemanager.R;

import java.util.List;

public class SettingsArrayAdapter extends ArrayAdapter<String>{

    private List<String> settings;

    public SettingsArrayAdapter(@NonNull Context context, List<String> settings) {
        super(context, 0, settings);
        this.settings = settings;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.setting_item, parent, false);
        TextView tvSettName = (TextView)convertView.findViewById(R.id.tvSettName);
        tvSettName.setText(settings.get(position));
        return convertView;
    }
}
