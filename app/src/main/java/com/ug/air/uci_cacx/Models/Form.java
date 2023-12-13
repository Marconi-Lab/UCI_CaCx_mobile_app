package com.ug.air.uci_cacx.Models;

public class Form {

    String scrrening_id, date, filename;

    public Form(String scrrening_id, String date, String filename) {
        this.scrrening_id = scrrening_id;
        this.date = date;
        this.filename = filename;
    }

    public String getScrrening_id() {
        return scrrening_id;
    }

    public String getDate() {
        return date;
    }

    public String getFilename() {
        return filename;
    }
}
