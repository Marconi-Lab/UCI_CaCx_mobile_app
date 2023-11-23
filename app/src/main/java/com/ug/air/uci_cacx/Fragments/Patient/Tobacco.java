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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ug.air.uci_cacx.R;
import com.ug.air.uci_cacx.Utils.FunctionalUtils;

public class Tobacco extends Fragment {

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    View view;
    Button next_btn, back_btn;
    RadioGroup radioGroup1, radioGroup2;
    LinearLayout linearLayout;
    String tobacco, years;
    public static  final String TOBACCO ="tobacco_use";
    public static  final String YEARS ="years_on_tobacco";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tobacco, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        next_btn = view.findViewById(R.id.next);
        back_btn = view.findViewById(R.id.back);

        radioGroup1 = view.findViewById(R.id.radioGroup_1);
        radioGroup2 = view.findViewById(R.id.radioGroup_2);
        linearLayout = view.findViewById(R.id.nin_layout);

        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton selectedRadioButton = view.findViewById(checkedId);
                tobacco = selectedRadioButton.getText().toString();

                if (tobacco.equals("Yes")) {
                    linearLayout.setVisibility(View.VISIBLE);
                }
                else {
                    linearLayout.setVisibility(View.GONE);
                    years = "";
                }
            }
        });

        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton selectedRadioButton = view.findViewById(checkedId);
                years = selectedRadioButton.getText().toString();
            }
        });

        load_data();
        update_views();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new Origin());
                fr.commit();
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tobacco.isEmpty()) {
                    Toast.makeText(requireActivity(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
                else if (tobacco.equals("Yes") && years.isEmpty()){
                    Toast.makeText(requireActivity(), "Please provide the duration", Toast.LENGTH_SHORT).show();
                }
                else {
                    save_data();
                }
            }
        });

        return view;
    }

    private void save_data() {
        editor.putString(TOBACCO, tobacco);
        editor.putString(YEARS, years);
        editor.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Hiv_Status());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void load_data() {
        tobacco = sharedPreferences.getString(TOBACCO, "");
        years = sharedPreferences.getString(YEARS, "");
    }

    private void update_views() {

        if (!tobacco.isEmpty()){
            FunctionalUtils.setRadioButton(radioGroup1, tobacco);

            if (tobacco.equals("Yes")) {
                linearLayout.setVisibility(View.VISIBLE);
                FunctionalUtils.setRadioButton(radioGroup2, years);
            }

        }
    }
}