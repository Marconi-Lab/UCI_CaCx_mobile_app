package com.ug.air.uci_cacx.Models;

public class Form {

    String screening_id, date, filename;
    boolean complete;

    public Form(String screening_id, String date, String filename, boolean complete) {
        this.screening_id = screening_id;
        this.date = date;
        this.filename = filename;
        this.complete = complete;
    }

    public String getScreening_id() {
        return screening_id;
    }

    public String getDate() {
        return date;
    }

    public String getFilename() {
        return filename;
    }

    public boolean isComplete() {
        return complete;
    }
}
