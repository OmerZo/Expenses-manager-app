package com.learn.zohky.expensemanager.DAOs;

import java.util.Date;

public class ExpenseDAO {
    private int id;
    private float amount;
    private int currency;
    private Date datetime;
    private int category;
    private String description;
    private String note;

    public ExpenseDAO(){}
    public ExpenseDAO(float amount, int currency, Date datetime, int category, String description, String note){
        setAmount(amount);
        setCurrency(currency);
        setDatetime(datetime);
        setCategory(category);
        setDescription(description);
        setNote(note);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public Date getDatetime() {
        return this.datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

