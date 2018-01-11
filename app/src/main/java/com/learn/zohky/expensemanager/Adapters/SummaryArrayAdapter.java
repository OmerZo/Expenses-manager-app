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

public class SummaryArrayAdapter extends ArrayAdapter<String>{

    private List<String> summaries;

    public SummaryArrayAdapter(@NonNull Context context, List<String> summaries) {
        super(context, 0, summaries);
        this.summaries = summaries;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.summary_item, parent, false);
        TextView tvCategory = (TextView)convertView.findViewById(R.id.tvSumCategory);
        TextView tvTotalAmount = (TextView)convertView.findViewById(R.id.tvSumTotalAmount);
        String [] arr = summaries.get(position).split(" ", 2);
        tvTotalAmount.setText(arr[0]);
        tvCategory.setText(arr[1]);
        return convertView;
    }
}
