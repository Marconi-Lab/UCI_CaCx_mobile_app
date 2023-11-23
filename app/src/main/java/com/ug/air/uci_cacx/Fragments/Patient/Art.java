package com.ug.air.uci_cacx.Fragments.Patient;

import static com.ug.air.uci_cacx.Activities.Screening.SHARED_PREFS;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ug.air.uci_cacx.R;
import com.ug.air.uci_cacx.Utils.FunctionalUtils;


public class Art extends Fragment {

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    View view;
    Button next_btn, back_btn;
    RadioGroup radioGroup;
    LinearLayout linearLayout;
    EditText editText_regimen, editText_years;
    String art, years, regimen;
    int year;
    public static  final String ART ="on_ART";
    public static  final String LONG ="years_on_ART";
    public static  final String REGIMEN ="art_regimen";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_art, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        next_btn = view.findViewById(R.id.next);
        back_btn = view.findViewById(R.id.back);

        radioGroup = view.findViewById(R.id.radioGroup_1);
        editText_regimen = view.findViewById(R.id.regimen);
        editText_years = view.findViewById(R.id.years);
        linearLayout = view.findViewById(R.id.nin_layout);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton selectedRadioButton = view.findViewById(checkedId);
                art = selectedRadioButton.getText().toString();

                linearLayout.setVisibility(View.VISIBLE);
                if (art.equals("Yes")) {
                    linearLayout.setVisibility(View.VISIBLE);
                }
                else {
                    linearLayout.setVisibility(View.GONE);
                    editText_regimen.setText("");
                    editText_years.setText("");
                }
            }
        });

        load_data();
        update_views();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new Hiv_Status());
                fr.commit();
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regimen = editText_regimen.getText().toString().trim();
                years = editText_years.getText().toString().trim();

                if (art.isEmpty()){
                    Toast.makeText(requireActivity(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
                else if(art.equals("Yes") && (regimen.isEmpty() || years.isEmpty())){
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
        editor.putString(ART, art);
        editor.putString(REGIMEN, regimen);
        if (years.isEmpty()){
            editor.putInt(LONG, 0);
        }
        else {
            editor.putInt(LONG, Integer.parseInt(years));
        }

        editor.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Parity());
        fr.addToBackStack(null);
        fr.commit();
    }

    public void load_data(){
        art = sharedPreferences.getString(ART, "");
        regimen = sharedPreferences.getString(REGIMEN, "");
        year = sharedPreferences.getInt(LONG, 0);
    }

    private void update_views(){
        if (!art.isEmpty()){
            FunctionalUtils.setRadioButton(radioGroup, art);

            if (art.equals("Yes")){
                linearLayout.setVisibility(View.VISIBLE);
                FunctionalUtils.checkZeroValue(editText_years, year);
                editText_regimen.setText(regimen);
            }
        }
    }
}