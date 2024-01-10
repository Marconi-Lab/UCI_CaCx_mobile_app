package com.ug.air.uci_cacx.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {
    @SerializedName("results")
    List<Patient> patient;

    public Result(List<Patient> patient) {
        this.patient = patient;
    }

    public List<Patient> getPatient() {
        return patient;
    }
}
