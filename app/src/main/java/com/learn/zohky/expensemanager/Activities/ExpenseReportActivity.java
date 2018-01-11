package com.learn.zohky.expensemanager.Activities;


import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.learn.zohky.expensemanager.DAOs.ExpenseDAO;
import com.learn.zohky.expensemanager.Handler.ExpenseCategoryHandler;
import com.learn.zohky.expensemanager.Handler.ExpensesHandler;
import com.learn.zohky.expensemanager.R;
import com.learn.zohky.expensemanager.Adapters.ReportArrayAdapter;

import java.util.List;


public class ExpenseReportActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    List<ExpenseDAO> expenseDAOList;
    ReportArrayAdapter reportArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_report);
        initListView();
    }


    /**
     * Initializing the Report activity with the list view
     */
    private void initListView() {
        ListView lvReport = (ListView) findViewById(R.id.lvReport);
        ExpensesHandler expensesHandler = new ExpensesHandler(this);
        expenseDAOList = (List<ExpenseDAO>) expensesHandler.getAllDateOrder();
        reportArrayAdapter = new ReportArrayAdapter(this, expenseDAOList);
        lvReport.setAdapter(reportArrayAdapter);
        lvReport.setOnItemClickListener(this);
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
        } else if (itemId == R.id.action_setting) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        final Dialog d = new Dialog(this);
        d.setContentView(R.layout.report_dialog);
        TextView tvDiaCat = (TextView) d.findViewById(R.id.tvDiaCat);
        TextView tvDiaDesc = (TextView) d.findViewById(R.id.tvDiaDesc);
        TextView tvDiaNote = (TextView) d.findViewById(R.id.tvDiaNote);
        Button bDiaDelete = (Button) d.findViewById(R.id.bDiaDelete);
        final ExpenseDAO expenseDAO = expenseDAOList.get(position);

        ExpenseCategoryHandler categoryHandler = new ExpenseCategoryHandler(this);
        String cat = categoryHandler.getById(expenseDAO.getCategory()).getName();
        tvDiaCat.setText(cat);
        tvDiaDesc.setText(expenseDAO.getDescription());
        tvDiaNote.setText(expenseDAO.getNote());
        d.show();

        bDiaDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpensesHandler expensesHandler = new ExpensesHandler(v.getContext());
                expensesHandler.deleteExpense(expenseDAO);
                d.dismiss();
                reportArrayAdapter.remove(expenseDAO);
//                reportArrayAdapter.notifyDataSetChanged();
            }
        });

    }
}
