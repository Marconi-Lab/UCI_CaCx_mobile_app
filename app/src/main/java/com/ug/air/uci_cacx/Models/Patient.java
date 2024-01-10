package com.ug.air.uci_cacx.Models;

public class Patient {

    String patient_id, patient_name, contact;
    int age;

    public Patient(String patient_id, String patient_name, String contact, int age) {
        this.patient_id = patient_id;
        this.patient_name = patient_name;
        this.contact = contact;
        this.age = age;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public String getContact() {
        return contact;
    }

    public int getAge() {
        return age;
    }
}
