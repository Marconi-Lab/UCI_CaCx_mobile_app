package com.ug.air.uci_cacx.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.ug.air.uci_cacx.Fragments.Identification;
import com.ug.air.uci_cacx.R;

public class Screening extends AppCompatActivity {

    public static final String SHARED_PREFS ="shared_pref";
//    public static  final String TOKEN ="access_token";
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screening);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, new Identification());
        fragmentTransaction.commit();
    }
}