package com.ug.air.uci_cacx.Models;

public class Facility {

    String facility_id, facility_name;

    public Facility(String facility_id, String facility_name) {
        this.facility_id = facility_id;
        this.facility_name = facility_name;
    }

    public String getFacility_id() {
        return facility_id;
    }

    public String getFacility_name() {
        return facility_name;
    }
}
