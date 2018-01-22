package com.learn.zohky.expensemanager.Activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.os.Build;
import android.provider.CalendarContract;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.learn.zohky.expensemanager.DAOs.CurrenciesDAO;
import com.learn.zohky.expensemanager.DAOs.ExpenseCategoryDAO;
import com.learn.zohky.expensemanager.DAOs.ExpenseDAO;
import com.learn.zohky.expensemanager.Dialogs.DatePickerDialog;
import com.learn.zohky.expensemanager.Dialogs.EditCategoryDialog;
import com.learn.zohky.expensemanager.Handler.CurrenciesHandler;
import com.learn.zohky.expensemanager.Handler.ExpenseCategoryHandler;
import com.learn.zohky.expensemanager.Handler.ExpensesHandler;
import com.learn.zohky.expensemanager.Handler.RatesHandler;
import com.learn.zohky.expensemanager.R;
import com.learn.zohky.expensemanager.Dialogs.TimePickerDialog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.widget.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExpenseSubmitActivity extends AppCompatActivity implements TextWatcher {
    EditText etAmount;
    List<CurrenciesDAO> currencies;
    SharedPreferences prefs;
    private static final String SPINNER_CURR_KEY = "currKey";
    private static final String SPINNER_CAT_KEY = "catKey";
    Spinner currSpinner, catSpinner;
    boolean isEmpty = true;

    @Override
    protected void onResume() {
        super.onResume();
        fillCatSpinner();
        refreshCalcAmount();
        ExpenseCategoryHandler expenseCategoryHandler = new ExpenseCategoryHandler(this);
        expenseCategoryHandler.deleteNotUsed();
    }

    /**
     * Fill the category spinner
     */
    private void fillCatSpinner() {
        ExpenseCategoryHandler expenseCategoryHandler = new ExpenseCategoryHandler(this);
        List<ExpenseCategoryDAO> categories = (List<ExpenseCategoryDAO>) expenseCategoryHandler.getAllNotDeleted();
        catSpinner = (Spinner) findViewById(R.id.categorySpinner);
        ArrayAdapter<ExpenseCategoryDAO> adapter =
                new ArrayAdapter<ExpenseCategoryDAO>(this, R.layout.spinner_item, R.id.text, categories);
        catSpinner.setAdapter(adapter);
        int prefsCat = prefs.getInt(SPINNER_CAT_KEY, 0);
        if(catSpinner.getCount() > prefsCat) {
            catSpinner.setSelection(prefsCat);
        }
    }


    private void refreshCalcAmount() {
        TextView tvRateName = (TextView)findViewById(R.id.tvRateName);
        tvRateName.setText(prefs.getString(SettingsActivity.DEF_BASE,"ILS"));
        String etAmountText = etAmount.getText().toString();
        etAmount.setText(etAmountText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_submit);
        etAmount = (EditText) findViewById(R.id.etAmount);
        etAmount.addTextChangedListener(this);
        prefs = getSharedPreferences(LoginActivity.PREF_NAME, MODE_PRIVATE);
        initDatetime();
//        if (expenseCategoryHandler.getAll().size() == 0) {
//            initDefCat(expenseCategoryHandler);
//        }
        fillCurrSpinner();

        currSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String etAmountText = etAmount.getText().toString();
                etAmount.setText(etAmountText);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    /**
     * Initializing the current datetime to the edit text
     */
    private void initDatetime() {
        Button bDate = (Button) findViewById(R.id.bDate);
        Button bTime = (Button) findViewById(R.id.bTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String currentDate = simpleDateFormat.format(new Date());
        String[] arr = currentDate.split(" ");
        bDate.setText(arr[0]);
        bTime.setText(arr[1]);
    }

    /**
     * fill the currencies spinner
     */
    private void fillCurrSpinner() {
        CurrenciesHandler currenciesHandler = new CurrenciesHandler(this);
        currencies = (List<CurrenciesDAO>) currenciesHandler.getAll();
        currSpinner = (Spinner) findViewById(R.id.currencySpinner);
        ArrayAdapter<CurrenciesDAO> adapter2 =
                new ArrayAdapter<CurrenciesDAO>(this, R.layout.spinner_item, R.id.text, currencies);
        currSpinner.setAdapter(adapter2);
        currSpinner.setSelection(prefs.getInt(SPINNER_CURR_KEY, 0));
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
        if (itemId == R.id.action_summary) {
            Intent intent = new Intent(this, ExpenseSummaryActivity.class);
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


    private void initDefCat(ExpenseCategoryHandler categoryHandler) {
        ExpenseCategoryDAO categoryDAO = new ExpenseCategoryDAO();

        categoryDAO.setName("Food");
        categoryHandler.create(categoryDAO);

        categoryDAO.setName("Shopping");
        categoryHandler.create(categoryDAO);
    }

    public void setDate(View view) {
        DialogFragment datePickerFragment = new DatePickerDialog();
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void setTime(View view) {
        DialogFragment timeDialogFragment = new TimePickerDialog();
        timeDialogFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void submitClick(View view) {
        Button bDate = (Button) findViewById(R.id.bDate);
        Button bTime = (Button) findViewById(R.id.bTime);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(SPINNER_CURR_KEY, currSpinner.getSelectedItemPosition());
        editor.putInt(SPINNER_CAT_KEY, catSpinner.getSelectedItemPosition());
        editor.commit();
        boolean isBefore = true;
        Date selectedDate = null;
        try {
            String selectedDateStr = (bDate.getText().toString() + " " + bTime.getText().toString());
            selectedDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(selectedDateStr);
            isBefore = selectedDate.before(new Date());
        } catch (ParseException e) {
            //System.out.println("--> Wrong Date");
            e.printStackTrace();
        }
        if(isBefore && catSpinner.getCount() != 0) {
            insertExpense(selectedDate);
        } else {
            Toast.makeText(this, "Wrong Date or category", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Get the selected date and add the expense to the expenses table
     * @param selectedDate selected date.
     */
    private void insertExpense(Date selectedDate) {
        TextView tvRate = (TextView) findViewById(R.id.tvRate);
        float amount = (Float.valueOf(tvRate.getText().toString()));
        String base = prefs.getString(SettingsActivity.DEF_BASE, "ILS");
        CurrenciesHandler currenciesHandler = new CurrenciesHandler(this);
        CurrenciesDAO currenciesDAO = currenciesHandler.getByName(new CurrenciesDAO(base));
        int currencyId = currenciesDAO.getId();
        ExpenseCategoryDAO categoryDAO = (ExpenseCategoryDAO) catSpinner.getSelectedItem();
        ExpenseCategoryHandler expenseCategoryHandler = new ExpenseCategoryHandler(this);
        categoryDAO = expenseCategoryHandler.getByName(categoryDAO);
        int catId = categoryDAO.getId();
        EditText etDesc = (EditText) findViewById(R.id.etDesc);
        String desc = etDesc.getText().toString();
        EditText etNote = (EditText) findViewById(R.id.etNote);
        String note = etNote.getText().toString();
        ExpenseDAO expenseDAO = new ExpenseDAO(amount, currencyId, selectedDate, catId, desc, note);
        ExpensesHandler expensesHandler = new ExpensesHandler(this);
        expensesHandler.create(expenseDAO);
        Intent intent = new Intent(this, ExpenseSummaryActivity.class);
        intent.putExtra("insertedDate", selectedDate);
        startActivity(intent);
        if(((CheckBox)findViewById(R.id.cbToCalendar)).isChecked()){
            addEvent(categoryDAO.getName(), desc, note, amount, base, selectedDate);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        isEmpty = etAmount.getText().toString().equals("");
    }

    @Override
    public void afterTextChanged(Editable editable) {
        TextView tvRate = (TextView) findViewById(R.id.tvRate);
        if (!isEmpty) {
            tvRate.setText(String.valueOf(calcAmount()));
            Button bSubmit = (Button) findViewById(R.id.bSubmit);
            bSubmit.setEnabled(true);
        } else {
            tvRate.setText("0");
            Button bSubmit = (Button) findViewById(R.id.bSubmit);
            bSubmit.setEnabled(false);
        }
    }

    /**
     * Calculate the amount from one currency to other currency(Base)
     * @return Amount(float)
     */
    private float calcAmount(){
        int selectedCurrPos = currSpinner.getSelectedItemPosition();
        CurrenciesDAO selectedCurrObj = currencies.get(selectedCurrPos);
        RatesHandler ratesHandler = new RatesHandler(this);
        float selectedCurrRate = ratesHandler.getById(selectedCurrObj).getExchange();
        //System.out.println("--> selectedCurrName = " + selectedCurrObj.getName());
        //System.out.println("--> selectedCurrRate = " + selectedCurrRate);
        String base = prefs.getString(SettingsActivity.DEF_BASE, "ILS");
        //System.out.println("--> selectedDefRate = " + base);
        CurrenciesHandler currenciesHandler = new CurrenciesHandler(this);
        CurrenciesDAO baseCurrObj = currenciesHandler.getByName(new CurrenciesDAO(base));
        float baseCurrRate = ratesHandler.getById(baseCurrObj).getExchange();
        return (baseCurrRate / selectedCurrRate) * Float.valueOf(etAmount.getText().toString());

    }

    public void editCatClick(View view) {
        Dialog dialog = new EditCategoryDialog(this, android.R.style.Theme_DeviceDefault_Dialog_Alert);
        dialog.show();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                onResume();
            }
        });
    }

    public void addEvent(String title, String desc, String note, float amount, String base, Date selectedDate){
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);
        intent.putExtra(CalendarContract.Events.TITLE, title + " - " + desc);
        intent.putExtra(CalendarContract.Events.DESCRIPTION, note + "\n" + amount + " " + base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, toCalendar(selectedDate).getTimeInMillis());
            intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, toCalendar(selectedDate).getTimeInMillis() + 5*60*1000);
        }
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Calendar toCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
}

