package com.learn.zohky.expensemanager.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.learn.zohky.expensemanager.DownloadJsonTask;
import com.learn.zohky.expensemanager.Handler.CurrenciesHandler;
import com.learn.zohky.expensemanager.Handler.RatesHandler;
import com.learn.zohky.expensemanager.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String DEFAULT_PASSWORD = "123456789";
    public static final String PASSWORD_KEY = "pass";
    public static final String PREF_NAME = "pref";
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        CurrenciesHandler currenciesHandler = new CurrenciesHandler(this);
        RatesHandler ratesHandler = new RatesHandler(this);

//        For different bases:
//        String base = prefs.getString(SettingsActivity.DEF_BASE, "ILS");
//        DownloadJsonTask task = new DownloadJsonTask(this, currenciesHandler, ratesHandler, base);

        DownloadJsonTask task = new DownloadJsonTask(this, currenciesHandler, ratesHandler, "ILS");

        task.execute();

        Button enter = (Button) findViewById(R.id.buttonEnter);
        enter.setOnClickListener(this);

        if (prefs.contains(PASSWORD_KEY)) {
            EditText editText = (EditText) findViewById(R.id.editText);
            editText.setText("");
            editText.setFocusable(true);
            editText.setClickable(true);
            password = prefs.getString(PASSWORD_KEY, null);
        } else {
            EditText editText = (EditText) findViewById(R.id.editText);
            editText.setFocusable(false);
            editText.setClickable(false);
            editText.setLongClickable(false);
            password = DEFAULT_PASSWORD;
        }
    }

    @Override
    public void onClick(View view) {
        EditText editText = (EditText) findViewById(R.id.editText);
        String editPass = editText.getText().toString();
        if (editPass.equals(password)) {
            Intent intent = new Intent(this, ExpenseSubmitActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Wrong password", Toast.LENGTH_SHORT).show();
        }
    }
}
