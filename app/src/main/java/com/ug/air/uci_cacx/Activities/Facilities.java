package com.ug.air.uci_cacx.Activities;

import static com.ug.air.uci_cacx.Activities.Login.CREDENTIALS_PREFS;
import static com.ug.air.uci_cacx.Activities.Login.FACILITIES;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ug.air.uci_cacx.Models.Facility;
import com.ug.air.uci_cacx.R;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Facilities extends AppCompatActivity {

    Button save_btn;
    Spinner spinner_facility;
    String facility, facilities;
    String facility_code = "";
    ArrayList<Facility> facilityArrayList = new ArrayList<>();
    public static  final String CODE ="facility_code";
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    ArrayAdapter<CharSequence> adapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facilities);

        sharedPreferences = getSharedPreferences(CREDENTIALS_PREFS,MODE_PRIVATE);
        editor = sharedPreferences.edit();

        save_btn = findViewById(R.id.btnsign);
        spinner_facility = findViewById(R.id.spinner_facility);

        Gson gson = new Gson();
        facilities = sharedPreferences.getString(FACILITIES, null);
        Type type = new TypeToken<ArrayList<Facility>>() {}.getType();
        facilityArrayList = gson.fromJson(facilities, type);
        if (facilityArrayList == null) {
            facilityArrayList = new ArrayList<>();
            Toast.makeText(this, "There is no facility list", Toast.LENGTH_SHORT).show();
            save_btn.setEnabled(false);
        }
        else {
            save_btn.setEnabled(true);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getFacilityNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_facility.setAdapter(adapter);

        spinner_facility.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedFacility = (String) adapterView.getItemAtPosition(i);
                facility_code = getFacilityCode(selectedFacility);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (facility_code.isEmpty()){
                    Toast.makeText(Facilities.this, "Please select the facility", Toast.LENGTH_SHORT).show();
                }
                else {
                    editor.putString(CODE, facility_code);
                    editor.apply();
                    startActivity(new Intent(Facilities.this, Permissions.class));

                }
            }
        });

    }

    private ArrayList<String> getFacilityNames() {
        ArrayList<String> facilityNames = new ArrayList<>();
        for (Facility facility1: facilityArrayList) {
            facilityNames.add(facility1.getFacility_name());
        }
        return facilityNames;
    }

    private String getFacilityCode(String facilityName) {
        for (Facility facility : facilityArrayList) {
            if (facility.getFacility_name().equals(facilityName)) {
                return facility.getFacility_id();
            }
        }
        return ""; // Return an empty string or handle the case when the facility code is not found
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}