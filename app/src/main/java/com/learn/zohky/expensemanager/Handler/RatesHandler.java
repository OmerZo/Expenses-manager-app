package com.learn.zohky.expensemanager.Handler;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.learn.zohky.expensemanager.DAOs.CurrenciesDAO;
import com.learn.zohky.expensemanager.DAOs.RatesDAO;
import com.learn.zohky.expensemanager.DbHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class RatesHandler {

    private DbHelper dbHelper;

    public RatesHandler(Context context){
        this.dbHelper = new DbHelper(context);
    }


    public RatesDAO getById(CurrenciesDAO currenciesDAO){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String str = "SELECT * FROM " + DbHelper.RATES_TITLE + " WHERE "
                + DbHelper.ID + " LIKE " + currenciesDAO.getId() + ";";
//        System.out.println("--> RatesHandler: getById: " + str);
        Cursor cursor = db.rawQuery(str, null);
        cursor.moveToFirst();
        RatesDAO ratesDAO = createDao(cursor);
        cursor.close();
        db.close();
        dbHelper.close();
        return ratesDAO;
    }

    public Collection<RatesDAO> getAll(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String str = "SELECT * FROM " + DbHelper.RATES_TITLE + ";";
//        System.out.println("--> RatesHandler: getAll: " + str);
        Cursor cursor = db.rawQuery(str, null);
        ArrayList<RatesDAO> results = new ArrayList<>();
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


    public Collection<RatesDAO> getToday(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String str;
        Date date = new Date();
        if(date.getHours() >= 16) {
            str = "SELECT * FROM " + DbHelper.RATES_TITLE
                    + " WHERE STRFTIME('%d' , 'now') = STRFTIME('%d' , " + DbHelper.RATES_DATE + " );";
        } else {
            str = "SELECT * FROM " + DbHelper.RATES_TITLE
                    + " WHERE STRFTIME('%d' , 'now' , '-1 days') = STRFTIME('%d' , " + DbHelper.RATES_DATE + " );";
        }
//        System.out.println("--> RatesHandler: getToday: " + str);
        Cursor cursor = db.rawQuery(str, null);
//        System.out.println("--> RatesHandler: getToday: cursorFirst: " + cursor.moveToFirst());
        ArrayList<RatesDAO> results = new ArrayList<>();
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

    public void create(RatesDAO ratesDAO){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String strDate = new SimpleDateFormat("yyyy-MM-dd").format(ratesDAO.getDate());
        String str =
                "INSERT INTO " + DbHelper.RATES_TITLE + " VALUES(null, '" + ratesDAO.getCurrency() +
                        "', '" + ratesDAO.getExchange() + "', '" + strDate + "');";
//        System.out.println("--> RatesHandler: create: " + str);
        db.execSQL(str);
        db.close();
        dbHelper.close();
    }


    public boolean checkIfExists(RatesDAO ratesDAO) {
        boolean flag;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String str = "SELECT " + DbHelper.RATES_DATE + " FROM " +
                DbHelper.RATES_TITLE + " WHERE " + DbHelper.RATES_DATE +
                " = '" + ratesDAO.getDate() + "' AND " + DbHelper.RATES_CURRENCY + " = '" + ratesDAO.getCurrency() + "';";
//        System.out.println("--> RatesHandler: checkIfExists: " + str);
        Cursor cursor = db.rawQuery(str, null);
        flag = cursor.moveToFirst();
        cursor.close();
        db.close();
        dbHelper.close();
        return flag;
    }

    public boolean checkIfDateExists(){
        boolean flag;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String str = "SELECT * FROM " +
                DbHelper.RATES_TITLE + " WHERE STRFTIME('%d/%m/%Y', DATE('now')) = '14/12/2017';";
//        System.out.println("--> RatesHandler: checkIfDateExists: " + str);
        Cursor cursor = db.rawQuery(str, null);
        flag = cursor.moveToFirst();
        cursor.close();
        db.close();
        dbHelper.close();
        return flag;
    }


    private RatesDAO createDao(Cursor cursor){
//        System.out.println("--> New RatesDAO: id=" + cursor.getInt(0) + " currency=" + cursor.getInt(1) + " exchange=" + cursor.getFloat(2) + " date=" + cursor.getString(3) );
        String dateS = cursor.getString(3);
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        RatesDAO ratesDAO = new RatesDAO(cursor.getInt(1), cursor.getFloat(2), date);
        ratesDAO.setId(cursor.getInt(0));
        return ratesDAO;
    }
}

