package com.learn.zohky.expensemanager.DAOs;

import java.util.Date;


public class RatesDAO {
    private int id;
    private int currency;
    private float exchange;
    private Date date;

    public RatesDAO(){}
    public RatesDAO(int currency, float exchange, Date datetime){
        setCurrency(currency);
        setExchange(exchange);
        setDate(datetime);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public float getExchange() {
        return exchange;
    }

    public void setExchange(float exchange) {
        this.exchange = exchange;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date){
        this.date = date;
    }
}

