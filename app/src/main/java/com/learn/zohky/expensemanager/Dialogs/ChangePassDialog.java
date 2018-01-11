package com.learn.zohky.expensemanager.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.learn.zohky.expensemanager.Activities.LoginActivity;
import com.learn.zohky.expensemanager.R;

import static android.content.Context.MODE_PRIVATE;

public class ChangePassDialog extends Dialog implements View.OnClickListener {

    public ChangePassDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public ChangePassDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pass_dialog);
        Button bSavePass = (Button)findViewById(R.id.bSavePass);
        bSavePass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        SharedPreferences prefs = getContext().getSharedPreferences(LoginActivity.PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        EditText etNewPass = (EditText) findViewById(R.id.etNewPass);
        EditText etNewPassAgain = (EditText) findViewById(R.id.etNewPassAgain);
        String newPass = etNewPass.getText().toString();
        String newPassAgain = etNewPassAgain.getText().toString();
        if (!newPass.equals("") && newPass.equals(newPassAgain)) {
            editor.putString(LoginActivity.PASSWORD_KEY, newPass);
            editor.commit();
            Toast.makeText(getContext(), "The changes have been saved!", Toast.LENGTH_SHORT).show();
            this.dismiss();
        } else {
            Toast.makeText(getContext(), "wrong password", Toast.LENGTH_SHORT).show();
        }
    }
}
