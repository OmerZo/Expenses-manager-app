package com.learn.zohky.expensemanager.Handler;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.learn.zohky.expensemanager.DAOs.ExpenseCategoryDAO;
import com.learn.zohky.expensemanager.DAOs.ExpenseDAO;
import com.learn.zohky.expensemanager.DbHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class ExpensesHandler {

    private DbHelper dbHelper;

    public ExpensesHandler(Context context) {
        this.dbHelper = new DbHelper(context);
    }

    /**
     * Insert new expense into the expense table.
     * @param expenseDAO The expenseDAO to insert.
     */
    public void create(ExpenseDAO expenseDAO) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String strDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(expenseDAO.getDatetime());
        String str =
                ("INSERT INTO " + DbHelper.EXPENSES_TITLE + " VALUES(null, '"
                        + expenseDAO.getAmount() + "', " + expenseDAO.getCurrency() + ", '" +
                        strDate + "', " + expenseDAO.getCategory() + ", '" +
                        expenseDAO.getDescription() + "', '" + expenseDAO.getNote() + "');");
//        System.out.println("--> ExpensesHandler: create: " + str);
        db.execSQL(str);
        db.close();
        dbHelper.close();
    }

    public Collection<ExpenseDAO> getAll(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String str = "SELECT * FROM " + DbHelper.EXPENSES_TITLE + ";";
//        System.out.println("--> ExpensesHandler: getAll: " + str);
        Cursor cursor = db.rawQuery(str, null);
//        System.out.println("--> ExpensesHandler: getAll: cursorFirst: " + cursor.moveToFirst());
        ArrayList<ExpenseDAO> results = new ArrayList<>();
        if(cursor.moveToFirst()) {
            do {
                results.add(createDao(cursor));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        dbHelper.close();
        return results;
    }

    /**
     * @return Return a list with all the expenses order by date from the newest to the oldest.
     */
    public Collection<ExpenseDAO> getAllDateOrder(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String str = "SELECT * FROM " + DbHelper.EXPENSES_TITLE + " ORDER BY " + DbHelper.EXPENSES_DATE + " DESC;";
//        System.out.println("--> ExpensesHandler: getAllDateOrder: " + str);
        Cursor cursor = db.rawQuery(str, null);
        ArrayList<ExpenseDAO> results = new ArrayList<>();
        if(cursor.moveToFirst()) {
            do {
                results.add(createDao(cursor));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        dbHelper.close();
        return results;
    }


    /**
     * Get a list with all the expenses of the giving date.
     * @param month The month to get the list by.
     * @param year The year to get the list by.
     * @return Get a list with all the expenses of the giving date.
     */
    public ArrayList<String> getSumByCat(String month, String year) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String str = "SELECT " + DbHelper.CATEGORY_TITLE + "." + DbHelper.CATEGORY_NAME
                + ", SUM(" + DbHelper.EXPENSES_AMOUNT + ") FROM "
                + DbHelper.EXPENSES_TITLE +
                " INNER JOIN " + DbHelper.CATEGORY_TITLE + " ON "
                + DbHelper.EXPENSES_CATEGORY + " = " + DbHelper.CATEGORY_TITLE + "." + DbHelper.ID
                + " WHERE STRFTIME('%m', " + DbHelper.EXPENSES_DATE + ") = '" + month + "'"
                + " AND STRFTIME('%Y', " + DbHelper.EXPENSES_DATE + ") = '" + year + "'"
                + " GROUP BY " + DbHelper.EXPENSES_CATEGORY + ";";


        Cursor cursor = db.rawQuery(str, null);
//        System.out.println("--> ExpensesHandler: getSumByCat: " + str);
//        System.out.println("--> ExpensesHandler: getSumByCat: cursorFirst: " + cursor.moveToFirst());
        ArrayList<String> list = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            do {
                String catName = cursor.getString(0);
//                System.out.println("--> ExpensesHandler: getSumByCat: catName: " + catName);
                float sum = cursor.getFloat(1);
//                System.out.println("--> ExpensesHandler: getSumByCat: catSum: " + sum);
                String finalStr = sum + " " + catName;
//                System.out.println("--> ExpensesHandler: getSumByCat: list: " + finalStr);
                list.add(finalStr);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        dbHelper.close();
        return list;
    }

    public ExpenseDAO getById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String str = "SELECT * FROM " + DbHelper.EXPENSES_TITLE + " WHERE " + DbHelper.ID + " LIKE " + id + ";";
//        System.out.println("ExpensesHandler getById " + str);
        Cursor cursor = db.rawQuery(str, null);
        cursor.moveToFirst();
        ExpenseDAO expenseDAO = createDao(cursor);
        cursor.close();
        db.close();
        dbHelper.close();
        return expenseDAO;
    }

    /**
     * Delete expense from the table.
     * @param expenseDAO The expense dao to delete.
     */
    public void deleteExpense(ExpenseDAO expenseDAO){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String str = "DELETE FROM " + DbHelper.EXPENSES_TITLE
                + " WHERE " + DbHelper.ID  + " = " + expenseDAO.getId() + ";";
//        System.out.println("ExpensesHandler deleteExpense " + str);
        db.execSQL(str);
        db.close();
        dbHelper.close();
    }

    private ExpenseDAO createDao(Cursor cursor) {
        String dateS = cursor.getString(3);
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ExpenseDAO expenseDAO =
                new ExpenseDAO(cursor.getFloat(1), cursor.getInt(2), date,
                        cursor.getInt(4), cursor.getString(5), cursor.getString(6));
        expenseDAO.setId(cursor.getInt(0));
        return expenseDAO;
    }

    /**
     * Check if the giving category dao is used.
     * @param categoryDAO the category to check for.
     * @return True if the category is used.
     */
    public boolean isUsed(ExpenseCategoryDAO categoryDAO){
        boolean flag;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String str = "SELECT * FROM " + DbHelper.EXPENSES_TITLE + " WHERE "
                + DbHelper.EXPENSES_CATEGORY + " = " + categoryDAO.getId() + ";";
//        System.out.println("ExpensesHandler isUsed " + str);
        Cursor cursor = db.rawQuery(str, null);
//        System.out.println("Cursor first" + cursor.moveToFirst());
        flag = cursor.moveToFirst();
        cursor.close();
        db.close();
        dbHelper.close();
        return flag;
    }


}

