package com.learn.zohky.expensemanager.Adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.learn.zohky.expensemanager.DAOs.ExpenseDAO;
import com.learn.zohky.expensemanager.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReportArrayAdapter extends ArrayAdapter<ExpenseDAO>{

    private List<ExpenseDAO> expenses;

    public ReportArrayAdapter(@NonNull Context context, List<ExpenseDAO> expenses) {
        super(context, 0, expenses);
        this.expenses = expenses;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.report_item, parent, false);
        TextView tvRepDate = convertView.findViewById(R.id.tvRepDate);
        TextView tvRepAmount = convertView.findViewById(R.id.tvRepAmount);
        TextView tvRepDesc = convertView.findViewById(R.id.tvRepDesc);
        ExpenseDAO expenseDAO = expenses.get(position);
        Date datetime = expenseDAO.getDatetime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String strDateTime = simpleDateFormat.format(datetime);
        String[] dateArr = strDateTime.split(" ");
        tvRepDate.setText(String.format("%s\n%s", dateArr[0], dateArr[1]));
        tvRepAmount.setText(String.valueOf(expenseDAO.getAmount()));
        String desc = expenseDAO.getDescription();
        if(desc.length()>20) {
            String subDesc = expenseDAO.getDescription().substring(0, 20);
            subDesc += "...";
            desc = subDesc;
        }
        tvRepDesc.setText(desc);
        return convertView;
    }
}
