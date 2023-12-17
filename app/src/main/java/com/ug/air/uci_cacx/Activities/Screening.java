package com.ug.air.uci_cacx.Activities;


import static com.ug.air.uci_cacx.Fragments.Patient.Citizen.CITIZEN;
import static com.ug.air.uci_cacx.Fragments.Patient.Clinicians.COMPLETE;
import static com.ug.air.uci_cacx.Fragments.Patient.Identification.FIRST_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.ug.air.uci_cacx.Fragments.Patient.Citizen;
import com.ug.air.uci_cacx.Fragments.Patient.Clinicians;
import com.ug.air.uci_cacx.Fragments.Patient.Height;
import com.ug.air.uci_cacx.Fragments.Patient.Identification;
import com.ug.air.uci_cacx.Fragments.Patient.Nok_1;
import com.ug.air.uci_cacx.Fragments.Patient.Prior_Screening_1;
import com.ug.air.uci_cacx.Fragments.Patient.Residence;
import com.ug.air.uci_cacx.Fragments.Patient.Tobacco;
import com.ug.air.uci_cacx.Fragments.Patient.Visit;
import com.ug.air.uci_cacx.R;
import com.ug.air.uci_cacx.Utils.FunctionalUtils;

public class Screening extends AppCompatActivity {

    public static final String SHARED_PREFS ="shared_pref";
//    public static  final String TOKEN ="access_token";
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screening);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, new Clinicians());
//        fragmentTransaction.add(R.id.fragment_container, new Citizen());
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.exit_layout);
        dialog.setCancelable(true);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);

        Button btnYes = dialog.findViewById(R.id.yes);
        Button btnNo = dialog.findViewById(R.id.no);

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean complete = sharedPreferences.getBoolean(COMPLETE, false);
                if (!sharedPreferences.getString(FIRST_NAME, "").isEmpty()) {
                    FunctionalUtils.save_file(Screening.this, complete);
                }
                dialog.dismiss();
                startActivity(new Intent(Screening.this, Home.class));
            }
        });

        dialog.show();

    }
}