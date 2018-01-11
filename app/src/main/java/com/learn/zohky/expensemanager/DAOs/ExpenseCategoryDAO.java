package com.learn.zohky.expensemanager.DAOs;


public class ExpenseCategoryDAO {
    private int id;
    private String name;

    public ExpenseCategoryDAO(){}
    public ExpenseCategoryDAO(String name){
        setName(name);
    }
    public ExpenseCategoryDAO(int id, String name){
        setId(id);
        setName(name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }
}

