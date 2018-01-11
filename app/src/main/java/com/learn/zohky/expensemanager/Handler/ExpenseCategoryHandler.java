package com.learn.zohky.expensemanager.Handler;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.learn.zohky.expensemanager.DAOs.ExpenseCategoryDAO;
import com.learn.zohky.expensemanager.DbHelper;

import java.util.ArrayList;
import java.util.Collection;

public class ExpenseCategoryHandler {

    private DbHelper dbHelper;
    private Context context;

    public ExpenseCategoryHandler(Context context){
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }

    /**
     * Insert new Category into the Category table.
     * @param categoryDAO The categoryDao to insert.
     */
    public void create(ExpenseCategoryDAO categoryDAO){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String str =
                "INSERT INTO " + DbHelper.CATEGORY_TITLE + " VALUES(null, '" + categoryDAO.getName() + "', 0);";
//        System.out.println("--> ExpenseCategoryHandler: create: " + str);
        db.execSQL(str);
        db.close();
        dbHelper.close();
    }

    public Collection<ExpenseCategoryDAO> getAll(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String str = "SELECT * FROM " + DbHelper.CATEGORY_TITLE + ";";
//        System.out.println("--> ExpenseCategoryHandler: getAll: " + str);
        Cursor cursor = db.rawQuery(str, null);
        ArrayList<ExpenseCategoryDAO> results = new ArrayList<>();
        if(cursor.moveToFirst()){
            do {
                results.add(createDao(cursor));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        dbHelper.close();
        return results;
    }

    /**
     * Returns all categories that have been deleted.
     * @return A collection with all the deleted categories dao.
     */
    private Collection<ExpenseCategoryDAO> getAllDeleted(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String str = "SELECT * FROM " + DbHelper.CATEGORY_TITLE + " WHERE " +
                DbHelper.CATEGORY_IS_DELETED + " = 1;";
//        System.out.println("--> ExpenseCategoryHandler: getAllDeleted: " + str);
        Cursor cursor = db.rawQuery(str, null);
        ArrayList<ExpenseCategoryDAO> results = new ArrayList<>();
        if(cursor.moveToFirst()){
            do {
                results.add(createDao(cursor));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        dbHelper.close();
        return results;
    }

    /**
     * Returns all categories that have not been deleted.
     * @return A collection with all the not deleted categories dao.
     */
    public Collection<ExpenseCategoryDAO> getAllNotDeleted(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String str = "SELECT * FROM " + DbHelper.CATEGORY_TITLE + " WHERE " +
                DbHelper.CATEGORY_IS_DELETED + " = 0;";
//        System.out.println("--> ExpenseCategoryHandler: getAllNotDeleted: " + str);
        Cursor cursor = db.rawQuery(str, null);
        ArrayList<ExpenseCategoryDAO> results = new ArrayList<>();
        if(cursor.moveToFirst()){
            do {
                results.add(createDao(cursor));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        dbHelper.close();
        return results;
    }

    /**
     * Return the corresponding object to the giving id.
     * @param id The id to get the object for.
     * @return Return the corresponding object to the giving id.
     */
    public ExpenseCategoryDAO getById(int id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String str = "SELECT * FROM " + DbHelper.CATEGORY_TITLE + " WHERE " + DbHelper.ID + " LIKE " + id + ";";
//        System.out.println("ExpenseCategoryHandler getById" + str);
        Cursor cursor = db.rawQuery(str, null);
        cursor.moveToFirst();
        ExpenseCategoryDAO categoryDAO = createDao(cursor);
        cursor.close();
        db.close();
        dbHelper.close();
        return categoryDAO;
    }


    /**
     * Return the corresponding object to the giving dao name.
     * @param categoryDAO The object how contains the name.
     * @return Return the corresponding object to the giving dao name.
     */
    public ExpenseCategoryDAO getByName(ExpenseCategoryDAO categoryDAO){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String str = ("SELECT * FROM " + DbHelper.CATEGORY_TITLE + " WHERE " + DbHelper.CATEGORY_NAME
                + " = '" + categoryDAO.getName() + "';");
//        System.out.println("--> ExpenseCategoryHandler: getByName: " + str);
        Cursor cursor = db.rawQuery(str, null);
        cursor.moveToFirst();
        categoryDAO = createDao(cursor);
        cursor.close();
        db.close();
        dbHelper.close();
        return categoryDAO;
    }

    /**
     * Delete the giving category object.
     * @param categoryDAO The object to delete.
     */
    private void delete(ExpenseCategoryDAO categoryDAO){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String str =
                "DELETE FROM " + DbHelper.CATEGORY_TITLE + " WHERE "
                        + DbHelper.ID + " = " + categoryDAO.getId() + ";";
//        System.out.println("ExpenseCategoryHandler delete " + str);
        db.execSQL(str);
        db.close();
        dbHelper.close();
    }

    /**
     * Set the giving category object as deleted.
     * @param categoryDAO The object to hide.
     */
    public void hide(ExpenseCategoryDAO categoryDAO){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String str =
                "UPDATE " + DbHelper.CATEGORY_TITLE + " SET " + DbHelper.CATEGORY_IS_DELETED + " = 1 WHERE " +
                        DbHelper.ID + " = " + categoryDAO.getId() + ";";
//        System.out.println("ExpenseCategoryHandler hide " + str);
        db.execSQL(str);
        db.close();
        dbHelper.close();
    }

    public boolean checkIfExists(ExpenseCategoryDAO categoryDAO){
        boolean flag;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String str = "SELECT " + DbHelper.ID + " FROM " + DbHelper.CATEGORY_TITLE + " WHERE "
                + DbHelper.CATEGORY_NAME + " = '" + categoryDAO.getName() + "';";
//        System.out.println("ExpenseCategoryHandler checkIfExists " + str);
        Cursor cursor = db.rawQuery(str, null);
        flag = cursor.moveToFirst();
        cursor.close();
        db.close();
        dbHelper.close();
        return flag;
    }

    /**
     * Check if the giving category object is exists and have not been set as deleted.
     * @param categoryDAO The category dao to check for.
     * @return True if exists and show.
     */
    public boolean checkIfExistsAndShow(ExpenseCategoryDAO categoryDAO){
        boolean flag;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String str = "SELECT " + DbHelper.ID + " FROM " + DbHelper.CATEGORY_TITLE + " WHERE "
                + DbHelper.CATEGORY_NAME + " = '" + categoryDAO.getName() + "' AND " +
                DbHelper.CATEGORY_IS_DELETED + " = 0;";
//        System.out.println("ExpenseCategoryHandler checkIfExistsAndShow " + str);
        Cursor cursor = db.rawQuery(str, null);
        flag = cursor.moveToFirst();
        cursor.close();
        db.close();
        dbHelper.close();
        return flag;
    }


    private ExpenseCategoryDAO createDao(Cursor cursor){
        return new ExpenseCategoryDAO(cursor.getInt(0), cursor.getString(1));
    }

    /**
     * Delete all the categories dao how are not used and set as deleted.
     */
    public void deleteNotUsed(){
        ExpensesHandler expensesHandler = new ExpensesHandler(context);
        Collection<ExpenseCategoryDAO> hidden = getAllDeleted();
        for (ExpenseCategoryDAO catDao: hidden) {
//            System.out.println("Delete not used: catDao: " + catDao.getName());
            if(!expensesHandler.isUsed(catDao)){
                delete(catDao);
            }
        }
    }
}

