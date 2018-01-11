package com.learn.zohky.expensemanager;

import android.content.Context;
import android.database.sqlite.*;


public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "myDatabase.db";
    private  static final int EXPENSES_DB_VERSION = 1;
    public static final String ID = "_id";
    //-----------------------------------------------------
    public static final String EXPENSES_TITLE = "expenses"; //הוצאות
    public static final String EXPENSES_AMOUNT = "amount";
    private static final String EXPENSES_CURRENCY = "currency";
    public static final String EXPENSES_DATE = "datetime";
    public static final String EXPENSES_CATEGORY = "category";
    private static final String EXPENSES_DESCR = "description";
    private static final String EXPENSES_NOTE = "note";
    //-----------------------------------------------------
    public static final String RATES_TITLE = "rates"; //שערים
    public static final String RATES_CURRENCY = "currency";
    private static final String RATES_EXCHANGE = "exchange";
    public static final String RATES_DATE = "date";
    //-----------------------------------------------------
    public static final String CATEGORY_TITLE = "expense_category"; // סוגי הוצאה
    public static final String CATEGORY_NAME = "name";
    public static final String CATEGORY_IS_DELETED = "isDeleted";
    //-----------------------------------------------------
    public static final String CURRENCIES_TITLE = "currencies"; // מטבעות
    public static final String CURRENCIES_NAME = "name";


    public DbHelper(Context context) {
        super(context, DB_NAME, null, EXPENSES_DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String create_table_currencies =
                ("CREATE TABLE " + CURRENCIES_TITLE + " ( " +
                        ID + "  INTEGER PRIMARY KEY AUTOINCREMENT,  " +
                        CURRENCIES_NAME + "  TEXT NOT NULL UNIQUE);");

        String create_table_expense_category =
                ("CREATE TABLE " + CATEGORY_TITLE + " ( " +
                        ID + "  INTEGER PRIMARY KEY AUTOINCREMENT,  " +
                        CATEGORY_NAME + "  TEXT NOT NULL , " +
                        CATEGORY_IS_DELETED + " INTEGER);");

        String create_table_rates =
                ("CREATE TABLE " + RATES_TITLE + " ( " +
                        ID + "  INTEGER PRIMARY KEY AUTOINCREMENT,  " +
                        RATES_CURRENCY + "  INTEGER NOT NULL,  " +
                        RATES_EXCHANGE + "  REAL NOT NULL, " +
                        RATES_DATE + "  TEXT NOT NULL, " +
                        "FOREIGN KEY(" + RATES_CURRENCY + ") REFERENCES " + CURRENCIES_TITLE + "(" + CURRENCIES_NAME + "));");

        String create_table_expenses =
                ("CREATE TABLE " + EXPENSES_TITLE + " ( " +
                        ID + "  INTEGER PRIMARY KEY AUTOINCREMENT,  " +
                        EXPENSES_AMOUNT + "  REAL NOT NULL,  " +
                        EXPENSES_CURRENCY + "  INTEGER NOT NULL, " +
                        EXPENSES_DATE + "  TEXT NOT NULL,  " +
                        EXPENSES_CATEGORY + "  INTEGER NOT NULL, " +
                        EXPENSES_DESCR + "  TEXT,  " +
                        EXPENSES_NOTE + "  TEXT, " +
                        "FOREIGN KEY(" + EXPENSES_CURRENCY + ") REFERENCES " + CURRENCIES_TITLE + "(" + ID + "), " +
                        "FOREIGN KEY(" + EXPENSES_CATEGORY + ") REFERENCES " + CATEGORY_TITLE + "(" + ID + "));");

        try {
            db.execSQL(create_table_currencies);
            db.execSQL(create_table_expense_category);
            db.execSQL(create_table_rates);
            db.execSQL(create_table_expenses);
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
