package com.learn.zohky.expensemanager.Activities;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.*;
import android.widget.*;

import com.learn.zohky.expensemanager.Adapters.SettingsArrayAdapter;
import com.learn.zohky.expensemanager.Dialogs.ChangeBaseDialog;
import com.learn.zohky.expensemanager.Dialogs.ChangePassDialog;
import com.learn.zohky.expensemanager.Dialogs.EditCategoryDialog;
import com.learn.zohky.expensemanager.R;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static final String SPINNER_POS = "basePos";
    public static final String DEF_BASE = "baseVal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initSettings();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mMenuInflater = getMenuInflater();
        mMenuInflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_submit) {
            Intent intent = new Intent(this, ExpenseSubmitActivity.class);
            startActivity(intent);
        } else if (itemId == R.id.action_summary) {
            Intent intent = new Intent(this, ExpenseSummaryActivity.class);
            startActivity(intent);
        } else if (itemId == R.id.action_report) {
            Intent intent = new Intent(this, ExpenseReportActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Initializing the settings.
     */
    private void initSettings() {
        ArrayList<String> settings = new ArrayList<String>();
        settings.add("Edit Category");
        settings.add("Change Base");
        settings.add("Change Password");
        SettingsArrayAdapter settingsArrayAdapter = new SettingsArrayAdapter(this, settings);
        ListView lvSettings = (ListView)findViewById(R.id.lvSettings);
        lvSettings.setAdapter(settingsArrayAdapter);
        lvSettings.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Dialog dialog = null;
        switch (position){
            case 0:
                dialog = new EditCategoryDialog(this, android.R.style.Theme_DeviceDefault_Dialog_Alert);
                break;
            case 1:
                dialog = new ChangeBaseDialog(this, android.R.style.Theme_DeviceDefault_Dialog_Alert);
                break;
            case 2:
                dialog = new ChangePassDialog(this, android.R.style.Theme_DeviceDefault_Dialog_Alert);
                break;
        }
        dialog.show();
    }
}
