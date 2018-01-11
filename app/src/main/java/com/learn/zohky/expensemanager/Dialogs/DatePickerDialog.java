package com.learn.zohky.expensemanager.Dialogs;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.DatePicker;



import com.learn.zohky.expensemanager.Activities.ExpenseSubmitActivity;
import com.learn.zohky.expensemanager.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatePickerDialog extends DialogFragment implements OnDateSetListener {

    private static android.app.DatePickerDialog pickerDialog = null;
    private ExpenseSubmitActivity activity;

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Button bDate = (Button) activity.findViewById(R.id.bDate);
        String str = String.format("%d/%d/%d", i2, i1+1, i);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date d = simpleDateFormat.parse(str);
            String str1 = simpleDateFormat.format(d);
            bDate.setText(str1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        activity = (ExpenseSubmitActivity)getActivity();
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        if (pickerDialog == null) {
            pickerDialog = new android.app.DatePickerDialog(getActivity(), android.R.style.Theme_DeviceDefault_Dialog_Alert, this, year, month, day);
            return pickerDialog;
        }
        return pickerDialog;
    }
}

