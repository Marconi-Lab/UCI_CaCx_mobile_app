package com.ug.air.uci_cacx.Fragments.Patient;

import static com.ug.air.uci_cacx.Activities.Screening.SHARED_PREFS;
import static com.ug.air.uci_cacx.Fragments.Patient.Hiv_Status.HIV;

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
import android.widget.Toast;

import com.ug.air.uci_cacx.R;
import com.ug.air.uci_cacx.Utils.FunctionalUtils;

public class Parity extends Fragment {

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    View view;
    Button next_btn, back_btn;
    EditText editText_parity, editText_partners, editText_debut, editText_abortion, editText_ectopic;
    String parity, partner, debut, abortion, ectopic;
    int pa, pr, de, gravida, ab, ec;
    public static  final String PARITY ="parity";
    public static  final String PARTNER ="number_of_sexual_partners";
    public static  final String ABORTIONS ="abortions";
    public static  final String ECTOPIC ="ectopics";
    public static  final String GRAVIDA ="gravida";
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
        editText_abortion = view.findViewById(R.id.abort);
        editText_ectopic = view.findViewById(R.id.ecto);
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
                    fr.replace(R.id.fragment_container, new Hiv_Status());
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
                abortion = editText_abortion.getText().toString().trim();
                ectopic = editText_ectopic.getText().toString().trim();

                if (parity.isEmpty() || partner.isEmpty() || debut.isEmpty() || abortion.isEmpty() || ectopic.isEmpty()){
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
        editor.putInt(ABORTIONS, Integer.parseInt(abortion));
        editor.putInt(ECTOPIC, Integer.parseInt(ectopic));
        gravida = Integer.parseInt(parity) + Integer.parseInt(abortion) + Integer.parseInt(ectopic);
        editor.putInt(GRAVIDA, gravida);
        editor.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Contraceptives());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void update_views(){
        FunctionalUtils.checkZeroValue(editText_parity, sharedPreferences.getInt(PARITY, 0));
        FunctionalUtils.checkZeroValue(editText_partners, sharedPreferences.getInt(PARTNER, 0));
        FunctionalUtils.checkZeroValue(editText_debut, sharedPreferences.getInt(DEBUT, 0));
        FunctionalUtils.checkZeroValue(editText_abortion, sharedPreferences.getInt(ABORTIONS, 0));
        FunctionalUtils.checkZeroValue(editText_ectopic, sharedPreferences.getInt(ECTOPIC, 0));
    }
}