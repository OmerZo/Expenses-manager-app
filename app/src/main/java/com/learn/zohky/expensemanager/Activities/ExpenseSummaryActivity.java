package com.learn.zohky.expensemanager.Activities;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.learn.zohky.expensemanager.Handler.ExpensesHandler;
import com.learn.zohky.expensemanager.R;
import com.learn.zohky.expensemanager.Adapters.SummaryArrayAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ExpenseSummaryActivity extends AppCompatActivity {

    private Spinner monthSpinner,yearSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_summary);
        monthSpinner = (Spinner) findViewById(R.id.monthSpinner);
        yearSpinner = (Spinner) findViewById(R.id.yearSpinner);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillDateSpinners();
        selectSummaryDate();
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
        } else if (itemId == R.id.action_report) {
            Intent intent = new Intent(this, ExpenseReportActivity.class);
            startActivity(intent);
        } else if (itemId == R.id.action_setting) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * fill the spinners with all the months and back to 12 years
     */
    private void fillDateSpinners() {
        ArrayList<String> monthArr = new ArrayList<String>();
        ArrayList<String> yearArr = new ArrayList<String>();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 1; i <= 12; i++) {
            monthArr.add(String.format("%02d", i));
            yearArr.add(String.valueOf((year - i) + 1));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, R.id.text, monthArr);
        monthSpinner.setAdapter(adapter);
        adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, R.id.text, yearArr);
        yearSpinner.setAdapter(adapter);
    }

    /**
     * Choose which date to receive a summary.
     * Whether from inserted date or from current date.
     */
    private void selectSummaryDate() {
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            Date insertedDate = (Date) extras.get("insertedDate");
            setInsertedDate(insertedDate);
        } else {
            setCurrentDate();
        }
        getSummaryByDate((String) monthSpinner.getSelectedItem(), (String) yearSpinner.getSelectedItem());
    }

    /**
     * Set the spinners with the inserted date from the intent.
     * @param selectedDate Inserted date from the intent.
     */
    private void setInsertedDate(Date selectedDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String selectedDateStr = dateFormat.format(selectedDate);
        String[] dateArr = selectedDateStr.split("/");
        String[] yearArr = dateArr[2].split(" ");

        int year = Calendar.getInstance().get(Calendar.YEAR);
        if(Integer.valueOf(yearArr[0]) > (year - 12)){
            monthSpinner.setSelection(getIndex(monthSpinner, dateArr[1]));
            yearSpinner.setSelection(getIndex(yearSpinner, yearArr[0]));
        }
    }

    /**
     * Set the spinners with the current date.
     */
    private void setCurrentDate() {
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        monthSpinner.setSelection(getIndex(monthSpinner, String.valueOf(month)));
        yearSpinner.setSelection(getIndex(yearSpinner, String.valueOf(year)));
    }


    /**
     * Get the summary of the received date.
     * @param month The month to get the summary by.
     * @param year The year to get the summary by.
     */
    private void getSummaryByDate(String month, String year) {
        ExpensesHandler expensesHandler = new ExpensesHandler(this);
        List<String> summaries = (List<String>) expensesHandler.getSumByCat(month, year);
        SummaryArrayAdapter summaryArrayAdapter = new SummaryArrayAdapter(this, summaries);
        ListView lvSummary = (ListView) findViewById(R.id.lvSummary);
        lvSummary.setAdapter(summaryArrayAdapter);
        calcTotalAmount(summaries);
    }

    /**
     * Calculate the total amount of the entire month.
     * @param summaries A list with all the expenses of the selected month.
     */
    private void calcTotalAmount(List<String> summaries) {
        float totalAmount = 0;
        for (int i = 0; i < summaries.size(); i++) {
            String str = summaries.get(i);
            String[] arr = str.split(" ", 2);
            totalAmount += Float.valueOf(arr[0]);
        }
        TextView tvSumSum = (TextView) findViewById(R.id.tvSumSum);
        tvSumSum.setText(String.valueOf(totalAmount));
    }

    public void selectClicked(View view) {
        getSummaryByDate((String) monthSpinner.getSelectedItem(), (String) yearSpinner.getSelectedItem());
    }

    /**
     * Get the index of the string in the spinner.
     * @param spinner The spinner to search in.
     * @param search The string to search for.
     * @return The index of the desired string in the spinner.
     */
    private int getIndex(Spinner spinner, String search) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(search)) {
                index = i;
            }
        }
        return index;
    }
}
