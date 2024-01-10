package com.ug.air.uci_cacx.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.ug.air.uci_cacx.Fragments.Patient.Citizen;
import com.ug.air.uci_cacx.R;

public class Register extends AppCompatActivity {

    public static final String REGISTER_SHARED_PREFS ="register_pref";
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sharedPreferences = getSharedPreferences(REGISTER_SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, new Citizen());
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Register.this, FormMenu.class));
        finish();
    }
}