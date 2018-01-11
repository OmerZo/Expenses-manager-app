package com.learn.zohky.expensemanager.Dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.TimePicker;

import com.learn.zohky.expensemanager.Activities.ExpenseSubmitActivity;
import com.learn.zohky.expensemanager.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TimePickerDialog extends DialogFragment implements android.app.TimePickerDialog.OnTimeSetListener {

    private static android.app.TimePickerDialog pickerDialog = null;
    private ExpenseSubmitActivity activity;

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        Button bTime = (Button) activity.findViewById(R.id.bTime);
        //make the Time as string
        String str = String.format("%d:%d", i, i1);
        //define pattern
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        try {
            //convert to object Date by the pattern
            Date d = simpleDateFormat.parse(str);
            //convert back to string from the object Date
            String str1 = simpleDateFormat.format(d);
            bTime.setText(str1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        activity = (ExpenseSubmitActivity)getActivity();
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        if (pickerDialog == null) {
            pickerDialog = new android.app.TimePickerDialog(getActivity(), android.R.style.Theme_DeviceDefault_Dialog_Alert, this, hour, minute, true);
            return pickerDialog;
        }
        return pickerDialog;
    }


}
