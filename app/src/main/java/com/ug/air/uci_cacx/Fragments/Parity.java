package com.ug.air.uci_cacx.Fragments;

import static com.ug.air.uci_cacx.Activities.Screening.SHARED_PREFS;
import static com.ug.air.uci_cacx.Fragments.Hiv_status.HIV;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ug.air.uci_cacx.R;

public class Parity extends Fragment {

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    View view;
    Button next_btn, back_btn;
    EditText editText_parity, editText_partners, editText_debut;
    String parity, partner, debut;
    int pa, pr, de;
    public static  final String PARITY ="parity";
    public static  final String PARTNER ="number_of_sexual_partners";
    public static  final String DEBUT ="age_at_sex_debut";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_parity, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        next_btn = view.findViewById(R.id.next);
        back_btn = view.findViewById(R.id.back);

        editText_debut = view.findViewById(R.id.age);
        editText_partners = view.findViewById(R.id.partners);
        editText_parity = view.findViewById(R.id.parity);

        update_views();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String hiv = sharedPreferences.getString(HIV, "");

                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                if (hiv.equals("Positive")){
                    fr.replace(R.id.fragment_container, new Art());
                }
                else {
                    fr.replace(R.id.fragment_container, new Hiv_status());
                }
                fr.commit();
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parity = editText_parity.getText().toString().trim();
                partner = editText_partners.getText().toString().trim();
                debut = editText_debut.getText().toString().trim();

                if (parity.isEmpty() || partner.isEmpty() || debut.isEmpty()){
                    Toast.makeText(requireActivity(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    save_data();
                }
            }
        });

        return view;
    }

    private void save_data() {
        editor.putInt(PARITY, Integer.parseInt(parity));
        editor.putInt(PARTNER, Integer.parseInt(partner));
        editor.putInt(DEBUT, Integer.parseInt(debut));
        editor.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Contraceptives());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void update_views(){
        editText_parity.setText(String.valueOf(sharedPreferences.getInt(PARITY, 0)));
        editText_partners.setText(String.valueOf(sharedPreferences.getInt(PARTNER, 0)));
        editText_debut.setText(String.valueOf(sharedPreferences.getInt(DEBUT, 0)));
    }
}