package com.learn.zohky.expensemanager.DAOs;


public class CurrenciesDAO {
    private int id;
    private String name;

    public CurrenciesDAO(){}
    public CurrenciesDAO(String name){
        setName(name);
    }
    public CurrenciesDAO(int id, String name){
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

