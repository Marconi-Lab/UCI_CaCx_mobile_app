package com.ug.air.uci_cacx.Models;

public class Form {

    String scrrening_id, date;

    public Form(String scrrening_id, String date) {
        this.scrrening_id = scrrening_id;
        this.date = date;
    }

    public String getScrrening_id() {
        return scrrening_id;
    }

    public String getDate() {
        return date;
    }
}
