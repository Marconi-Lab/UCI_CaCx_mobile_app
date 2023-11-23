package com.ug.air.uci_cacx.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.ug.air.uci_cacx.Fragments.Forms.Complete;
import com.ug.air.uci_cacx.Fragments.Forms.Incomplete;
import com.ug.air.uci_cacx.Fragments.Patient.Identification;
import com.ug.air.uci_cacx.R;

public class Forms extends AppCompatActivity {

    String frag = "none";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forms);

        Intent intent = getIntent();
        if (intent.hasExtra("Fragment")) {
            frag = intent.getExtras().getString("Fragment");
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            switch (frag) {
                case "incomplete":
                    fragmentTransaction.add(R.id.fragment_container, new Incomplete());
                    break;
                case "complete":
                    fragmentTransaction.add(R.id.fragment_container, new Complete());
                    break;
            }
            fragmentTransaction.commit();
        }
    }
}