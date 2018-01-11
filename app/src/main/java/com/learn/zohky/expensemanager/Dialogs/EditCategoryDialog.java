package com.learn.zohky.expensemanager.Dialogs;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.*;

import com.learn.zohky.expensemanager.DAOs.ExpenseCategoryDAO;
import com.learn.zohky.expensemanager.Handler.ExpenseCategoryHandler;
import com.learn.zohky.expensemanager.R;

import java.util.List;

public class EditCategoryDialog extends Dialog implements View.OnClickListener {

    private Spinner catSpinner;
    private ExpenseCategoryHandler categoryHandler;


    public EditCategoryDialog(@NonNull Context context) {
        super(context);
    }

    public EditCategoryDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_category_dialog);
        setCatSpinner();
        Button bAdd = (Button)findViewById(R.id.bAdd);
        Button bRemove = (Button)findViewById(R.id.bRemove);
        bAdd.setOnClickListener(this);
        bRemove.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bRemove){
            deleteCategory();
        } else if (v.getId() == R.id.bAdd){
            addCategory();
        }
        setCatSpinner();
    }

    private void setCatSpinner(){
        catSpinner = (Spinner) findViewById(R.id.RemoveCategorySpinner);
        categoryHandler = new ExpenseCategoryHandler(getContext());
        categoryHandler.deleteNotUsed();
        List<ExpenseCategoryDAO> category = (List<ExpenseCategoryDAO>) categoryHandler.getAllNotDeleted();
        ArrayAdapter<ExpenseCategoryDAO> adapter2 =
                new ArrayAdapter<ExpenseCategoryDAO>(getContext(), R.layout.spinner_item, R.id.text, category);
        catSpinner.setAdapter(adapter2);
    }

    private void addCategory() {
        ExpenseCategoryDAO categoryDAO = new ExpenseCategoryDAO();
        EditText etAddCat = (EditText)findViewById(R.id.etAddCat);
        categoryDAO.setName(etAddCat.getText().toString());
        if (!etAddCat.getText().toString().isEmpty()) {
            if (categoryHandler.checkIfExistsAndShow(categoryDAO)) {
                Toast.makeText(getContext(), "Category already exists", Toast.LENGTH_SHORT).show();
            } else {
                categoryHandler.create(categoryDAO);
                Toast.makeText(getContext(), "Category added", Toast.LENGTH_SHORT).show();
                etAddCat.setText("");
            }
        }
    }

    private void deleteCategory() {
            ExpenseCategoryDAO catSpinnerSelectedItem = (ExpenseCategoryDAO) catSpinner.getSelectedItem();
            categoryHandler.hide(catSpinnerSelectedItem);
    }
}
