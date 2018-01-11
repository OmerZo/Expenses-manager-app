package com.learn.zohky.expensemanager.Dialogs;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.learn.zohky.expensemanager.Activities.LoginActivity;
import com.learn.zohky.expensemanager.Activities.SettingsActivity;
import com.learn.zohky.expensemanager.DAOs.CurrenciesDAO;
import com.learn.zohky.expensemanager.Handler.CurrenciesHandler;
import com.learn.zohky.expensemanager.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ChangeBaseDialog extends Dialog implements View.OnClickListener {

    public ChangeBaseDialog(@NonNull Context context) {
        super(context);
    }

    public ChangeBaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_base_dialog);

        SharedPreferences prefs = getContext().getSharedPreferences(LoginActivity.PREF_NAME, MODE_PRIVATE);
        CurrenciesHandler currenciesHandler = new CurrenciesHandler(getContext());
        List<CurrenciesDAO> currencies = (List<CurrenciesDAO>) currenciesHandler.getAll();
        Spinner currSpinner = (Spinner) findViewById(R.id.defCurrSpinner);
        ArrayAdapter<CurrenciesDAO> adapter =
                new ArrayAdapter<CurrenciesDAO>(getContext(), R.layout.spinner_item, R.id.text, currencies);
        currSpinner.setAdapter(adapter);
        currSpinner.setSelection(prefs.getInt(SettingsActivity.SPINNER_POS, 0));
        Button bSave = (Button)findViewById(R.id.bSaveBase);
        bSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Spinner currSpinner = (Spinner) findViewById(R.id.defCurrSpinner);
        SharedPreferences prefs = getContext().getSharedPreferences(LoginActivity.PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(SettingsActivity.SPINNER_POS, currSpinner.getSelectedItemPosition());
        editor.putString(SettingsActivity.DEF_BASE, currSpinner.getSelectedItem().toString());
        Toast.makeText(getContext(), "The changes have been saved!", Toast.LENGTH_SHORT).show();
        editor.commit();
        this.dismiss();
    }
}
