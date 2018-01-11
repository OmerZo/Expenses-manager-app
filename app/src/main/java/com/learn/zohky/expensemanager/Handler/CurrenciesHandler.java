package com.learn.zohky.expensemanager.Handler;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.learn.zohky.expensemanager.DAOs.CurrenciesDAO;
import com.learn.zohky.expensemanager.DbHelper;

import java.util.ArrayList;
import java.util.Collection;

public class CurrenciesHandler {

    private DbHelper dbHelper;

    public CurrenciesHandler(Context context) {
        this.dbHelper = new DbHelper(context);
    }

    /**
     * Insert new currency into the currencies table
     * @param currenciesDAO The object of the new currency to insert
     */
    public void create(CurrenciesDAO currenciesDAO) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String str =
                "INSERT INTO " + DbHelper.CURRENCIES_TITLE + " VALUES(null, '"
                + currenciesDAO.getName() + "');";
        db.execSQL(str);
//        System.out.println("--> CurrenciesHandler: create: " + str);
        db.close();
        dbHelper.close();
    }


    public CurrenciesDAO getById(CurrenciesDAO currenciesDao){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String str = "SELECT * FROM " + DbHelper.CURRENCIES_TITLE + " WHERE " + DbHelper.ID + " LIKE " + currenciesDao.getId() + ";";
//        System.out.println("--> CurrenciesHandler: getById: " + str);
        Cursor cursor = db.rawQuery(str, null);
        cursor.moveToFirst();
        CurrenciesDAO currenciesDAO = createDao(cursor);
        cursor.close();
        db.close();
        dbHelper.close();
        return currenciesDAO;
    }

    /**
     * Get the related currency object to the currency name
     * @param currenciesDao The name of the CurrenciesDAO to get from the db
     * @return CurrenciesDAO object
     */
    public CurrenciesDAO getByName(CurrenciesDAO currenciesDao){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String str = ("SELECT * FROM " + DbHelper.CURRENCIES_TITLE + " WHERE " + DbHelper.CURRENCIES_NAME
                + " = '" + currenciesDao.getName() + "';");
//        System.out.println("--> CurrenciesHandler: getByName: " + str);
        Cursor cursor = db.rawQuery(str, null);
        cursor.moveToFirst();
        CurrenciesDAO currenciesDAO = createDao(cursor);
        cursor.close();
        db.close();
        dbHelper.close();
        return currenciesDAO;
    }

    /**
     * Get list with all the currencies objects
     * @return currenciesDao collection
     */
    public Collection<CurrenciesDAO> getAll(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String str = "SELECT * FROM " + DbHelper.CURRENCIES_TITLE + ";";
//        System.out.println("--> CurrenciesHandler: getAll: " + str);
        Cursor cursor = db.rawQuery(str, null);
        ArrayList<CurrenciesDAO> results = new ArrayList<>();
        if (cursor.moveToFirst()) {
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
     * @param currenciesDAO The name of the CurrenciesDAO to check if exists
     * @return True if currencies exists
     */
    public boolean checkIfExists(CurrenciesDAO currenciesDAO) {
        boolean flag;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String str = "SELECT " + DbHelper.CURRENCIES_NAME + " FROM " +
                DbHelper.CURRENCIES_TITLE + " WHERE " + DbHelper.CURRENCIES_NAME +
                " LIKE '" + currenciesDAO.getName() + "';";
//        System.out.println("--> CurrenciesHandler: checkIfExists: " + str);
        Cursor cursor = db.rawQuery(str, null);
        flag = cursor.moveToFirst();
        cursor.close();
        db.close();
        dbHelper.close();
        return flag;
    }

    /**
     * Create the currenciesDao from the cursor
     * @return currenciesDAO
     */
    private CurrenciesDAO createDao(Cursor cursor){
        return new CurrenciesDAO(cursor.getInt(0), cursor.getString(1));
    }

}
